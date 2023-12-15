import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

//NETWORKING:
//6000 - MAIN COMMUNICATIONS
//6001 - INITIAL SERVER CONNECTION

public class Main implements ActionListener{
    //Properties
    static boolean blnConnected = false;
    boolean blnHost;
    static String strUsername;
    
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
    Font fntHelvetica125 = new Font("Helvetica", Font.BOLD, 125);

    //JComponents
    JFrame theFrame = new JFrame();

    JPanel theHomePanel = new JPanel();
    JLabel PictionaryLabel = new JLabel("PICTONARY");
    JLabel EnterNameLabel = new JLabel("Enter Name");
    JLabel StartGameLabel = new JLabel("Start Game");
    JTextField NameField = new JTextField();
    JButton HostGameButton = new JButton("Host Game");
    JButton JoinGameButton = new JButton("Join Game");

    JPanel theServerSelectionPanel = new JPanel();
    static JButton Server1Button = new JButton();
    static JButton Server2Button = new JButton();
    static JButton Server3Button = new JButton();
    static JButton Server4Button = new JButton();
    static JButton Server5Button = new JButton();


    //Methods
    public void actionPerformed(ActionEvent evt){
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
        else if(evt.getSource() == JoinGameButton){
            if(!NameField.getText().equals("")){
                strUsername = NameField.getText();
                blnHost = false;
                theFrame.setVisible(false);
                theFrame.setContentPane(theServerSelectionPanel);
                theFrame.pack();
                theFrame.setVisible(true);
                findServer.start();
            }
            else{
                NameField.setText("Please Enter a Name");
            }
        }
        else if(evt.getSource() == Server1Button || evt.getSource() == Server2Button || evt.getSource() == Server3Button || evt.getSource() == Server4Button || evt.getSource() == Server5Button){
            blnConnected = true;
            if(evt.getSource() == Server1Button){
                strServerAddress = strServerList[0][0];
            }
            System.out.println(strServerAddress);
            ClientSocket = new SuperSocketMaster(strServerAddress, 6000, this);
            ClientSocket.connect();
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

        Server1Button.setSize(640, 50);
        Server1Button.setLocation(320, 200);
        Server1Button.setFont(fntHelvetica30);
        Server1Button.setBackground(clrLightGrey);
        Server1Button.setBorder(null);

        Server2Button.setSize(640, 50);
        Server2Button.setLocation(320, 300);
        Server2Button.setFont(fntHelvetica30);
        Server2Button.setBackground(clrLightGrey);
        Server2Button.setBorder(null);

        Server3Button.setSize(640, 50);
        Server3Button.setLocation(320, 400);
        Server3Button.setFont(fntHelvetica30);
        Server3Button.setBackground(clrLightGrey);
        Server3Button.setBorder(null);

        Server4Button.setSize(640, 50);
        Server4Button.setLocation(320, 500);
        Server4Button.setFont(fntHelvetica30);
        Server4Button.setBackground(clrLightGrey);
        Server4Button.setBorder(null);

        Server5Button.setSize(640, 50);
        Server5Button.setLocation(320, 600);
        Server5Button.setFont(fntHelvetica30);
        Server5Button.setBackground(clrLightGrey);
        Server5Button.setBorder(null);

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
    }

    //Main Method
    public static void main(String[] args) throws IOException{
        new Main();
    }
}