//This class is intended to be run as a seperate thread. It receives server pings on the LAN and updates the buttons 

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class findServer implements Runnable{
    //Properties
    Model theModel;
    String[] strServerLoad;
    
    //Methods
    public void run(){
        try{
            // Create a DatagramSocket to listen on a specific port
            DatagramSocket inSocket = new DatagramSocket(6001);

            // Buffer to store incoming data
            byte[] buffer = new byte[1024];

            // Create a DatagramPacket to receive the incoming data
            DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
            
            while(!theModel.blnConnected){
                //Receive the packet
                inSocket.receive(inPacket);

                //Extract and display the received message
                String receivedMessage = new String(inPacket.getData(), 0, inPacket.getLength());
                strServerLoad = receivedMessage.split(",");
                theModel.updateServerList(strServerLoad);
            }

            // Close the socket
            inSocket.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Constructor
    public findServer(Model theModel){
        this.theModel = theModel;
    }
}
