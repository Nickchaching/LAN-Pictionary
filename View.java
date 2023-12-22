//Importing the Panels Directory (or package)
import Panels.*;

//Importing Dependencies
import java.awt.event.*;
import javax.swing.*;

public class View implements ActionListener{
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

    //Constructor
    public View(Controller theController){
        this.theController = theController;
        
        theFrame.setContentPane(theHomePanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(false);
        theFrame.pack();
        theFrame.setVisible(true);
    }
}
