//Importing the Panels Directory (or package)
import Panels.*;

//Importing Dependencies
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


//When Project is complete, create ClientModel and ServerModel classes, then, create the object within the controller that is necessary

//Panel Identifier
//1 - MAIN MENU

public class View implements ActionListener, MouseMotionListener, KeyListener, DocumentListener{
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
    nonDrawerRoundPanel theNonDrawerRoundPanel = new nonDrawerRoundPanel();
    postRoundPanel thePostRoundPanel = new postRoundPanel();


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
            theModel.changedScore();
            //Change Panel to Drawing Frame
            theFrame.setContentPane(theDrawerRoundPanel);
            theFrame.pack();
            theDrawerRoundPanel.updateItemLabel(theModel.getObject());
            if(theModel.isHost()){
                theDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                theNonDrawerRoundPanel.updatePlayerList(theModel.changedScore());
            }
            else{
                theDrawerRoundPanel.updatePlayerList(theModel.getScores());
                theNonDrawerRoundPanel.updatePlayerList(theModel.getScores());
            }
        }
        //Pre-Round Timer Completed
        else if(evt.getSource() == theModel.preRoundTimer){
            theModel.choseObject(1);
            //Change Panel to Drawing Panel
            theFrame.setContentPane(theDrawerRoundPanel);
            theFrame.pack();
            theDrawerRoundPanel.updateItemLabel(theModel.getObject());
            if(theModel.isHost()){
                theDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                theNonDrawerRoundPanel.updatePlayerList(theModel.changedScore());
            }
            else{
                theDrawerRoundPanel.updatePlayerList(theModel.getScores());
                theNonDrawerRoundPanel.updatePlayerList(theModel.getScores());
            }
        }
        //Server Message Handling
        else if(evt.getSource() == theModel.HostSocket){
            if(theModel.HostSocket.readText().substring(0,1).equals("0")){
                int intType = theModel.serverMessageRecieved();
                
                if(intType == 0){
                    theServerLobbyPanel.updatePlayerList(theModel.getPlayerList());
                }
                else if(intType == 1){
                    if(theFrame.getContentPane() == theDrawerRoundPanel){
                        theDrawerRoundPanel.updateChatArea(theModel.getMessageData());
                        if(theModel.checkScoreUpdated()){
                            theDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                        }
                    }
                    else{
                        theNonDrawerRoundPanel.updateChatArea(theModel.getMessageData());
                        if(theModel.checkScoreUpdated()){
                            theNonDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                        }
                    }
                }
                else if(intType == 2){
                    //Change Panel to Drawing Frame
                    theFrame.setContentPane(theNonDrawerRoundPanel);
                    theFrame.pack();
                    theNonDrawerRoundPanel.updateItemLabel(theModel.getObjectLength());
                    if(theModel.isHost()){
                        theDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                        theNonDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                    }
                    else{
                        theDrawerRoundPanel.updatePlayerList(theModel.getScores());
                        theNonDrawerRoundPanel.updatePlayerList(theModel.getScores());
                    }
                }
                else if(intType == 3){
                    theNonDrawerRoundPanel.updateDraw(theModel.getDrawingData());
                }
            }
        }
        //Client Message Handling
        else if(evt.getSource() == theModel.ClientSocket){
            if(theModel.ClientSocket.readText().substring(0,1).equals("1")){
                int intType = theModel.clientMessageRecieved();

                if(intType == 0){
                    theClientLobbyPanel.updatePlayerList(theModel.getPlayers());
                }
                else if(intType == 1){
                    if(theFrame.getContentPane() == theDrawerRoundPanel){
                        theDrawerRoundPanel.updateChatArea(theModel.getMessageData());
                    }
                    else{
                        theNonDrawerRoundPanel.updateChatArea(theModel.getMessageData());
                    }
                }
                else if(intType == 2){
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
                else if(intType == 3){
                    if(theFrame.getContentPane() == theDrawerPRPanel){
                        theDrawerPRPanel.updateTimer(theModel.getTimeRemPer());
                    }
                    else if(theFrame.getContentPane() == theNonDrawerPRPanel){
                        theNonDrawerPRPanel.updateTimer(theModel.getTimeRemPer());
                    }
                }
                else if(intType == 4){
                    if(!theModel.isDrawing()){
                        theFrame.setContentPane(theNonDrawerRoundPanel);
                        theFrame.pack();
                        theNonDrawerRoundPanel.updateItemLabel(theModel.getObjectLength());
                    }
                    if(theModel.isHost()){
                        theDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                        theNonDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                    }
                    else{
                        theDrawerRoundPanel.updatePlayerList(theModel.getScores());
                        theNonDrawerRoundPanel.updatePlayerList(theModel.getScores());
                    }
                }
                else if(intType == 5){
                    if(theFrame.getContentPane() == theNonDrawerRoundPanel){
                        theNonDrawerRoundPanel.updateDraw(theModel.getDrawingData());
                    }
                }
                else if(intType == 6){
                    if(theFrame.getContentPane() == theDrawerRoundPanel){
                        theDrawerRoundPanel.updateTimer(theModel.getTimeRemPer());
                    }
                    else{
                        theNonDrawerRoundPanel.updateTimer(theModel.getTimeRemPer());
                    }
                }
                else if(intType == 7){
                    if(theFrame.getContentPane() == theDrawerRoundPanel){
                        theDrawerRoundPanel.updatePlayerList(theModel.getScores());
                    }
                    else{
                        theNonDrawerRoundPanel.updatePlayerList(theModel.getScores());
                    }
                }
                else if(intType == 8){
                    thePostRoundPanel.initializePanel(theModel.getObject());
                    theFrame.setContentPane(thePostRoundPanel);
                    theFrame.pack();
                    theDrawerRoundPanel.clearScreen();
                    theNonDrawerRoundPanel.clearScreen();
                }
                else if(intType == 9){
                    thePostRoundPanel.updateTimer(theModel.getTimeRemPer());
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
            else if(theFrame.getContentPane() == theNonDrawerRoundPanel){
                theNonDrawerRoundPanel.updateTimer(Double.parseDouble(theModel.sendPing(2)));
            }
            else if(theFrame.getContentPane() == thePostRoundPanel){
                thePostRoundPanel.updateTimer(Double.parseDouble(theModel.sendPing(3)));
            }
            //Responsible for Procesing and Sending out Telemetry
        }

        //Drawing Telemetry
        else if(evt.getSource() == theDrawerRoundPanel.ClearButton){
            theDrawerRoundPanel.clearScreen();
            int intDrawClear[] = new int[4];
            intDrawClear[0] = -1;
            intDrawClear[1] = -1;
            intDrawClear[2] = -1;
            intDrawClear[3] = -1;
            theModel.newDrawData(intDrawClear);
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
        //Chat Message Handling
        else if(evt.getSource() == theDrawerRoundPanel.ChatField || evt.getSource() == theNonDrawerRoundPanel.ChatField){
            if(evt.getSource() == theDrawerRoundPanel.ChatField){
                if(!theModel.newMessage(theDrawerRoundPanel.getChatField())){
                    if(theModel.isHost()){
                        theDrawerRoundPanel.updateChatArea(theModel.getMessageData());
                        if(theModel.checkScoreUpdated()){
                            theDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                        }
                    }
                }
            }
            else{
                if(!theModel.newMessage(theNonDrawerRoundPanel.getChatField())){
                    if(theModel.isHost()){
                        theNonDrawerRoundPanel.updateChatArea(theModel.getMessageData());
                        if(theModel.checkScoreUpdated()){
                            theNonDrawerRoundPanel.updatePlayerList(theModel.changedScore());
                        }
                    }
                }
            }
        }
        //Round Timer Completed
        else if(evt.getSource() == theModel.roundTimer){
            theModel.endRound();
            //Change Panel to Post-Round Panel
            thePostRoundPanel.initializePanel(theModel.getObject());
            theFrame.setContentPane(thePostRoundPanel);
            theFrame.pack();
        }
        //Post-Round Timer Completed
        else if(evt.getSource() == theModel.postRoundTimer){
            if(theModel.resetRound()){
                theDrawerRoundPanel.clearScreen();
                theNonDrawerRoundPanel.clearScreen();

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
            else{
                //Show Leaderboard
            }
        }
    }

    public void mouseDragged(MouseEvent evt){
        int intX = evt.getX();
        int intY = evt.getY();
        if(intX > 15 && intX < 800 && intY > 25 && intY < 695){
            theModel.newDrawData(theDrawerRoundPanel.updateDraw(intX, intY));
        }
    }

    public void insertUpdate(DocumentEvent evt){
        theDrawerRoundPanel.updateChar(1);
        theNonDrawerRoundPanel.updateChar(1);
    }

    public void removeUpdate(DocumentEvent evt){
        theDrawerRoundPanel.updateChar(0);
        theNonDrawerRoundPanel.updateChar(0);
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
        theDrawerRoundPanel.ChatField.addActionListener(this);
        theDrawerRoundPanel.addMouseMotionListener(this);
        theDrawerRoundPanel.ChatField.getDocument().addDocumentListener(this);

        //Adding NonDrawerRound Panel Action Listeners
        theNonDrawerRoundPanel.ChatField.addActionListener(this);
        theNonDrawerRoundPanel.ChatField.getDocument().addDocumentListener(this);

        //Initialzing the Frame
        theFrame.setContentPane(theHomePanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.pack();
        theFrame.setVisible(true);

    }

    //Unused Methods
    public void changedUpdate(DocumentEvent evt){
        
    }

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
