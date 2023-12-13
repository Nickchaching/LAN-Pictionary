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
//6001 - SERVER CALLS
//6002 - SERVER RESPONSES

public class Main implements ActionListener{
    //Properties
    boolean blnConnected = false;
    boolean blnHost;
    String strUsername;
    String strServerAddress;
    SuperSocketMaster HostSocket = new SuperSocketMaster(6000, this);
    SuperSocketMaster ClientSocket = new SuperSocketMaster(strServerAddress, 6000, this);

    //Methods
    public void actionPerformed(ActionEvent evt){
        

    }
        

    //Searching For Servers (Continue Listening for 5 Seconds)
    public void ServerSearching(){
        try{
            //Create a DatagramSocket
            DatagramSocket outSocket = new DatagramSocket();
            
            //Set the broadcast address
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

            //Message to be sent
            String message = "searching for servers";
            byte[] data = message.getBytes();

            //Create a DatagramPacket with the message, length, and broadcast address
            DatagramPacket outPacket = new DatagramPacket(data, data.length, broadcastAddress, 6001);

            //Send the packet
            outSocket.send(outPacket);

            //Close the socket
            outSocket.close();

            // Create a DatagramSocket to listen on a specific port
            DatagramSocket inSocket = new DatagramSocket(6002);

            // Buffer to store incoming data
            byte[] buffer = new byte[1024];

            // Create a DatagramPacket to receive the incoming data
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);

            //Receive the packet
            inSocket.receive(inPacket);

            //Extract and display the received message
            String receivedMessage = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println(receivedMessage);
            
            // Close the socket
            inSocket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }     
    }


    //Listening For New Clients (Continue listening until blnConnected == true)
    public void ServerConnection(){
        try{
            // Create a DatagramSocket to listen on a specific port
            DatagramSocket inSocket = new DatagramSocket(6001);

            // Buffer to store incoming data
            byte[] buffer = new byte[1024];

            // Create a DatagramPacket to receive the incoming data
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);

            //Receive the packet
            inSocket.receive(inPacket);

            //Extract and display the received message
            String receivedMessage = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println(receivedMessage);

            // Close the socket
            inSocket.close();

            //Delay
            Thread.sleep(250);

            //Create a DatagramSocket
            DatagramSocket outSocket = new DatagramSocket();
            
            //Set the broadcast address
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

            //Message to be sent
            String message = HostSocket.getMyAddress();
            byte[] data = message.getBytes();

            //Create a DatagramPacket with the message, length, and broadcast address
            DatagramPacket outPacket = new DatagramPacket(data, data.length, broadcastAddress, 6002);

            //Send the packet
            outSocket.send(outPacket);

            //Close the socket
            outSocket.close();
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }


    //Constructor
    public Main(){

    }
}