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
    clientLobbyPanel theClientLobbyPanel = new clientLobbyPanel();
    drawerPRPanel theDrawerPRPanel = new drawerPRPanel();
    nonDrawerPRPanel theNonDrawerPRPanel = new nonDrawerPRPanel();
    drawerRoundPanel theDrawerRoundPanel = new drawerRoundPanel();


    //Methods
    public void actionPerformed(ActionEvent evt){
        //Initial Host Connection
        if(evt.getSource() == theHomePanel.HostGameButton){
            if(theModel.initializeHost(theHomePanel.NameField.getText())){
                theServerLobbyPanel.displayThemes(theModel.getThemes());
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
                theFrame.setContentPane(theClientLobbyPanel);
                theFrame.pack();
            }
            else{
                //Display an "error label" and update all buttons to reflect the new list
            }
        }
        //Server Theme Selection
        else if(evt.getSource() == theServerLobbyPanel.Theme1Button || evt.getSource() == theServerLobbyPanel.Theme2Button || evt.getSource() == theServerLobbyPanel.Theme3Button || evt.getSource() == theServerLobbyPanel.Theme4Button || evt.getSource() == theServerLobbyPanel.Theme5Button || evt.getSource() == theServerLobbyPanel.Theme6Button || evt.getSource() == theServerLobbyPanel.Theme7Button || evt.getSource() == theServerLobbyPanel.Theme8Button){
            int intButton = 0;
            if(evt.getSource() == theServerLobbyPanel.Theme1Button){
                intButton = 1;
            }
            else if(evt.getSource() == theServerLobbyPanel.Theme2Button){
                intButton = 2;
            }
            else if(evt.getSource() == theServerLobbyPanel.Theme3Button){
                intButton = 3;
            }
            else if(evt.getSource() == theServerLobbyPanel.Theme4Button){
                intButton = 4;
            }
            else if(evt.getSource() == theServerLobbyPanel.Theme5Button){
                intButton = 5;
            }
            else if(evt.getSource() == theServerLobbyPanel.Theme6Button){
                intButton = 6;
            }
            else if(evt.getSource() == theServerLobbyPanel.Theme7Button){
                intButton = 7;
            }
            else if(evt.getSource() == theServerLobbyPanel.Theme8Button){
                intButton = 8;
            }
            if(theModel.selectTheme(intButton)){
                theServerLobbyPanel.updateThemeSelection(intButton);
            }
        }
        //Server Start Game
        else if(evt.getSource() == theServerLobbyPanel.StartGameButton){
            if(theModel.startGame()){
                if(theModel.newRound()){
                    //Host is drawing
                    theDrawerPRPanel.initializePanel(theModel.getRound(), theModel.getObjectChoices());
                    theFrame.setContentPane(theDrawerPRPanel);
                    theFrame.pack();
                }
                else{
                    //Host is not drawing
                    theNonDrawerPRPanel.initializePanel(theModel.getRound());
                    theFrame.setContentPane(theNonDrawerPRPanel);
                    theFrame.pack();
                }
            }
        }
        //Drawing Choice Selected
        else if(evt.getSource() == theDrawerPRPanel.Choice1Button || evt.getSource() == theDrawerPRPanel.Choice2Button){
            int intButton;
            if(evt.getSource() == theDrawerPRPanel.Choice1Button){
                intButton = 1;
            }
            else{
                intButton = 2;
            }
            theModel.choseObject(intButton);
            //Change Panel to Drawing Frame
            theFrame.setContentPane(theDrawerRoundPanel);
            theFrame.pack();
            theDrawerRoundPanel.updateItemLabel(theModel.getObject());
            System.out.println("Changing Frame");
            System.out.println(theModel.intObjectLength);
            System.out.println(theModel.strObject);
        }
        //Pre-Round Timer Completed
        else if(evt.getSource() == theModel.preRoundTimer){
            theModel.choseObject(1);
            //Change Panel to Drawing Frame
            theFrame.setContentPane(theDrawerRoundPanel);
            theFrame.pack();
            theDrawerRoundPanel.updateItemLabel(theModel.getObject());
            System.out.println("Changing Frame");
            System.out.println(theModel.intObjectLength);
            System.out.println(theModel.strObject);
        }
        //Server Message Handling
        else if(evt.getSource() == theModel.HostSocket){
            if(theModel.HostSocket.readText().substring(0,1).equals("0")){
                if(theModel.serverMessageRecieved() == 0){
                    theServerLobbyPanel.updatePlayerList(theModel.getPlayerList());
                }
                else if(theModel.serverMessageRecieved() == 2){
                    //Change Panel to Drawing Frame
                    theFrame.setContentPane(theDrawerRoundPanel);
                    theFrame.pack();
                    System.out.println("Changing Frame");
                    System.out.println(theModel.intObjectLength);
                    System.out.println(theModel.strObject);
                }
            }
        }
        //Client Message Handling
        else if(evt.getSource() == theModel.ClientSocket){
            if(theModel.ClientSocket.readText().substring(0,1).equals("1")){
                if(theModel.clientMessageRecieved() == 0){
                    theClientLobbyPanel.updatePlayerList(theModel.getPlayers());
                }
                else if(theModel.clientMessageRecieved() == 2){
                    if(theModel.isDrawing()){
                        theDrawerPRPanel.initializePanel(theModel.getRound(), theModel.getObjectChoices());
                        theFrame.setContentPane(theDrawerPRPanel);
                        theFrame.pack();
                    }
                    else{
                        theNonDrawerPRPanel.initializePanel(theModel.getRound());
                        theFrame.setContentPane(theNonDrawerPRPanel);
                        theFrame.pack();
                    }
                }
                else if(theModel.clientMessageRecieved() == 3){
                    if(theFrame.getContentPane() == theDrawerPRPanel){
                        theDrawerPRPanel.updateTimer(theModel.getTimeRemPer());
                    }
                    else if(theFrame.getContentPane() == theNonDrawerPRPanel){
                        theNonDrawerPRPanel.updateTimer(theModel.getTimeRemPer());
                    }
                }
                else if(theModel.clientMessageRecieved() == 4){
                    //Change Panel to Drawing Frame
                    theFrame.setContentPane(theDrawerRoundPanel);
                    theFrame.pack();
                }
                else if(theModel.clientMessageRecieved() == 6){
                    if(theFrame.getContentPane() == theDrawerRoundPanel){
                        theDrawerRoundPanel.updateTimer(theModel.getTimeRemPer());
                    }
                }
            }
        }
        //Pushing Regular Updates
        else if(evt.getSource() == theModel.pingTimer){
            if(theFrame.getContentPane() == theServerLobbyPanel){
                theModel.sendPing(0);
            }
            else if(theFrame.getContentPane() == theDrawerPRPanel){
                theDrawerPRPanel.updateTimer(Double.parseDouble(theModel.sendPing(1)));
            }
            else if(theFrame.getContentPane() == theNonDrawerPRPanel){
                theNonDrawerPRPanel.updateTimer(Double.parseDouble(theModel.sendPing(1)));
            }
            else if(theFrame.getContentPane() == theDrawerRoundPanel){
                theDrawerRoundPanel.updateTimer(Double.parseDouble(theModel.sendPing(2)));
            }
            //Responsible for Procesing and Sending out Telemetry
        }

        //Drawing Telemetry
        if(evt.getSource() == theDrawerRoundPanel.ClearButton){
            theDrawerRoundPanel.clearScreen();
        }
        else if(evt.getSource() == theDrawerRoundPanel.SSizeButton){
            theDrawerRoundPanel.updateSize(6);
        }
        else if(evt.getSource() == theDrawerRoundPanel.LSizeButton){
            theDrawerRoundPanel.updateSize(10);
        }
        else if(evt.getSource() == theDrawerRoundPanel.YellowButton){
            theDrawerRoundPanel.updateColour(1);
        }
        else if(evt.getSource() == theDrawerRoundPanel.GreenButton){
            theDrawerRoundPanel.updateColour(2);
        }
        else if(evt.getSource() == theDrawerRoundPanel.BlueButton){
            theDrawerRoundPanel.updateColour(3);
        }
        else if(evt.getSource() == theDrawerRoundPanel.PurpleButton){
            theDrawerRoundPanel.updateColour(4);
        }
        else if(evt.getSource() == theDrawerRoundPanel.RedButton){
            theDrawerRoundPanel.updateColour(5);
        }
        else if(evt.getSource() == theDrawerRoundPanel.OrangeButton){
            theDrawerRoundPanel.updateColour(6);
        }
        else if(evt.getSource() == theDrawerRoundPanel.BlackButton){
            theDrawerRoundPanel.updateColour(7);
        }
    }

    public void mouseDragged(MouseEvent evt){
        int intX = evt.getX();
        int intY = evt.getY();
        if(intX > 15 && intX < 800 && intY > 25 && intY < 695){
            theDrawerRoundPanel.updateDraw(intX, intY);
        }
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

        //Adding Server Lobby Panel Action Listeners
        theServerLobbyPanel.Theme1Button.addActionListener(this);
        theServerLobbyPanel.Theme2Button.addActionListener(this);
        theServerLobbyPanel.Theme3Button.addActionListener(this);
        theServerLobbyPanel.Theme4Button.addActionListener(this);
        theServerLobbyPanel.Theme5Button.addActionListener(this);
        theServerLobbyPanel.Theme6Button.addActionListener(this);
        theServerLobbyPanel.Theme7Button.addActionListener(this);
        theServerLobbyPanel.Theme8Button.addActionListener(this);
        theServerLobbyPanel.StartGameButton.addActionListener(this);
        
        //Adding DrawingPR Panel Action Listeners
        theDrawerPRPanel.Choice1Button.addActionListener(this);
        theDrawerPRPanel.Choice2Button.addActionListener(this);

        //Adding DrawingRound Panel Action Listeners
        theDrawerRoundPanel.ClearButton.addActionListener(this);
        theDrawerRoundPanel.SSizeButton.addActionListener(this);
        theDrawerRoundPanel.LSizeButton.addActionListener(this);
        theDrawerRoundPanel.YellowButton.addActionListener(this);
        theDrawerRoundPanel.GreenButton.addActionListener(this);
        theDrawerRoundPanel.BlueButton.addActionListener(this);
        theDrawerRoundPanel.PurpleButton.addActionListener(this);
        theDrawerRoundPanel.RedButton.addActionListener(this);
        theDrawerRoundPanel.OrangeButton.addActionListener(this);
        theDrawerRoundPanel.BlackButton.addActionListener(this);
        theDrawerRoundPanel.addMouseMotionListener(this);

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
