public class Model{
    //Properties
    Controller theController;

    boolean blnConnected = false;
    boolean blnHost;
    String strUsername;
    String strIncomingSplit[];
    
    SuperSocketMaster HostSocket;
    int intPlayers = 0;
    Thread broadcastIP = new Thread(new broadcastIP());

    SuperSocketMaster ClientSocket;
    String strServerAddress;
    String[][] strServerList = new String[5][3];
    Thread findServer = new Thread(new findServer());

    //Methods


    //Constuctor
    public Model(Controller theController){
        this.theController = theController;
    }
}
