//Importing the Panels Directory (or package)
import Panels.*;

//Importing Dependencies
import java.awt.event.*;
import javax.swing.*;

//When Project is complete, create ClientModel and ServerModel classes, then, create the object within the controller that is necessary

//Panel Identifier
//1 - MAIN MENU

public class View implements ActionListener, MouseMotionListener, KeyListener{
    //Properties
    Model theModel = new Model(this);

    //JComponents
    JFrame theFrame = new JFrame();
    homePanel theHomePanel = new homePanel();
    serverSelectionPanel theServerSelectionPanel = new serverSelectionPanel();
    serverLobbyPanel theServerLobbyPanel = new serverLobbyPanel();

    //Methods
    public void actionPerformed(ActionEvent evt){
        //Initial Host Connection
        if(evt.getSource() == theHomePanel.HostGameButton){
            if(theModel.initializeHost(theHomePanel.NameField.getText())){
                theFrame.setContentPane(theServerLobbyPanel);
                theFrame.pack();
            }
            else{
                theHomePanel.NameField.setText("Please Enter a Name");
            }
        }
        //Initial Client Connection
        else if(evt.getSource() == theHomePanel.JoinGameButton){
            if(theModel.initializeClient(theHomePanel.NameField.getText())){
                theFrame.setContentPane(theServerSelectionPanel);
                theFrame.pack();
            }
            else{
                theHomePanel.NameField.setText("Please Enter a Name");
            }
        }
        //Client Server Selection
        else if(evt.getSource() == theServerSelectionPanel.Server1Button || evt.getSource() == theServerSelectionPanel.Server2Button || evt.getSource() == theServerSelectionPanel.Server3Button || evt.getSource() == theServerSelectionPanel.Server4Button || evt.getSource() == theServerSelectionPanel.Server5Button){
            int intButton = 0;
            if(evt.getSource() == theServerSelectionPanel.Server1Button){
                intButton = 1;
            }
            else if(evt.getSource() == theServerSelectionPanel.Server2Button){
                intButton = 2;
            }
            else if(evt.getSource() == theServerSelectionPanel.Server3Button){
                intButton = 3;
            }
            else if(evt.getSource() == theServerSelectionPanel.Server4Button){
                intButton = 4;
            }
            else if(evt.getSource() == theServerSelectionPanel.Server5Button){
                intButton = 5;
            }

            if(theModel.initializeClientConnection(intButton)){
                //Swap to Lobby Panel
            }
            else{
                //Display an "error label" and update all buttons to reflect the new list
            }
        }
        //Server Message Handling
        else if(evt.getSource() == theModel.HostSocket){
            if(theModel.HostSocket.readText().substring(0,1).equals("0")){
                if(theModel.serverMessageRecieved() == 0){
                    theServerLobbyPanel.updatePlayerList(theModel.getPlayerList());
                }
            }
        }
        //Client Message Handling
        else if(evt.getSource() == theModel.ClientSocket){
            if(theModel.HostSocket.readText().substring(0,1).equals("1")){
                //Client Message Handling
            }
        }
    }

    public void mouseDragged(MouseEvent evt){
        
    }

    public void keyTyped(KeyEvent evt){
        
    }

    public void updateServerButton(String strUpdate, int intButton){
        if(intButton == 1){
            theServerSelectionPanel.Server1Button.setText(strUpdate);
            theServerSelectionPanel.Server1Button.setVisible(true);
        }
        else if(intButton == 2){
            theServerSelectionPanel.Server2Button.setText(strUpdate);
            theServerSelectionPanel.Server2Button.setVisible(true);
        }
        else if(intButton == 3){
            theServerSelectionPanel.Server3Button.setText(strUpdate);
            theServerSelectionPanel.Server3Button.setVisible(true);
        }
        else if(intButton == 4){
            theServerSelectionPanel.Server4Button.setText(strUpdate);
            theServerSelectionPanel.Server4Button.setVisible(true);
        }
        else if(intButton == 5){
            theServerSelectionPanel.Server5Button.setText(strUpdate);
            theServerSelectionPanel.Server5Button.setVisible(true);
        }
    }
    

    //Constructor
    public View(){
        //Adding Home Panel Action Listeners
        theHomePanel.HostGameButton.addActionListener(this);
        theHomePanel.JoinGameButton.addActionListener(this);

        //Adding Server Selection Panel Action Listeners
        theServerSelectionPanel.Server1Button.addActionListener(this);
        theServerSelectionPanel.Server2Button.addActionListener(this);
        theServerSelectionPanel.Server3Button.addActionListener(this);
        theServerSelectionPanel.Server4Button.addActionListener(this);
        theServerSelectionPanel.Server5Button.addActionListener(this);

        //Initialzing the Frame
        theFrame.setContentPane(theHomePanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    //Unused Methods
    public void keyPressed(KeyEvent evt){

    }

    public void keyReleased(KeyEvent evt){

    }

    public void mouseMoved(MouseEvent evt){

    }

    //Main Method
    public static void main(String args[]){
        new View();
    }
}
