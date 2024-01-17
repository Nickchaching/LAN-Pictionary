//NETWORKING:
//6000 - MAIN COMMUNICATIONS
    //General Format: DesignationID#, ActionID#, IP, Param1, Param2, Param3, Param4
        //DesignationID 0 = Intended for Server
        //DesignationID 1 = Intended for Clients
    
    //Server Intended Messages
        //Terminating Connection: 0, -1, IP
        //Initial Connection: 0, 0, IP, ClientName
        //Chat Telemetry: 0, 1, IP, Message
        //Object Choice Telemetry: 0, 2, IP, Object
        //Drawing Telemetry: 0, 3, IP, PosX, PosY, BrushSize, BrushColor

    //Client Intended Messages
        //Terminating Connection: 1, -1, IP
        //Lobby Player Info Ping: 1, 0, IP, PlayerNames[]
        //Chat Telemetry: 1, 1, IP, FormattedMessage (with player name)
        //Round Intitalization1 (Choosing): 1, 2, IP, RoundsPlayed, DrawerIP, ObjectstoChoose[]
        //Round Initializaton Telemetry: 1, 3, IP, TimeRemaining
        //Round Initialization2 (Done Choosing): 1, 4, IP, Object.charlength
        //Drawing Telemetry: 1, 5, IP, PosX, PosY, BrushSize, BrushColor
        //Game Telemetry: 1, 6, IP, TimeRemaining
        //Score Update Telemetry: 1, 7, IP, PlayerScores[][]
        //Round Completion: 1, 8, IP, Object
        //Round Initializaton Telemetry: 1, 9, IP, TimeRemaining

//6001 - INITIAL SERVER CONNECTION
    //Server Ping: IP, ServerName, PlayerCount

//SERVER STORAGE:
//Client Details: IP, Name, Points, Drawing?, Drew?

import java.io.*;
import javax.swing.*;

public class Model{
    //Properties
    View theView;

    //Game Settings
    int intPreRoundDuration = 15000;
    int intRoundDuration = 90000;
    int intPostRoundDuration = 5000;
    int intRounds = 5;
    int intAnsScore = 50;

    //Shared Properties
    boolean blnConnected = false;
    boolean blnHost;
    boolean blnGameStarted = false;
    String strUsername;
    String strIncomingSplit[];
    String strTheme;
    int intRound;
    boolean blnDrawing;
    String strChoiceObjects[];
    String strObject;
    int intObjectLength;
    int intTempDraw[] = new int[4];
    String strTempMessage;
    
    //Server Properties
    SuperSocketMaster HostSocket;
    Thread broadcastIP = new Thread(new broadcastIP(this));
    String strPlayerList[][];
    String strPlayerTemp[][];
    String strThemes[] = new String[8];
    String strObjects[];
    String strDrawer;
    Timer pingTimer;
    SuperTimer preRoundTimer;
    SuperTimer roundTimer;
    SuperTimer postRoundTimer;

    //Client Only Properties
    SuperSocketMaster ClientSocket;
    String strServerAddress;
    String[][] strServerList = new String[5][3];
    Thread findServer = new Thread(new findServer(this));
    String strPlayers[];
    double dblTimePerRemaining;
    String strTempScores[];

    //Server Methods
    //Initial Host Connection
    public boolean initializeHost(String strNameField){
        if(!strNameField.equals("")){
            strUsername = strNameField;
            blnHost = true;
            HostSocket = new SuperSocketMaster(6000, theView);
            blnConnected = HostSocket.connect();
            if(blnConnected){
                broadcastIP.start();
                pingTimer.start();
                strPlayerList = new String[1][4];
                strPlayerList[0][0] = HostSocket.getMyAddress();
                strPlayerList[0][1] = strUsername;
                strPlayerList[0][2] = "00";
            }
            return blnConnected;
        }
        else{
            return false;
        }
    }

    //BroadcastIP Message Recall
    public String getStatus(){
        return HostSocket.getMyAddress()+","+strUsername+","+strPlayerList.length;
    }

    //Retrieve Player List
    public String[][] getPlayerList(){
        return strPlayerList;
    }

    //Load and Retrieve Themes
    public String[] getThemes(){
        int intCount;
        try{
            BufferedReader themeFile = new BufferedReader(new FileReader("Assets/themes.txt"));
            for(intCount = 0; intCount < 8; intCount++){
                strThemes[intCount] = themeFile.readLine();
            }
            themeFile.close();
            strTheme = strThemes[0];
        }
        catch(FileNotFoundException e){
            System.out.println("Unfortunately, the themes file has not been found");
        }
        catch(IOException e){
            System.out.println("Unfortunately, there has been an error loading the themes");
        }
        return strThemes;
    }

    //Theme Selection Handling
    public boolean selectTheme(int intTheme){
        if(strThemes[intTheme - 1] != strTheme){
            strTheme = strThemes[intTheme - 1];
            return true;
        }
        else{
            return false;
        }
    }

    //Loading Theme Objects
    public boolean loadObjects(){
        int intCount;
        int intObjects = 0;
        try{
            BufferedReader objectFile = new BufferedReader(new FileReader("Assets/"+strTheme));
            while(objectFile.readLine() != null){
                intObjects++;
            }
            objectFile.close();
            objectFile = new BufferedReader(new FileReader("Assets/"+strTheme));
            strObjects = new String[intObjects];
            for(intCount = 0; intCount < intObjects; intCount++){
                strObjects[intCount] = objectFile.readLine();
            }
        return true;
        }
        catch(FileNotFoundException e){
            System.out.println("Unfortunately, the object file has not been found");
            return false;
        }
        catch(IOException e){
            System.out.println("Unfortunately, there has been an error loading the objects");
            return false;
        }
    }

    //Get Random Drawer
    public String getRandDrawer(){
        return strPlayerList[(int)(Math.random() * strPlayerList.length)][0];
    }

    //Get Random Objects
    public String[] getRandObjects(){
        String strRandObjects[] = new String[2];
        strRandObjects[0] = strObjects[(int)(Math.random() * strObjects.length)];
        strRandObjects[1] = strObjects[(int)(Math.random() * strObjects.length)];
        while(strRandObjects[0].equals(strRandObjects[1])){
            strRandObjects[1] = strObjects[(int)(Math.random() * strObjects.length)];
        }
        return strRandObjects;
    }

    //Start Game Procedure
    public boolean startGame(){
        blnGameStarted = loadObjects();
        if(blnGameStarted){
            intRound = 1;
        }
        return blnGameStarted;
    }

    //New Round
    public boolean newRound(){
        //Generating a new Drawer and Objects
        strDrawer = getRandDrawer();
        strChoiceObjects = getRandObjects();

        //Informing Clients of Round Start
        HostSocket.sendText("1,2,"+HostSocket.getMyAddress()+","+intRound+","+strDrawer+","+String.join(",", strChoiceObjects));

        preRoundTimer.start();

        //Checking if the Host is the Drawer
        if(strDrawer.equals(HostSocket.getMyAddress())){
            blnDrawing = true;
            return true;
        }
        else{
            blnDrawing = false;
            return false;
        }

    }

    //Starting a Round (Drawing)
    public void startRound(){
        preRoundTimer.stop();
        intObjectLength = strObject.length();
        
        int intCount;
        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            strPlayerList[intCount][3] = "0";
        }

        HostSocket.sendText("1,4,"+HostSocket.getMyAddress()+","+intObjectLength);
        roundTimer.start();
    }

    //Ending a Round
    public void endRound(){
        roundTimer.stop();

        int intCount;
        int intCounter = 0;
        int intDrawer = -1;
        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            if(strPlayerList[intCount][3].equals("1")){
                intCounter++;
            }
            if(strPlayerList[intCount][0].equals(strDrawer)){
                intDrawer = intCount;
            }
        }

        strPlayerList[intDrawer][2] = ""+(Integer.parseInt(strPlayerList[intDrawer][2]) + (int)(intAnsScore * 2 * (intCounter/(double)(strPlayerList.length - 1))));
        HostSocket.sendText("1,8,"+HostSocket.getMyAddress()+","+strObject);
        postRoundTimer.start();
    }

    public boolean resetRound(){
        postRoundTimer.stop();
        
        //Reset Properties

        if(intRound == intRounds){
            //Show Leaderboard and Send Client Message
            return false;
        }
        else{
            intRound++;
            return true;
        }
    }

    //(RE)-Broadcasts Drawing Data to Clients
    public void sendDrawData(int intDrawData[]){
        HostSocket.sendText("1,5,"+HostSocket.getMyAddress()+","+intDrawData[0]+","+intDrawData[1]+","+intDrawData[2]+","+intDrawData[3]);
    }

    //(RE)-Broadcasts Chat Data to Clients
    public String sendChatData(String strIP, String strChatData){
        int intCount;
        String strFormattedChatData = "";
        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            if(strPlayerList[intCount][0].equals(strIP)){
                strFormattedChatData = strPlayerList[intCount][1];
                break;
            }
        }

        if(strChatData.equalsIgnoreCase(strObject) && strPlayerList[intCount][3].equals("0") && !strPlayerList[intCount][0].equals(strDrawer)){
            strFormattedChatData = strFormattedChatData + " got the right answer";

            //Score Increment
            strPlayerList[intCount][2] = "" + (Integer.parseInt(strPlayerList[intCount][2]) + intAnsScore);
            strPlayerList[intCount][3] = "1";
        }
        else if(strChatData.equalsIgnoreCase(strObject)){
            strFormattedChatData = strFormattedChatData + " is trying to ruin the fun";
        }
        else{
            strFormattedChatData = strFormattedChatData + ": " + strChatData;
        }

        HostSocket.sendText("1,1,"+HostSocket.getMyAddress()+","+strFormattedChatData);
        return strFormattedChatData;
    }

    //Get Object Choices
    public String[] getObjectChoices(){
        return strChoiceObjects;
    }

    //Get Round Info
    public int getRound(){
        return intRound;
    }

    //Server Message Handling
    public int serverMessageRecieved(){
        strIncomingSplit = HostSocket.readText().split(",");
        
        //Message Type 0: Initial Connection
        if(strIncomingSplit[1].equals("0")){
            System.out.println("The server has accepted a new client, "+strIncomingSplit[3]+", at "+strIncomingSplit[2]);
            int intCount;
            strPlayerTemp = strPlayerList;
            strPlayerList = new String[strPlayerTemp.length + 1][4];
            for(intCount = 0; intCount < strPlayerTemp.length; intCount++){
                strPlayerList[intCount] = strPlayerTemp[intCount];
            }
            strPlayerList[strPlayerTemp.length][0] = strIncomingSplit[2];
            strPlayerList[strPlayerTemp.length][1] = strIncomingSplit[3];
            strPlayerList[strPlayerTemp.length][2] = "00";
        }
        //Message Type 1: Chat Telemetry
        else if(strIncomingSplit[1].equals("1")){
            strTempMessage = sendChatData(strIncomingSplit[2], strIncomingSplit[3]);
        }
        //Message Type 2: Object Choice Telemetry
        else if(strIncomingSplit[1].equals("2")){
            strObject = strIncomingSplit[3];
            startRound();
        }
        //Message Type 3: Drawing Telemetry
        else if(strIncomingSplit[1].equals("3")){
            intTempDraw[0] = Integer.parseInt(strIncomingSplit[3]);
            intTempDraw[1] = Integer.parseInt(strIncomingSplit[4]);
            intTempDraw[2] = Integer.parseInt(strIncomingSplit[5]);
            intTempDraw[3] = Integer.parseInt(strIncomingSplit[6]);
            sendDrawData(intTempDraw);
        }

        return Integer.parseInt(strIncomingSplit[1]);

        //Split the server message by commas
        //Check index 1 for message type
        //Within if-statements, trigger appropriate actions
    }

    //Regular Server Data Pings
    public String sendPing(int intType){
        //Lobby Player Info Ping
        if(intType == 0 && strPlayerList != null){
            int intCount;
            String strEncode1[] = new String[strPlayerList.length];
            for(intCount = 0; intCount < strPlayerList.length; intCount++){
                strEncode1[intCount] = strPlayerList[intCount][1];
            }
            HostSocket.sendText("1,0,"+HostSocket.getMyAddress()+","+String.join(",", strEncode1));
        }
        //Round Initialization Ping
        else if(intType == 1){
            HostSocket.sendText("1,3,"+HostSocket.getMyAddress()+","+(preRoundTimer.getRemainingTime()*100.0/intPreRoundDuration));
            return (preRoundTimer.getRemainingTime()*100.0/intPreRoundDuration)+"";
        }
        else if(intType == 2){
            HostSocket.sendText("1,6,"+HostSocket.getMyAddress()+","+(roundTimer.getRemainingTime()*100.0/intRoundDuration));
            return (roundTimer.getRemainingTime()*100.0/intRoundDuration)+"";
        }
        else if(intType == 3){
            HostSocket.sendText("1,9,"+HostSocket.getMyAddress()+","+(postRoundTimer.getRemainingTime()*100.0/intPostRoundDuration));
            return (postRoundTimer.getRemainingTime()*100.0/intPostRoundDuration)+"";
        }

        //Defualt Return
        return null;
    }

    //Score Change Update Ping
    public String[] changedScore(){
        String strPlayerListTemp[][] = strPlayerList;
        String strTemp[] = new String[strPlayerList.length];
        int intCount;
        int intCount2;
        int intScoreAbove;
        int intScoreBelow;

        //Bubble Sorting Scores
        String strTemp2[];
        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            for(intCount2 = 0; intCount2 < strPlayerList.length - intCount - 1; intCount2++){
                intScoreAbove = Integer.parseInt(strPlayerListTemp[intCount2][2]);
                intScoreBelow = Integer.parseInt(strPlayerListTemp[intCount2 + 1][2]);
                if(intScoreBelow > intScoreAbove){
                    strTemp2 = strPlayerListTemp[intCount2 + 1];
                    strPlayerListTemp[intCount2 + 1] = strPlayerListTemp[intCount2];
                    strPlayerListTemp[intCount2] = strTemp2;
                }
            }
        }

        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            strTemp[intCount] = strPlayerListTemp[intCount][2]+" PTS: "+strPlayerListTemp[intCount][1];
            //Display Edge Buffering
            strTemp[intCount] = "  " + strTemp[intCount];
        }

        HostSocket.sendText("1,7,"+HostSocket.getMyAddress()+","+String.join(",", strTemp));
        return strTemp;
    }

    //Game End Update Ping
    public String[] endGamePing(){
        String strPlayerListTemp[][] = strPlayerList;
        String strTemp[] = new String[strPlayerList.length];
        int intCount;
        int intCount2;
        int intScoreAbove;
        int intScoreBelow;

        //Bubble Sorting Scores
        String strTemp2[];
        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            for(intCount2 = 0; intCount2 < strPlayerList.length - intCount - 1; intCount2++){
                intScoreAbove = Integer.parseInt(strPlayerListTemp[intCount2][2]);
                intScoreBelow = Integer.parseInt(strPlayerListTemp[intCount2 + 1][2]);
                if(intScoreBelow > intScoreAbove){
                    strTemp2 = strPlayerListTemp[intCount2 + 1];
                    strPlayerListTemp[intCount2 + 1] = strPlayerListTemp[intCount2];
                    strPlayerListTemp[intCount2] = strTemp2;
                }
            }
        }

        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            strTemp[intCount] = strPlayerListTemp[intCount][2]+" PTS: "+strPlayerListTemp[intCount][1];
            //Display Edge Buffering
            strTemp[intCount] = "  " + strTemp[intCount];
        }

        HostSocket.sendText("1,10,"+HostSocket.getMyAddress()+","+String.join(",", strTemp));
        return strTemp;
    }

    //Score Updated Check
    public boolean checkScoreUpdated(){
        if(strTempMessage.split(":").length == 1){
            return true;
        }
        else{
            return false;
        }
    }

    //Out of Time Send Object
    public void outTimeObjectPing(){
        HostSocket.sendText("1,11,"+HostSocket.getMyAddress()+","+strObject);
    }

    //Client Methods
    //Initial Client Connection
    public boolean initializeClient(String strNameField){
        if(!strNameField.equals("")){
            strUsername = strNameField;
            blnHost = false;
            findServer.start();
            return true;
        }
        else{
            return false;
        }
    }

    //Client Server Selection
    public boolean initializeClientConnection(int intButton){
        strServerAddress = strServerList[intButton - 1][0];
        System.out.println(strServerAddress);
        ClientSocket = new SuperSocketMaster(strServerAddress, 6000, theView);
        blnConnected = ClientSocket.connect();
        if(blnConnected){
            ClientSocket.sendText("0,0,"+ClientSocket.getMyAddress()+","+strUsername);
            return true;
        }
        else{
            //Remove the server from the list and update the list to ensure no gaps
            //Disconnect the socket
            return false;
        }
    }

    //FindServer Server List Update
    public void updateServerList(String[] strServerLoad){
        int intCount = 0;
        
        //PROGRAM TO ALLOW OVERWRITING IF SAME IP IN THE FUTURE

        //Check to see if IP Exists and Slots Filled
        for(intCount = 0; intCount < 5; intCount++){
            if(strServerList[intCount][0] == null){
                strServerList[intCount] = strServerLoad;
                intCount = 5;
            }
            else if(strServerList[intCount][0].equals(strServerLoad[0])){
                intCount = 5;
            }
        }

        System.out.println("New PARSE");
        System.out.println("Server IP: "+strServerList[0][0]+" | Server Name: "+strServerList[0][1]+" | Server Players: "+strServerList[0][2]);
        System.out.println("Server IP: "+strServerList[1][0]+" | Server Name: "+strServerList[1][1]+" | Server Players: "+strServerList[1][2]);
        System.out.println("Server IP: "+strServerList[2][0]+" | Server Name: "+strServerList[2][1]+" | Server Players: "+strServerList[2][2]);
        System.out.println("Server IP: "+strServerList[3][0]+" | Server Name: "+strServerList[3][1]+" | Server Players: "+strServerList[3][2]);
        System.out.println("Server IP: "+strServerList[4][0]+" | Server Name: "+strServerList[4][1]+" | Server Players: "+strServerList[4][2]);

        if(strServerList[0][0] != null){
            theView.updateServerButton(strServerList[0][1]+" | "+strServerList[0][0]+" | "+strServerList[0][2]+" Players", 1);
        }
        if(strServerList[1][0] != null){
            theView.updateServerButton(strServerList[1][1]+" | "+strServerList[1][0]+" | "+strServerList[1][2]+" Players", 2);
        }
        if(strServerList[2][0] != null){
            theView.updateServerButton(strServerList[2][1]+" | "+strServerList[2][0]+" | "+strServerList[2][2]+" Players", 3);
        }
        if(strServerList[3][0] != null){
            theView.updateServerButton(strServerList[3][1]+" | "+strServerList[3][0]+" | "+strServerList[3][2]+" Players", 4);
        }
        if(strServerList[4][0] != null){
            theView.updateServerButton(strServerList[4][1]+" | "+strServerList[4][0]+" | "+strServerList[4][2]+" Players", 5);
        }
    }

    //Client Message Handling
    public int clientMessageRecieved(){
        strIncomingSplit = ClientSocket.readText().split(",");

        //Message Type 0: Lobby Player Info Ping
        if(strIncomingSplit[1].equals("0")){
            int intLength = strIncomingSplit.length - 3;
            int intCount;
            String strDecode[] = new String[intLength];
            for(intCount = 0; intCount < intLength; intCount++){
                strDecode[intCount] = strIncomingSplit[intCount + 3];
            }
            strPlayers = strDecode;
        }

        //Message Type 1: Chat Telemetry
        else if(strIncomingSplit[1].equals("1")){
            strTempMessage = strIncomingSplit[3];
        }

        //Message Type 2: Start of a New Round
        else if(strIncomingSplit[1].equals("2")){
            intRound = Integer.parseInt(strIncomingSplit[3]);
            if(strIncomingSplit[4].equals(ClientSocket.getMyAddress())){
                blnDrawing = true;
                strChoiceObjects = new String[2];
                strChoiceObjects[0] = strIncomingSplit[5];
                strChoiceObjects[1] = strIncomingSplit[6];
            }
            else{
                blnDrawing = false;
            }
        }

        //Message Type 3: Pre-Round Timer Update Ping
        else if(strIncomingSplit[1].equals("3")){
            dblTimePerRemaining = Double.parseDouble(strIncomingSplit[3]);
        }

        //Message Type 4: Start of a Round
        else if(strIncomingSplit[1].equals("4")){
            intObjectLength = Integer.parseInt(strIncomingSplit[3]);
        }

        //Message Type 5: Drawing Telemetry
        else if(strIncomingSplit[1].equals("5")){
            intTempDraw[0] = Integer.parseInt(strIncomingSplit[3]); 
            intTempDraw[1] = Integer.parseInt(strIncomingSplit[4]); 
            intTempDraw[2] = Integer.parseInt(strIncomingSplit[5]); 
            intTempDraw[3] = Integer.parseInt(strIncomingSplit[6]); 
        }

        //Message Type 6: Round Timer Update Ping
        else if(strIncomingSplit[1].equals("6")){
            dblTimePerRemaining = Double.parseDouble(strIncomingSplit[3]);
        }

        //Message Type 7: Score Update Telemetry
        else if(strIncomingSplit[1].equals("7")){
            int intCount;
            String strDecode[] = new String[strIncomingSplit.length - 3];
            for(intCount = 0; intCount < strIncomingSplit.length - 3; intCount++){
                strDecode[intCount] = strIncomingSplit[intCount + 3];
            }
            
            strTempScores = strDecode;
        }

        //Message Type 8: End of a Round
        else if(strIncomingSplit[1].equals("8")){
            strObject = strIncomingSplit[3];
        }

        //Message Type 9: Post-Round Timer Update Ping
        else if(strIncomingSplit[1].equals("9")){
            dblTimePerRemaining = Double.parseDouble(strIncomingSplit[3]);
        }

        //Message Type 10: Leaderboard Ping
        else if(strIncomingSplit[1].equals("10")){
            int intCount;
            String strDecode[] = new String[strIncomingSplit.length - 3];
            for(intCount = 0; intCount < strIncomingSplit.length - 3; intCount++){
                strDecode[intCount] = strIncomingSplit[intCount + 3];
            }
            
            strTempScores = strDecode;
        }

        //Message Type 11: Chat Telemetry
        else if(strIncomingSplit[1].equals("11")){
            strObject = strIncomingSplit[3];
        }

        return Integer.parseInt(strIncomingSplit[1]);
    }

    //Retrieve Player List
    public String[] getPlayers(){
        return strPlayers;
    }

    //Retrieve Drawing Status
    public boolean isDrawing(){
        return blnDrawing;
    }

    //Retrieve Time Remaining Percentage
    public double getTimeRemPer(){
        return dblTimePerRemaining;
    }

    //Retrieve Player Scores
    public String[] getScores(){
        return strTempScores;
    }

    //Shared Methods
    //Select Object to Draw
    public void choseObject(int intButton){
        if(intButton == 1){
            strObject = strChoiceObjects[0];
        }
        else{
            strObject = strChoiceObjects[1];
        }
        if(blnHost){
            if(!isDrawing()){
                outTimeObjectPing();
            }
            startRound();
        }
        else{
            ClientSocket.sendText("0,2,"+ClientSocket.getMyAddress()+","+strObject);
        }
    }

    //Send New Drawing Data
    public void newDrawData(int intDrawData[]){
        if(blnHost){
            sendDrawData(intDrawData);
        }
        else{
            ClientSocket.sendText("0,3,"+ClientSocket.getMyAddress()+","+intDrawData[0]+","+intDrawData[1]+","+intDrawData[2]+","+intDrawData[3]);
        }
    }

    //Send Chat Message
    public boolean newMessage(String strMessage){
        if(!strMessage.equals("")){
            if(blnHost){
                strTempMessage = sendChatData(HostSocket.getMyAddress(), strMessage);
            }
            else{
                ClientSocket.sendText("0,1,"+ClientSocket.getMyAddress()+","+strMessage);
            }
        }
        return strMessage.equals("");
    }

    //Retrieve Item Drawing
    public String getObject(){
        return strObject;
    }

    //Retrieve Drawing Data
    public int[] getDrawingData(){
        return intTempDraw;
    }

    //Retrieve Message Data
    public String getMessageData(){
        return strTempMessage;
    }

    //Retrieve Object Length
    public int getObjectLength(){
        return intObjectLength;
    }

    //Retrieve Host Status
    public boolean isHost(){
        return blnHost;
    }

    //Constuctor
    public Model(View theView){
        this.theView = theView;
        pingTimer = new Timer(1000/60, theView);
        preRoundTimer = new SuperTimer(intPreRoundDuration, theView);
        roundTimer = new SuperTimer(intRoundDuration, theView);
        postRoundTimer = new SuperTimer(intPostRoundDuration, theView);
    }
}
