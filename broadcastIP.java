import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class broadcastIP implements Runnable{
    //Methods
    public void run(){
        try{
            //Create a DatagramSocket
            DatagramSocket outSocket = new DatagramSocket();
            
            //Set the broadcast address
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

            while(!Main.blnConnected){
                //Message to be sent
                String message = Main.HostSocket.getMyAddress()+","+Main.strUsername+","+Main.intPlayers;
                byte[] data = message.getBytes();

                //Create a DatagramPacket with the message, length, and broadcast address
                DatagramPacket outPacket = new DatagramPacket(data, data.length, broadcastAddress, 6001);

                //Send the packet
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
    public broadcastIP(){

    }
}
