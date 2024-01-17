package Panels;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class paneltest{
    //Properties
    JFrame theFrame = new JFrame();
    postRoundPanel testPanel = new postRoundPanel();


    //Constructor
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
