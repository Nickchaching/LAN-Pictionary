package Panels;
import javax.swing.*;

public class paneltest {
    JFrame theFrame = new JFrame();
    drawerPRPanel testPanel = new drawerPRPanel();

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
