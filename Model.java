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
        //Drawing Telemetry: 0, 2, IP, PosX, PosY, BrushSize, BrushColor

    //Client Intended Messages
        //Terminating Connection: 1, -1, IP
        //Lobby Player Info Ping: 1, 0, IP, PlayerNames[]
        //Chat Telemetry: 1, 1, IP, FormattedMessage (with player name)
        //Round Intitalization1 (Choosing): 1, 2, IP, RoundsPlayed, DrawerIP, ObjectstoChoose[]
        //Round Initializaton Telemetry: 1, 3, IP, TimeRemaining
        //Round Initialization2 (Done Choosing): 1, 4, IP, Object.charlength
        //Drawing Telemetry: 1, 5, IP, PosX, PosY, BrushSize, BrushColor
        //Game Telemetry: 1, 6, IP, TimeRemaining, PlayerScores[][]
        //Round Completion: 1, 7, IP, Object
        //Round Initializaton Telemetry: 1, 8, IP, TimeRemaining

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

    //Shared Properties
    boolean blnConnected = false;
    boolean blnHost;
    boolean blnGameStarted = false;
    String strUsername;
    String strIncomingSplit[];
    String strTheme;
    int intRound;
    boolean blnDrawing;
    
    //Server Properties
    SuperSocketMaster HostSocket;
    int intPlayers = 0;
    Thread broadcastIP = new Thread(new broadcastIP(this));
    String strPlayerList[][];
    String strPlayerTemp[][];
    String strThemes[] = new String[8];
    String strObjects[];
    String strChoiceObjects[];
    String strDrawer;
    String strObject;
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

    //Server Methods
    //Initial Host Connection
    public boolean initializeHost(String strNameField){
        if(!strNameField.equals("")){
            strUsername = strNameField;
            blnHost = true;
            HostSocket = new SuperSocketMaster(6000, theView, this);
            blnConnected = HostSocket.connect();
            if(blnConnected){
                broadcastIP.start();
                pingTimer.start();
                strPlayerList = new String[1][4];
                strPlayerList[0][0] = HostSocket.getMyAddress();
                strPlayerList[0][1] = strUsername;
            }
            return blnConnected;
        }
        else{
            return false;
        }
    }

    //BroadcastIP Message Recall
    public String getStatus(){
        return HostSocket.getMyAddress()+","+strUsername+","+intPlayers;
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
            return true;
        }
        else{
            return false;
        }

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
            strPlayerList = new String[strPlayerTemp.length + 1][2];
            for(intCount = 0; intCount < strPlayerTemp.length; intCount++){
                strPlayerList[intCount] = strPlayerTemp[intCount];
            }
            strPlayerList[strPlayerTemp.length][0] = strIncomingSplit[2];
            strPlayerList[strPlayerTemp.length][1] = strIncomingSplit[3];
        }
        //Message Type 1: Drawing Telemetry
        else if(strIncomingSplit[1].equals("1")){

        }
        //Message Type 2: Chat Telemetry
        else if(strIncomingSplit[1].equals("2")){

        }
        //Message Type -1: Terminating Connection
        else if(strIncomingSplit[1].equals("-1")){

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
            HostSocket.sendText("1,3,"+HostSocket.getMyAddress()+preRoundTimer.getRemainingTime());
            return (int)(preRoundTimer.getRemainingTime()*100/intPreRoundDuration)+"";
        }
        
        //Defualt Return
        return null;
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

        if(strIncomingSplit[1].equals("0")){
            int intLength = strIncomingSplit.length - 3;
            int intCount;
            String strDecode[] = new String[intLength];
            for(intCount = 0; intCount < intLength; intCount++){
                strDecode[intCount] = strIncomingSplit[intCount + 3];
            }
            strPlayers = strDecode;
        }

        return Integer.parseInt(strIncomingSplit[1]);
    }

    //Retrieve Player List
    public String[] getPlayers(){
        return strPlayers;
    }


    //Constuctor
    public Model(View theView){
        this.theView = theView;
        pingTimer = new Timer(1000, theView);
        preRoundTimer = new SuperTimer(intPreRoundDuration, theView);
        roundTimer = new SuperTimer(intRoundDuration, theView);
        postRoundTimer = new SuperTimer(intPostRoundDuration, theView);
    }
}
