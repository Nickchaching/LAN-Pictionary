package Panels;
import javax.swing.*;
import java.awt.event.*;


public class paneltest{
    //Properties
    JFrame theFrame = new JFrame();
    drawerRoundPanel testPanel = new drawerRoundPanel();
    
    //Drawing Properties
    static int intPenSize; 
        //3 is small, 5 is large
    static int intPenColour; 
        //ended up using clrSelected on drawerRoundPanel
    

    //Methods

    public paneltest(){
        theFrame.setContentPane(testPanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(true);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    public static void main(String[] args){
        new paneltest();
    }
}
