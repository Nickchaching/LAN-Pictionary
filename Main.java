import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.*;
import javax.swing.event.*;

//NETWORKING:
//6000 - MAIN COMMUNICATIONS
//6001 - INITIAL SERVER CONNECTION

public class Main implements ActionListener{
    //Properties
    boolean blnConnected = false;
    boolean blnHost;
    String strUsername;
    
    SuperSocketMaster HostSocket;
    int intPlayers = 0;

    SuperSocketMaster ClientSocket;
    String strServerAddress;
    String[] strServerLoad;
    String[][] strServerList = new String[5][3];

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
    JButton Server1Button = new JButton();


    //Methods
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == HostGameButton){
            if(!NameField.getText().equals("")){
                strUsername = NameField.getText();
                blnHost = true;
                HostSocket = new SuperSocketMaster(6000, this);
                broadcastIP();
            }
            else{
                NameField.setText("Please Enter a Name");
            }
        }
        if(evt.getSource() == JoinGameButton){
            if(!NameField.getText().equals("")){
                strUsername = NameField.getText();
                blnHost = false;
                findServer();
                ClientSocket = new SuperSocketMaster(strServerAddress, 6000, this);
            }
            else{
                NameField.setText("Please Enter a Name");
            }
        }
    }
    
    //Client Only Methods
    public void findServer(){
        try{
            // Create a DatagramSocket to listen on a specific port
            DatagramSocket inSocket = new DatagramSocket(6001);

            // Buffer to store incoming data
            byte[] buffer = new byte[1024];

            // Create a DatagramPacket to receive the incoming data
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
            
            while(!blnConnected){
                int intCount = 0;
                
                //Receive the packet
                inSocket.receive(inPacket);

                //Extract and display the received message
                String receivedMessage = new String(inPacket.getData(), 0, inPacket.getLength());
                strServerLoad = receivedMessage.split(",");

                //Check to see if IP Exists and Slots Filled
                for(intCount = 0; intCount < 5; intCount++){
                    if(strServerList[intCount][0] == null){
                        strServerList[intCount] = strServerLoad;
                        intCount = 5;
                    }
                    else if(strServerList[intCount][0].equals(strServerLoad[0])){
                        intCount = 5;
                    }
                    else{
                        intCount++;
                    }
                }

                strServerList[0] = strServerLoad;

                System.out.println("Server Name: "+strServerList[0][0]+" | Server IP: "+strServerList[0][1]+" | Server Players: "+strServerList[0][2]);
                System.out.println("Server Name: "+strServerList[1][0]+" | Server IP: "+strServerList[1][1]+" | Server Players: "+strServerList[1][2]);
                System.out.println("Server Name: "+strServerList[2][0]+" | Server IP: "+strServerList[2][1]+" | Server Players: "+strServerList[2][2]);
                System.out.println("Server Name: "+strServerList[3][0]+" | Server IP: "+strServerList[3][1]+" | Server Players: "+strServerList[3][2]);
                System.out.println("Server Name: "+strServerList[4][0]+" | Server IP: "+strServerList[4][1]+" | Server Players: "+strServerList[4][2]);
           
            }

            // Close the socket
            inSocket.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Server Only Methods
    public void broadcastIP(){
        try{
            //Create a DatagramSocket
            DatagramSocket outSocket = new DatagramSocket();
            
            //Set the broadcast address
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

            //Message to be sent
            String message = HostSocket.getMyAddress()+","+strUsername+","+intPlayers;
            byte[] data = message.getBytes();

            //Create a DatagramPacket with the message, length, and broadcast address
            DatagramPacket outPacket = new DatagramPacket(data, data.length, broadcastAddress, 6001);

            //Send the packet
            while(!blnConnected){
                outSocket.send(outPacket);
                System.out.println("Broadcasted IP");
                try{
                    Thread.sleep(5000);
                }
                catch(InterruptedException e){

                }
            }

            //Close the socket
            outSocket.close();
        }
        catch(Exception e){

        }
    }

    //Constructor
    public Main(){
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