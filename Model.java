//Pictionary
//Programmers: Nicholas Ching / Ryan Song / Erin Hu
//Project: ICS4U CPT

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
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Pictionary Model
 * This class deals with the data to play pictionary<br>
 * It loads data from word list files into arrays<br>
 * It has methods to control server messaging and score data  
 * <p>
 * 
 * @author Nicholas Ching, Ryan Song, Erin Hu
 * @version 1.0.0
 * @since 2024-01-21
 */

public class Model{
    //Properties
    /** Provides access to methods for UX graphics */
    View theView;

    //Game Settings
    /** Time duration when drawer is choosing object to draw */
    int intPreRoundDuration = 15000;
    /** Time duration when drawer is drawing and other players are guessing */
    int intRoundDuration = 90000;
    /** Time duration to display correct word after drawing */
    int intPostRoundDuration = 5000;
    /** Total number of drawing turns */
    int intRounds = 5;
    /** Number of points for guessing corectly */
    int intAnsScore = 50;

    //Shared Properties
    /** Indicates whether host or client is connected*/
    boolean blnConnected = false;
    /** Indicates whether user is host or client */
    boolean blnHost;
    /** Indicates whether game has started */
    boolean blnGameStarted = false;
    /** Player's username */
    String strUsername;
    /** Message received*/
    String strIncomingSplit[];
    /** Chosen theme for drawing */
    String strTheme;
    /** Round number */
    int intRound;
    /** Indicates whether player is the drawer*/
    boolean blnDrawing;
    /** Options for objects that the drawer will draw*/
    String strChoiceObjects[];
    /** Object that the drawer chooses to draw */
    String strObject;
    /** Length of object name*/
    int intObjectLength;
    /** Stores drawing data temporarily for view to retrieve*/
    int intTempDraw[] = new int[4];
    /** Stores incoming chat message data temporarily for view to retrieve*/
    String strTempMessage;
    
    //Server Properties
    /** Socket for host*/
    SuperSocketMaster HostSocket;
    /** Thread for broadcasting IP*/
    Thread broadcastIP = new Thread(new broadcastIP(this));
    /** List of players */
    String strPlayerList[][];
    /** Temporarily stores the list of players for it to be resorted */
    String strPlayerTemp[][];
    /** Stores the 8 themes from themes text file */
    String strThemes[] = new String[8];
    /** Stores objects from the selected theme file */
    String strObjects[];
    /** Stores the IP address of the drawer */
    String strDrawer;
    /** Timer used to ping data to clients periodically */
    Timer pingTimer;
    /** Timer used for object selection containing a special method that allows you to retrieve time remaining*/
    SuperTimer preRoundTimer;
    /** Timer used for drawer to draw object */
    SuperTimer roundTimer;
    /** Timer used to display the correct object after drawing */
    SuperTimer postRoundTimer;

    //Client Only Properties
    /** Socket for client */
    SuperSocketMaster ClientSocket;
    /** IP address of server */
    String strServerAddress;
    /** Stores list of available servers to join*/
    String[][] strServerList = new String[5][3];
    /** Thread for detecting servers */
    Thread findServer = new Thread(new findServer(this));
    /** List of player names*/
    String strPlayers[];
    /** % of time remaining for player to guess or draw*/
    double dblTimePerRemaining;
    /** Stores scores temporarily*/
    String strTempScores[];

    //Server Methods
    /** Set up and connect socket after player enters name 
      * @param strNameField name inputted by user in text field
      * @return a boolean of whether the host socket is connected and broadcasting the IP
      */
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
    /** Gets the IP address, username, and length of player list
      * @return a String with the IP address, username, and length splitted by commas
      */
    public String getStatus(){
        return HostSocket.getMyAddress()+","+strUsername+","+strPlayerList.length;
    }

    //Retrieve Player List
    /** Gets the player list which includes IP address, username, and score
      * @return a 2D String array with IP address, username, and score 
      */
    public String[][] getPlayerList(){
        return strPlayerList;
    }

    //Load and Retrieve Themes
    /** Gets a theme by reading the theme file 
      * @return a String array with the list of themes
      */
    public String[] getThemes(){
        int intCount;

        InputStream fileRead = this.getClass().getResourceAsStream("Assets/themes.txt");
        if(fileRead != null){
            try{
                BufferedReader themeFile = new BufferedReader(new InputStreamReader(fileRead));
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
                System.out.println("Theme Loading Failed");
            }
        }
        else{
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
                System.out.println("Theme Loading Failed");
            }
        }
        return strThemes;
        /*
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
        */
    }

    //Theme Selection Handling
    /** Determines whether a theme has been selected
      * @param intTheme integer indicates which theme has been chosen  
      * @return a boolean indicating whether the user has chosen a theme
      */
    public boolean selectTheme(int intTheme){
        if(strThemes[intTheme - 1] != strTheme){
            strTheme = strThemes[intTheme - 1];
            HostSocket.sendText("1,12,"+HostSocket.getMyAddress()+","+strTheme);
            return true;
        }
        else{
            return false;
        }
    }

    //Loading Theme Objects
    /** Load objects of a theme from a text file
      * @return a boolean of whether objects have been loaded properly
      */
    public boolean loadObjects(){
        int intCount;
        int intObjects = 0;

        InputStream fileRead = this.getClass().getResourceAsStream("Assets/"+strTheme);
        if(fileRead != null){
            try{
                BufferedReader objectFile = new BufferedReader(new InputStreamReader(fileRead));
                while(objectFile.readLine() != null){
                    intObjects++;
                }
                objectFile.close();
                fileRead = this.getClass().getResourceAsStream("Assets/"+strTheme);
                objectFile = new BufferedReader(new InputStreamReader(fileRead));
                strObjects = new String[intObjects];
                for(intCount = 0; intCount < intObjects; intCount++){
                    strObjects[intCount] = objectFile.readLine();
                }
                return true;
            }
            catch(FileNotFoundException e){
                System.out.println("Unfortunately, the themes file has not been found");
                return false;
            }
            catch(IOException e){
                System.out.println("Theme Loading Failed");
                return false;
            }
        }
        else{
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
                System.out.println("Unfortunately, the themes file has not been found");
                return false;
            }
            catch(IOException e){
                System.out.println("Theme Loading Failed");
                return false;
            }
        }

        /* 
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
        */
    }

    //Get Random Drawer
    /** Randomly choose a player to draw
      * @return a String of a random player's IP address from the player list
      */
    public String getRandDrawer(){
        return strPlayerList[(int)(Math.random() * strPlayerList.length)][0];
    }

    //Get Random Objects
    /** Randomly choose two different objects from the object list<br>
      * Drawer will have two options to choose from  
      * @return a String array with two objects 
      */
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
    /** Starts game if game is able to load objects from the text file<br>
      * Sets the round number to 1
      * @return a boolean that indicates whether the objects could be loaded
      */
    public boolean startGame(){
        blnGameStarted = loadObjects();
        if(blnGameStarted){
            intRound = 1;
            pingTimer = new Timer(1000/60, theView);
            pingTimer.start();
        }
        return blnGameStarted;
    }

    //New Round
    /** Choose random drawer and random objects<br>
      * Host sends message to clients<br>
      * Start timer for the drawer to choose an object
      * @return a boolean that indicates whether the host is the drawer
      */
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
    /** Allows drawer to start drawing<br>
      * Displays hint for the guessers<br>
      * Sets player points to 0
      */
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
    /** Finishes the drawer's drawing turn<br>
      * Gives drawer points based on % of correct guesses
      */
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

    /** Reset current drawing round for the next drawing round
     * @return a boolean that indicates whether there will be a new drawing round
     */
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
    /** Host sends drawing data to clients
     * @param intDrawData integer array for x position, y position, brush size, and brush colour
     */
    public void sendDrawData(int intDrawData[]){
        HostSocket.sendText("1,5,"+HostSocket.getMyAddress()+","+intDrawData[0]+","+intDrawData[1]+","+intDrawData[2]+","+intDrawData[3]);
    }

    //(RE)-Broadcasts Chat Data to Clients
    /** Host sends messages for drawing guesses and chat
      * @param strIP IP address of the host
      * @param strChatData a guess or a message sent in the chat
      * @return a String formatted according to chat telemetry
      */
    public String sendChatData(String strIP, String strChatData){
        int intCount;
        String strFormattedChatData = "";
        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            if(strPlayerList[intCount][0].equals(strIP)){
                strFormattedChatData = strPlayerList[intCount][1];
                break;
            }
        }

        if(strChatData.toLowerCase().contains(strObject.toLowerCase()) && strPlayerList[intCount][3].equals("0") && !strPlayerList[intCount][0].equals(strDrawer)){
            strFormattedChatData = strFormattedChatData + " got the right answer";

            //Score Increment
            strPlayerList[intCount][2] = "" + (Integer.parseInt(strPlayerList[intCount][2]) + intAnsScore);
            strPlayerList[intCount][3] = "1";
        }
        else if(strChatData.toLowerCase().contains(strObject.toLowerCase())){
            strFormattedChatData = strFormattedChatData + " is trying to ruin the fun";
        }
        else{
            strFormattedChatData = strFormattedChatData + ": " + strChatData;
        }

        HostSocket.sendText("1,1,"+HostSocket.getMyAddress()+","+strFormattedChatData);
        return strFormattedChatData;
    }

    //Get Object Choices
    /** Gets the choices of objects to draw
     * @return a String array with objects to draw
     */
    public String[] getObjectChoices(){
        return strChoiceObjects;
    }

    //Get Round Info
    /** Gets the round number
      * @return an integer of the round number
      */
    public int getRound(){
        return intRound;
    }

    //Server Message Handling
    /** Manipulates game data depending on the type of message<br>
      * Message types are used for initial connection, chat, object choice, and drawing
      * @return an integer for the type of message received
      */
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
    /** Sends server data pings regularly
     * @param intType type of message
     * @return String for the percentage of remaining time if applicable
     */
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
    /** Provides updated scores after bubble sorting and formatting
     * @return a String array with the new score data 
     */
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

    //Check Answer Status
    /** Checks if the round is complete<br>
      * Checks number of players who have answered<br>
      * If everyone other than the drawer has answered, round is complete
      * @return boolean to indicate if the round is over
      */
    public boolean roundisComplete(){
        int intCount;
        int intCounter = 0;
        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            if(strPlayerList[intCount][3].equals("1")){
                intCounter++;
            }
        }

        return (intCounter == (strPlayerList.length - 1));
    }

    //Game End Update Ping
    /** Sorts and formats scores when game is over
      * @return String array with sorted and formatted scores for each player 
      */
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
    /** Checks if score is updated
      * @return a boolean to indicate if the score was updated
      */
    public boolean checkScoreUpdated(){
        if(strTempMessage.split(":").length == 1){
            return true;
        }
        else{
            return false;
        }
    }

    //Out of Time Send Object
    /** Sends the object if there is no more time for the round */
    public void outTimeObjectPing(){
        HostSocket.sendText("1,11,"+HostSocket.getMyAddress()+","+strObject);
    }

    //Client Methods
    //Initial Client Connection
    /** Sets up client connection and looks for broadcasted IP from server
      * @param strNameField
      * @return boolean to indicate if user entered a name in the name text field
      */
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
    /** Sets up and connects client socket
      * @param intButton button pressed when the user chooses which server to join
      * @return boolean to indicate whether the client is connected
      */
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
    /** Update list of servers for the player (client) to join
     * @param strServerLoad contains the username, IP, player data of servers found
     */
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
    /** Manipulates game data depending on the type of message<br>
      * Message types are for lobby, chat, starting rounds, timers, and scores
      * @return integer for type of message received
      */
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

        //Message Type 11: Object Selection Out of Time
        else if(strIncomingSplit[1].equals("11")){
            strObject = strIncomingSplit[3];
        }

        //Message Type 12: Theme Image Loading
        else if(strIncomingSplit[1].equals("12")){
            strTheme = strIncomingSplit[3];
        }

        return Integer.parseInt(strIncomingSplit[1]);
    }

    //Retrieve Player List
    /** Get list of players
      * @return String array for player list
      */
    public String[] getPlayers(){
        return strPlayers;
    }

    //Retrieve Drawing Status
    /** Get drawing status
     * @return boolean to indicate whether the user is drawing
     */
    public boolean isDrawing(){
        return blnDrawing;
    }

    //Retrieve Time Remaining Percentage
    /** Get the percentage of time remaining
      * @return double value of the percentage of time remaining
      */
    public double getTimeRemPer(){
        return dblTimePerRemaining;
    }

    //Retrieve Player Scores
    /** Get player scores
      * @return String array of player's scores
      */
    public String[] getScores(){
        return strTempScores;
    }

    /** Get chosen theme
      * @return String for the chosen theme
      */
    public String getTheme(){
        return strTheme;
    }

    //Shared Methods
    //Select Object to Draw
    /** Allows drawer to choose between two objects to draw
      * @param intButton button that the drawer selects
      */
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
    /** Send new drawing data
      * @param intDrawData drawing data for x position, y position, brush size, and brush colour
     */
    public void newDrawData(int intDrawData[]){
        if(blnHost){
            sendDrawData(intDrawData);
        }
        else{
            ClientSocket.sendText("0,3,"+ClientSocket.getMyAddress()+","+intDrawData[0]+","+intDrawData[1]+","+intDrawData[2]+","+intDrawData[3]);
        }
    }

    //Send Chat Message
    /** Send a chat message
      * @param strMessage text entered by the user in a text field
      * @return boolean to indicate whether the text field is blank
      */
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

    //Demo Screen
    /** Detects whether the user entered their username<br>
      * If a username is entered, this will allow them to access the demo
      * @param strNameField username entered by the user
      * @return boolean to indicate whether the user entered a username
      */
    public boolean initializeDemo(String strNameField){
        if(!strNameField.equals("")){
            strUsername = strNameField;
            return true;
        }
        else{
            return false;
        }
    }

    //Retrieve Item Drawing
    /** Gets the object that the drawer chose to draw
      * @return String of the object that the drawer chose to draw
      */
    public String getObject(){
        return strObject;
    }

    //Retrieve Drawing Data
    /** Gets drawing data
      * @return integer array for x position, y position, brush size, and brush colour
      */
    public int[] getDrawingData(){
        return intTempDraw;
    }

    //Retrieve Message Data
    /** Gets incoming message data
      * @return String with the incoming message data
      */
    public String getMessageData(){
        return strTempMessage;
    }

    //Retrieve Object Length
    /** Gets the number of characters in the object
      * @return integer with the length of the object
      */
    public int getObjectLength(){
        return intObjectLength;
    }

    //Retrieve Host Status
    /** Gets the host status
      * @return boolean to indicate whether the player is the host
      */
    public boolean isHost(){
        return blnHost;
    }

    //Constuctor
    /** 
     * Pictionary Model Constructor
     * 
     * @param theView class which involves the user interface and graphics
     */
    public Model(View theView){
        this.theView = theView;
        pingTimer = new Timer(1000, theView);
        preRoundTimer = new SuperTimer(intPreRoundDuration, theView);
        roundTimer = new SuperTimer(intRoundDuration, theView);
        postRoundTimer = new SuperTimer(intPostRoundDuration, theView);
    }
}
