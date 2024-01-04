package Panels;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;



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
    //Variable to Determine if Mouse/Drawing Data Should be Tracked in Array
    boolean blnSizeChosen = false;
    boolean blnColourChosen = false; //If both are true, the mouse/data will be stored

    //Methods
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == theDrawerRoundPanel.ClearButton){
            System.out.println("Clear");
            //blnInitialBackground = false;
            //blnClearPressed = true;
            //System.out.println("blnInitialBackground: "+blnInitialBackground);
            //System.out.println("blnClearPressed: "+blnClearPressed);
        }
        else if(evt.getSource() == theDrawerRoundPanel.SSizeButton){
            intPenSize = 6;
            blnSizeChosen = true;
        }
        else if(evt.getSource() == theDrawerRoundPanel.LSizeButton){
            intPenSize = 10;
            blnSizeChosen = true;
        }
        else if(evt.getSource() == theDrawerRoundPanel.YellowButton){
            theDrawerRoundPanel.clrSelected = assets.clrDrawYellow;
            intPenColour = 1;
            blnColourChosen = true;
        }
        else if(evt.getSource() == theDrawerRoundPanel.GreenButton){
            theDrawerRoundPanel.clrSelected = assets.clrDrawGreen;
            intPenColour = 2;
            blnColourChosen = true;
        }
        else if(evt.getSource() == theDrawerRoundPanel.BlueButton){
            theDrawerRoundPanel.clrSelected = assets.clrDrawBlue;
            intPenColour = 3;
            blnColourChosen = true;
        }
        else if(evt.getSource() == theDrawerRoundPanel.PurpleButton){
            theDrawerRoundPanel.clrSelected = assets.clrDrawPurple;
            intPenColour = 4;
            blnColourChosen = true;
        }
        else if(evt.getSource() == theDrawerRoundPanel.RedButton){
            theDrawerRoundPanel.clrSelected = assets.clrDrawRed;
            intPenColour = 5;
            blnColourChosen = true;
        }
        else if(evt.getSource() == theDrawerRoundPanel.OrangeButton){
            theDrawerRoundPanel.clrSelected = assets.clrDrawOrange;
            intPenColour = 6;
            blnColourChosen = true;
        }
        else if(evt.getSource() == theDrawerRoundPanel.BlackButton){
            theDrawerRoundPanel.clrSelected = assets.clrDrawBlack;
            intPenColour = 7;
            blnColourChosen = true;
        }
    } 
    public void mouseDragged(MouseEvent evt){
        int intX = evt.getX();
        int intY = evt.getY();
        if(intX > 15 && intX < 800 && intY > 25 && intY < 695 && blnSizeChosen == true && blnColourChosen == true){
            //Draw on Drawing Surface of the Panel
            Graphics g = theDrawerRoundPanel.getGraphics();
            g.setColor(theDrawerRoundPanel.clrSelected);
            g.fillOval(intX, intY, intPenSize, intPenSize);
            //Save Drawing Data in Array
            intDraw[intCount][0] = intX; 
            intDraw[intCount][1] = intY; 
            intDraw[intCount][2] = intPenSize;
            intDraw[intCount][3] = intPenColour;
            System.out.println("intCount: " + intCount);
            System.out.println("X: " + intDraw[intCount][0]);
            System.out.println("Y: " + intDraw[intCount][1]);
            System.out.println("Size: " + intDraw[intCount][2]);
            System.out.println("Colour: " + intDraw[intCount][3]);
            //Increase intCount
            intCount++;
        }    
    }
    public void mouseMoved(MouseEvent evt){

    }
    //Draw Saved Data After Each Repaint
    public static void redraw(int[][] intDraw, int intCount){
        //Variables
        int intX = intDraw[intCount][0];
        int intY = intDraw[intCount][1];
        int intSize = intDraw[intCount][2];
        int intColour = intDraw[intCount][3];
        Color clrUsed = assets.clrDrawBlack; //Initialized to Prevent Error
        //Convert Colour Integer to Colour 
        if(intColour == 1){
            clrUsed = assets.clrDrawYellow;
        }
        else if(intColour == 2){
            clrUsed = assets.clrDrawGreen;
        }
        else if(intColour == 3){
            clrUsed = assets.clrDrawBlue;
        }
        else if(intColour == 4){
            clrUsed = assets.clrDrawPurple;
        }
        else if(intColour == 5){
            clrUsed = assets.clrDrawRed;
        }
        else if(intColour == 6){
            clrUsed = assets.clrDrawOrange;
        }
        else if(intColour == 7){
            clrUsed = assets.clrDrawBlack;
        }
        //Graphics
        Graphics g = theDrawerRoundPanel.getGraphics();
        g.setColor(clrUsed);
        g.fillOval(intX, intY, intSize, intSize);
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
