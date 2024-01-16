package Panels;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class paneltest{
    //Properties
    JFrame theFrame = new JFrame();
    drawerRoundPanel testPanel = new drawerRoundPanel();


    //Constructor
    public paneltest(){
        theFrame.setContentPane(testPanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(true);
        theFrame.pack();
        theFrame.setVisible(true);
        
        String[] strPlayerList = new String[8];
        strPlayerList[0] = "bob";
        strPlayerList[1] = "bob1";
        strPlayerList[2] = "bob2";
        strPlayerList[3] = "bob3";
        strPlayerList[4] = "bob4";
        strPlayerList[5] = "bob5";
        strPlayerList[6] = "bob6";
        strPlayerList[7] = "bob7";


        testPanel.updatePlayerList(strPlayerList);
    }

    public static void main(String[] args){
        new paneltest();
    }
}
