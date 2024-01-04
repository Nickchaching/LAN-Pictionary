package Panels;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



public class paneltest implements ActionListener, MouseMotionListener{
    //Properties
    JFrame theFrame = new JFrame();
    static drawerRoundPanel theDrawerRoundPanel = new drawerRoundPanel();
    //Drawing Properties
    int intPenSize; //6 is small, 10 is large
    int intPenColour; 
    //Saving Drawings After Repaints
    static int intCount = 0; 
    static int intDraw[][] = new int[536520][4]; //Array Columns: X, Y, Pen Size, Pen Colour

    //Methods
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == theDrawerRoundPanel.ClearButton){
            //Variables
            int intRow = 0;
            int intColumn = 0;
            //Cover Drawings on Screen
            Graphics g = theDrawerRoundPanel.getGraphics();
            g.setColor(assets.clrWhite);
            g.fillRect(15, 25, 789, 680);
            //Reset Array Data
            for(intRow = 0; intRow < 536520; intRow++){
                for(intColumn = 0; intColumn < 4; intColumn++){
                    intDraw[intRow][intColumn] = 0;
                }
            }
            intCount = 0;
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
            //Save Drawing Data in Array
            intDraw[intCount][0] = intX; 
            intDraw[intCount][1] = intY; 
            intDraw[intCount][2] = theDrawerRoundPanel.intPenSize;
            intDraw[intCount][3] = theDrawerRoundPanel.intPenColour;
            System.out.println("intCount: " + intCount);
            System.out.println("X: " + intDraw[intCount][0]);
            System.out.println("Y: " + intDraw[intCount][1]);
            System.out.println("Size: " + intDraw[intCount][2]);
            System.out.println("Colour: " + intDraw[intCount][3]);
            //Increase intCount
            intCount++;
        }
        theDrawerRoundPanel.updateDraw(intDraw, intCount);
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
