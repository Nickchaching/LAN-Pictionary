import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class findServer implements Runnable{
    //Properties
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
            
            while(!Main.blnConnected){
                int intCount = 0;
                
                //Receive the packet
                inSocket.receive(inPacket);

                //Extract and display the received message
                String receivedMessage = new String(inPacket.getData(), 0, inPacket.getLength());
                strServerLoad = receivedMessage.split(",");

                //Check to see if IP Exists and Slots Filled
                for(intCount = 0; intCount < 5; intCount++){
                    if(Main.strServerList[intCount][0] == null){
                        Main.strServerList[intCount] = strServerLoad;
                        intCount = 5;
                    }
                    else if(Main.strServerList[intCount][0].equals(strServerLoad[0])){
                        intCount = 5;
                    }
                }

                System.out.println("New PARSE");
                System.out.println("Server IP: "+Main.strServerList[0][0]+" | Server Name: "+Main.strServerList[0][1]+" | Server Players: "+Main.strServerList[0][2]);
                System.out.println("Server IP: "+Main.strServerList[1][0]+" | Server Name: "+Main.strServerList[1][1]+" | Server Players: "+Main.strServerList[1][2]);
                System.out.println("Server IP: "+Main.strServerList[2][0]+" | Server Name: "+Main.strServerList[2][1]+" | Server Players: "+Main.strServerList[2][2]);
                System.out.println("Server IP: "+Main.strServerList[3][0]+" | Server Name: "+Main.strServerList[3][1]+" | Server Players: "+Main.strServerList[3][2]);
                System.out.println("Server IP: "+Main.strServerList[4][0]+" | Server Name: "+Main.strServerList[4][1]+" | Server Players: "+Main.strServerList[4][2]);
                
                Main.Server1Button.setText(Main.strServerList[0][1]+" | "+Main.strServerList[0][0]+" | "+Main.strServerList[0][2]+" Players");
            }

            // Close the socket
            inSocket.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Constructor
    public findServer(){

    }
}
