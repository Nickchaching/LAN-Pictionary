import Panels.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;



public class demotest implements MouseMotionListener, ActionListener, DocumentListener{
    //Properties
    JFrame theFrame = new JFrame();
    demoPanel theDemoPanel = new demoPanel();
    String strName = "Name";

    //Methods

    public void actionPerformed(ActionEvent evt){
        //Buttons
        if(evt.getSource() == theDemoPanel.ClearButton){
            theDemoPanel.clearScreen();
        }
        else if(evt.getSource() == theDemoPanel.SSizeButton){
            theDemoPanel.updateSize(6);
        }
        else if(evt.getSource() == theDemoPanel.LSizeButton){
            theDemoPanel.updateSize(10);
        }
        else if(evt.getSource() == theDemoPanel.YellowButton){
            theDemoPanel.updateColour(1);
        }
        else if(evt.getSource() == theDemoPanel.GreenButton){
            theDemoPanel.updateColour(2);
        }
        else if(evt.getSource() == theDemoPanel.BlueButton){
            theDemoPanel.updateColour(3);
        }
        else if(evt.getSource() == theDemoPanel.PurpleButton){
            theDemoPanel.updateColour(4);
        }
        else if(evt.getSource() == theDemoPanel.RedButton){
            theDemoPanel.updateColour(5);
        }
        else if(evt.getSource() == theDemoPanel.OrangeButton){
            theDemoPanel.updateColour(6);
        }
        else if(evt.getSource() == theDemoPanel.BlackButton){
            theDemoPanel.updateColour(7);
        }
        //Chat Message Handling
        else if(evt.getSource() == theDemoPanel.ChatField){
            theDemoPanel.updateChatArea(strName + ": " + theDemoPanel.getChatField());
        }
    }

    public String getChatField(){
        String strText = theDemoPanel.ChatField.getText();
        theDemoPanel.ChatField.setText("");
        return strText;
    }


    public void mouseDragged(MouseEvent evt){
        int intX = evt.getX();
        int intY = evt.getY();
        if(intX > 15 && intX < 800 && intY > 25 && intY < 695){
           theDemoPanel.updateDraw(intX, intY);
        }
    }

    
    public void insertUpdate(DocumentEvent evt){
        theDemoPanel.updateChar(1);
    }

    public void removeUpdate(DocumentEvent evt){
        theDemoPanel.updateChar(0);
    }

    public void changedUpdate(DocumentEvent evt){

    }
    
    public void mouseMoved(MouseEvent evt){

    }

    

    

    //Constructor
    public demotest(){

        theDemoPanel.addMouseMotionListener(this);


        theDemoPanel.ClearButton.addActionListener(this);
        theDemoPanel.SSizeButton.addActionListener(this);
        theDemoPanel.LSizeButton.addActionListener(this);
        theDemoPanel.YellowButton.addActionListener(this);
        theDemoPanel.GreenButton.addActionListener(this);
        theDemoPanel.BlueButton.addActionListener(this);
        theDemoPanel.PurpleButton.addActionListener(this);
        theDemoPanel.RedButton.addActionListener(this);
        theDemoPanel.OrangeButton.addActionListener(this);
        theDemoPanel.BlackButton.addActionListener(this);
        theDemoPanel.ChatField.addActionListener(this);
        theDemoPanel.ChatField.getDocument().addDocumentListener(this);
        theDemoPanel.addMouseMotionListener(this);


        theFrame.setContentPane(theDemoPanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setResizable(true);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    public static void main(String[] args){
        new demotest();
    }
}
