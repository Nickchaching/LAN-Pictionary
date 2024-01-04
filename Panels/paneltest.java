package Panels;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class paneltest implements ActionListener, MouseMotionListener{
    //Properties
    JFrame theFrame = new JFrame();
    drawerRoundPanel theDrawerRoundPanel = new drawerRoundPanel();

    //Methods
    public void actionPerformed(ActionEvent evt){
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
    public void mouseMoved(MouseEvent evt){

    }

    //Constructor
    public paneltest(){
        theFrame.setContentPane(theDrawerRoundPanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(true);
        theFrame.pack();
        theFrame.setVisible(true);

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
    }

    public static void main(String[] args){
        new paneltest();
    }
}
