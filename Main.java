import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

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
    String strServerAddress;
    SuperSocketMaster HostSocket;
    SuperSocketMaster ClientSocket;

    //Colours
    Color clrBackground = new Color(37, 37, 37);
    Color clrLightGrey = new Color(217, 217, 217);
    Color clrWhite = new Color(255, 255, 255);

    //Fonts
    Font fntHelvetica30 = new Font("Helvetica", Font.BOLD, 30);
    Font fntHelvetica40 = new Font("Helvetica", Font.BOLD, 40);
    Font fntHelvetica100 = new Font("Helvetica", Font.BOLD, 100);

    //JComponents
    JFrame theFrame = new JFrame();

    JPanel theHomePanel = new JPanel();
    JLabel PictionaryLabel = new JLabel("PICTONARY");
    JLabel EnterNameLabel = new JLabel("Enter Name");
    JLabel StartGameLabel = new JLabel("Start Game");
    JTextField NameField = new JTextField();
    JButton HostGameButton = new JButton("Host Game");
    JButton JoinGameButton = new JButton("Join Game");


    //Methods
    public void actionPerformed(ActionEvent evt){
        

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
                //Receive the packet
                inSocket.receive(inPacket);

                //Extract and display the received message
                String receivedMessage = new String(inPacket.getData(), 0, inPacket.getLength());
                System.out.println(receivedMessage);
            }

            // Close the socket
            inSocket.close();
        }
        catch(Exception e){

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
            String message = HostSocket.getMyAddress();
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

        PictionaryLabel.setSize(640, 125);
        PictionaryLabel.setLocation(320, 35);
        PictionaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        PictionaryLabel.setFont(fntHelvetica100);
        PictionaryLabel.setForeground(clrWhite);

        EnterNameLabel.setSize(640, 50);
        EnterNameLabel.setLocation(320, 200);
        EnterNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        EnterNameLabel.setFont(fntHelvetica40);
        EnterNameLabel.setForeground(clrWhite);

        NameField.setSize(640, 50);
        NameField.setLocation(320, 265);
        NameField.setHorizontalAlignment(SwingConstants.CENTER);
        NameField.setFont(fntHelvetica30);
        NameField.setBackground(clrLightGrey);
        NameField.setBorder(null);

        StartGameLabel.setSize(640, 50);
        StartGameLabel.setLocation(320, 365);
        StartGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        StartGameLabel.setFont(fntHelvetica40);
        StartGameLabel.setForeground(clrWhite);

        HostGameButton.setSize(640, 50);
        HostGameButton.setLocation(320, 430);
        HostGameButton.setFont(fntHelvetica30);
        HostGameButton.setBackground(clrLightGrey);
        HostGameButton.setBorder(null);

        JoinGameButton.setSize(640, 50);
        JoinGameButton.setLocation(320, 500);
        JoinGameButton.setFont(fntHelvetica30);
        JoinGameButton.setBackground(clrLightGrey);
        JoinGameButton.setBorder(null);

        theHomePanel.add(PictionaryLabel);
        theHomePanel.add(EnterNameLabel);
        theHomePanel.add(NameField);
        theHomePanel.add(StartGameLabel);
        theHomePanel.add(HostGameButton);
        theHomePanel.add(JoinGameButton);

        theFrame.setContentPane(theHomePanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    //Main Method
    public static void main(String[] args) throws IOException{
        Main game = new Main();
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        if(keyboard.readLine().equals("server mode")){
            game.blnHost = true;
            game.HostSocket = new SuperSocketMaster(6000, game);
            game.broadcastIP();
        }
        else{
            game.blnHost = false;
            game.findServer();
            game.ClientSocket = new SuperSocketMaster(game.strServerAddress, 6000, game);
        }
    }
}