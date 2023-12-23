//Importing the Panels Directory (or package)
import Panels.*;

//Importing Dependencies
import java.awt.event.*;
import javax.swing.*;

//Panel Identifier
//1 - MAIN MENU

public class View implements ActionListener, MouseMotionListener, KeyListener{
    //Properties
    Controller theController;

    //JComponents
    JFrame theFrame = new JFrame();
    homePanel theHomePanel = new homePanel();
    serverSelectionPanel theServerSelectionPanel = new serverSelectionPanel();

    //Methods
    public void actionPerformed(ActionEvent evt){
        //Controller Callback
        theController.viewInteracted(evt);
    }

    public void mouseDragged(MouseEvent evt){
        //Controller Callback
        theController.mouseInteracted(evt);
    }

    public void keyTyped(KeyEvent evt){
        //Controller Callback
        theController.keysInteracted(evt);
    }

    public void swapPanel(int intSwapTo){
        //if(intSwapTo == 1){

        //}
    }

    //Constructor
    public View(Controller theController){
        this.theController = theController;
        
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
}
