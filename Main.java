import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

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

public class Main implements ActionListener{
    //Properties
    static boolean blnConnected = false;
    boolean blnHost;
    static String strUsername;
    String strIncomingSplit[];
    
    static SuperSocketMaster HostSocket;
    static int intPlayers = 0;
    Thread broadcastIP = new Thread(new broadcastIP());

    SuperSocketMaster ClientSocket;
    String strServerAddress;
    static String[][] strServerList = new String[5][3];
    Thread findServer = new Thread(new findServer());

    //Colours
    Color clrBackground = new Color(37, 37, 37);
    Color clrLightGrey = new Color(217, 217, 217);
    Color clrWhite = new Color(255, 255, 255);

    //Fonts
    Font fntHelvetica30 = new Font("Helvetica", Font.BOLD, 30);
    Font fntHelvetica40 = new Font("Helvetica", Font.BOLD, 40);
    Font fntHelvetica75 = new Font("Helvetica", Font.BOLD, 75);
    Font fntHelvetica125 = new Font("Helvetica", Font.BOLD, 125);

    //JComponents
    JFrame theFrame = new JFrame();

    //Home Panel
    JPanel theHomePanel = new JPanel();
    JLabel PictionaryLabel = new JLabel("PICTONARY");
    JLabel EnterNameLabel = new JLabel("Enter Name");
    JLabel StartGameLabel = new JLabel("Start Game");
    JTextField NameField = new JTextField();
    JButton HostGameButton = new JButton("Host Game");
    JButton JoinGameButton = new JButton("Join Game");

    //Client Server Selection Panel
    JPanel theServerSelectionPanel = new JPanel();
    JLabel AvailableServersLabel = new JLabel("Available Servers");
    static JButton Server1Button = new JButton();
    static JButton Server2Button = new JButton();
    static JButton Server3Button = new JButton();
    static JButton Server4Button = new JButton();
    static JButton Server5Button = new JButton();

    //Methods
    public void actionPerformed(ActionEvent evt){
        //Initial Host Connection
        if(evt.getSource() == HostGameButton){
            if(!NameField.getText().equals("")){
                strUsername = NameField.getText();
                blnHost = true;
                HostSocket = new SuperSocketMaster(6000, this);
                HostSocket.connect();
                broadcastIP.start();
            }
            else{
                NameField.setText("Please Enter a Name");
            }
        }
        //Initial Client Connection
        else if(evt.getSource() == JoinGameButton){
            if(!NameField.getText().equals("")){
                strUsername = NameField.getText();
                blnHost = false;
                theFrame.setContentPane(theServerSelectionPanel);
                theFrame.pack();
                findServer.start();
            }
            else{
                NameField.setText("Please Enter a Name");
            }
        }
        //Client Server Selection
        else if(evt.getSource() == Server1Button || evt.getSource() == Server2Button || evt.getSource() == Server3Button || evt.getSource() == Server4Button || evt.getSource() == Server5Button){
            if(evt.getSource() == Server1Button){
                strServerAddress = strServerList[0][0];
            }
            else if(evt.getSource() == Server2Button){
                strServerAddress = strServerList[1][0];
            }
            else if(evt.getSource() == Server3Button){
                strServerAddress = strServerList[2][0];
            }
            else if(evt.getSource() == Server4Button){
                strServerAddress = strServerList[3][0];
            }
            else if(evt.getSource() == Server5Button){
                strServerAddress = strServerList[4][0];
            }
            System.out.println(strServerAddress);
            ClientSocket = new SuperSocketMaster(strServerAddress, 6000, this);
            blnConnected = ClientSocket.connect();
            if(blnConnected){
                ClientSocket.sendText("0,0,"+ClientSocket.getMyAddress()+","+strUsername);
                //Swap to Lobby Panel
            }
            else{
                //Show user an "error label" and remove the server as an option and disconnect the socket
            }
        }
        //Server Message Handling
        else if(evt.getSource() == HostSocket && HostSocket.readText().substring(0, 1).equals("0")){
            strIncomingSplit = HostSocket.readText().split(",");
            
            //Message Type 0: Initial Connection
            if(strIncomingSplit[1].equals("0")){
                System.out.println("The server has accepted a new client, "+strIncomingSplit[3]+", at "+strIncomingSplit[2]);
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

            //Split the server message by commas
            //Check index 1 for message type
            //Within if-statements, trigger appropriate actions
        }
    }

    //Constructor
    public Main(){
        //Home Panel
        theHomePanel.setPreferredSize(new Dimension(1280, 720));
        theHomePanel.setLayout(null);
        theHomePanel.setBackground(clrBackground);

        PictionaryLabel.setSize(1280, 150);
        PictionaryLabel.setLocation(0, 35);
        PictionaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        PictionaryLabel.setFont(fntHelvetica125);
        PictionaryLabel.setForeground(clrWhite);

        EnterNameLabel.setSize(640, 50);
        EnterNameLabel.setLocation(320, 250);
        EnterNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        EnterNameLabel.setFont(fntHelvetica40);
        EnterNameLabel.setForeground(clrWhite);

        NameField.setSize(640, 50);
        NameField.setLocation(320, 315);
        NameField.setHorizontalAlignment(SwingConstants.CENTER);
        NameField.setFont(fntHelvetica30);
        NameField.setBackground(clrLightGrey);
        NameField.setBorder(null);

        StartGameLabel.setSize(640, 50);
        StartGameLabel.setLocation(320, 415);
        StartGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        StartGameLabel.setFont(fntHelvetica40);
        StartGameLabel.setForeground(clrWhite);

        HostGameButton.setSize(640, 50);
        HostGameButton.setLocation(320, 480);
        HostGameButton.setFont(fntHelvetica30);
        HostGameButton.setBackground(clrLightGrey);
        HostGameButton.setBorder(null);

        JoinGameButton.setSize(640, 50);
        JoinGameButton.setLocation(320, 550);
        JoinGameButton.setFont(fntHelvetica30);
        JoinGameButton.setBackground(clrLightGrey);
        JoinGameButton.setBorder(null);

        theHomePanel.add(PictionaryLabel);
        theHomePanel.add(EnterNameLabel);
        theHomePanel.add(NameField);
        theHomePanel.add(StartGameLabel);
        theHomePanel.add(HostGameButton);
        theHomePanel.add(JoinGameButton);

        HostGameButton.addActionListener(this);
        JoinGameButton.addActionListener(this);

        //Server Selection Panel
        theServerSelectionPanel.setPreferredSize(new Dimension(1280, 720));
        theServerSelectionPanel.setLayout(null);
        theServerSelectionPanel.setBackground(clrBackground);
        
        AvailableServersLabel.setSize(640, 100);
        AvailableServersLabel.setLocation(320, 25);
        AvailableServersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        AvailableServersLabel.setFont(fntHelvetica75);
        AvailableServersLabel.setForeground(clrWhite);

        Server1Button.setSize(1000, 50);
        Server1Button.setLocation(140, 175);
        Server1Button.setFont(fntHelvetica30);
        Server1Button.setBackground(clrLightGrey);
        Server1Button.setBorder(null);

        Server2Button.setSize(1000, 50);
        Server2Button.setLocation(140, 275);
        Server2Button.setFont(fntHelvetica30);
        Server2Button.setBackground(clrLightGrey);
        Server2Button.setBorder(null);

        Server3Button.setSize(1000, 50);
        Server3Button.setLocation(140, 375);
        Server3Button.setFont(fntHelvetica30);
        Server3Button.setBackground(clrLightGrey);
        Server3Button.setBorder(null);

        Server4Button.setSize(1000, 50);
        Server4Button.setLocation(140, 475);
        Server4Button.setFont(fntHelvetica30);
        Server4Button.setBackground(clrLightGrey);
        Server4Button.setBorder(null);

        Server5Button.setSize(1000, 50);
        Server5Button.setLocation(140, 575);
        Server5Button.setFont(fntHelvetica30);
        Server5Button.setBackground(clrLightGrey);
        Server5Button.setBorder(null);

        Server1Button.setVisible(false);
        Server2Button.setVisible(false);
        Server3Button.setVisible(false);
        Server4Button.setVisible(false);
        Server5Button.setVisible(false);

        theServerSelectionPanel.add(AvailableServersLabel);
        theServerSelectionPanel.add(Server1Button);
        theServerSelectionPanel.add(Server2Button);
        theServerSelectionPanel.add(Server3Button);
        theServerSelectionPanel.add(Server4Button);
        theServerSelectionPanel.add(Server5Button);

        Server1Button.addActionListener(this);
        Server2Button.addActionListener(this);
        Server3Button.addActionListener(this);
        Server4Button.addActionListener(this);
        Server5Button.addActionListener(this);


        theFrame.setContentPane(theHomePanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.pack();
        theFrame.setVisible(true);

        test testing = new test(this);
    }

    //Main Method
    public static void main(String[] args) throws IOException{
        new Main();
    }
}