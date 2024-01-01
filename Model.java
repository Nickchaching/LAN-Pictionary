//NETWORKING:
//6000 - MAIN COMMUNICATIONS
    //General Format: DesignationID#, ActionID#, IP, Param1, Param2, Param3, Param4
        //DesignationID 0 = Intended for Server
        //DesignationID 1 = Intended for Clients
    
    //Server Intended Messages
        //Initial Connection: 0, 0, IP, ClientName
        //Terminating Connection: 0, -1, IP
        //Drawing Telemetry: 0, 1, IP, PosX, PosY, BrushSize, BrushColor
        //Chat Telemetry: 0, 2, IP, Message

    //Client Intended Messages
        //Terminating Connection: 1, -1, IP
        //Lobby Player Info Ping: 1, 0, IP, PlayerNames[]
        //Drawing Telemetry: 1, 1, IP, PosX, PosY, BrushSize, BrushColor
        //Chat Telemetry: 1, 2, IP, Message
        //Score Telemetry: 1, 3, IP, PlayerScores[]
        //Timer Telemetry: 1, 4, IP, TimeRemaining
        //Object Telemetry: 1, 5, IP, ObjecttoDraw
        //Game Telemetry: 1, 6, IP, RoundsPlayed

//6001 - INITIAL SERVER CONNECTION
    //Server Ping: IP, ServerName, PlayerCount

//SERVER STORAGE:
//Client Details: IP, Name

import java.io.*;

public class Model{
    //Properties
    View theView;

    //Shared Properties
    boolean blnConnected = false;
    boolean blnHost;
    boolean blnGameStarted = false;
    String strUsername;
    String strIncomingSplit[];
    String strTheme;
    
    //Server Properties
    SuperSocketMaster HostSocket;
    int intPlayers = 0;
    Thread broadcastIP = new Thread(new broadcastIP(this));
    String strPlayerList[][];
    String strPlayerTemp[][];
    String strThemes[] = new String[8];

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

    //Server Message Handling
    public int serverMessageRecieved(){
        strIncomingSplit = HostSocket.readText().split(",");
        
        //Message Type 0: Initial Connection
        if(strIncomingSplit[1].equals("0")){
            System.out.println("The server has accepted a new client, "+strIncomingSplit[3]+", at "+strIncomingSplit[2]);
            if(strPlayerList == null){
                strPlayerList = new String[1][2];
                strPlayerList[0][0] = strIncomingSplit[2];
                strPlayerList[0][1] = strIncomingSplit[3];
            }
            else{
                int intCount;
                strPlayerTemp = strPlayerList;
                strPlayerList = new String[strPlayerTemp.length + 1][2];
                for(intCount = 0; intCount < strPlayerTemp.length; intCount++){
                    strPlayerList[intCount] = strPlayerTemp[intCount];
                }
                strPlayerList[strPlayerTemp.length][0] = strIncomingSplit[2];
                strPlayerList[strPlayerTemp.length][1] = strIncomingSplit[3];
            }
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
    public void sendPing(int intType){
        if(intType == 0 && strPlayerList != null){
            int intCount;
            String strEncode1[] = new String[strPlayerList.length];
            for(intCount = 0; intCount < strPlayerList.length; intCount++){
                strEncode1[intCount] = strPlayerList[intCount][1];
            }
            HostSocket.sendText("1,0,"+HostSocket.getMyAddress()+","+String.join(",", strEncode1));
        }
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
    }
}
