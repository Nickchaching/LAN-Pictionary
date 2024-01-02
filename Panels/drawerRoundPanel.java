package Panels;


//Importing Graphics Dependencies
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Graphics;

import javax.swing.*;

public class drawerRoundPanel extends JPanel implements ActionListener, MouseMotionListener{
    //Properties
    JButton ClearButton = new JButton("X");
    JButton SSizeButton = new JButton("✎");
    JButton LSizeButton = new JButton("✎");
    JButton YellowButton = new JButton();
    JButton GreenButton = new JButton();
    JButton BlueButton = new JButton();
    JButton PurpleButton = new JButton();
    JButton RedButton = new JButton();
    JButton OrangeButton = new JButton();
    JButton BlackButton = new JButton();
    public Timer theTimer = new Timer(1000/60, this);
    int intWidth = 1280;
    boolean blnInitialBackground = true;
    boolean blnClearPressed = false;
    Color clrSelected;
    
    //Methods
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == theTimer){
            repaint();
        }
        else if(evt.getSource() == ClearButton){
            System.out.println("Clear");
            blnInitialBackground = false;
            blnClearPressed = true;
            System.out.println("blnInitialBackground: "+blnInitialBackground);
            System.out.println("blnClearPressed: "+blnClearPressed);
        }
        else if(evt.getSource() == SSizeButton){
            paneltest.intPenSize = 3;
        }
        else if(evt.getSource() == LSizeButton){
            paneltest.intPenSize = 5;
        }
        else if(evt.getSource() == YellowButton){
            clrSelected = assets.clrDrawYellow;
        }
        else if(evt.getSource() == GreenButton){
            clrSelected = assets.clrDrawGreen;
        }
        else if(evt.getSource() == BlueButton){
            clrSelected = assets.clrDrawBlue;
        }
        else if(evt.getSource() == PurpleButton){
            clrSelected = assets.clrDrawPurple;
        }
        else if(evt.getSource() == RedButton){
            clrSelected = assets.clrDrawPurple;
        }
        else if(evt.getSource() == OrangeButton){
            clrSelected = assets.clrDrawOrange;
        }
        else if(evt.getSource() == BlackButton){
            clrSelected = assets.clrDrawBlack;
        }
    }
    public void mouseDragged(MouseEvent evt){
        Graphics g = getGraphics();
        g.setColor(clrSelected);
        g.fillOval(evt.getX(), evt.getY(), paneltest.intPenSize, paneltest.intPenSize);
    }
    public void mouseMoved(MouseEvent evt){

    }


    public void paintComponent(Graphics g){
        System.out.println("blnInitialBackground: "+blnInitialBackground);
        System.out.println("blnClearPressed: "+blnClearPressed);
        g.setColor(assets.clrBackground);
        g.fillRect(0, 0, 1280, 720);
        g.setColor(assets.clrWhite);
        g.fillRect(15, 25, 789, 680);

        // NOT FINISHED CLEAR BUTTON    
        //Initial Background
        //if(blnInitialBackground == true){
        //    System.out.println("Red");
        //    System.out.println("blnInitialBackground: "+blnInitialBackground);
        //    System.out.println("blnClearPressed: "+blnClearPressed);            g.setColor(assets.clrRed);
        //    g.fillRect(15, 25, 789, 680);
        //}
        //Press Clear Button
        //else if(blnClearPressed == true){
        //    g.setColor(assets.clrWhite);
        //    g.fillRect(15, 25, 789, 680);
        //    blnClearPressed = false;
        //}
        g.setColor(assets.clrLightGrey);
        g.fillRect(0, 0, 1280, 10);

        //Temporary Layout
        g.fillRect(900, 25, 365, 270);
        g.fillRect(900, 305, 365, 50);
        g.fillRect(900, 365, 365, 340);


        g.setColor(assets.clrRed);
        g.fillRect(0, 0, intWidth, 10);



        //Drawing Live Update Code
    }
    
    //Constructor
    public drawerRoundPanel(){
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);
        
        
        ClearButton.setSize(54, 54);
        ClearButton.setLocation(825, 25);
        ClearButton.setFont(assets.fntHelvetica30);
        ClearButton.setBackground(assets.clrLightGrey);
        ClearButton.setBorder(null);
        
        SSizeButton.setSize(54, 54);
        SSizeButton.setLocation(825, 93);
        SSizeButton.setFont(assets.fntHelvetica20);
        SSizeButton.setBackground(assets.clrLightGrey);
        SSizeButton.setBorder(null);

        LSizeButton.setSize(54, 54);
        LSizeButton.setLocation(825, 162);
        LSizeButton.setFont(assets.fntHelvetica30);
        LSizeButton.setBackground(assets.clrLightGrey);
        LSizeButton.setBorder(null);

        YellowButton.setSize(54, 54);
        YellowButton.setLocation(825, 231);
        YellowButton.setBackground(assets.clrDrawYellow);
        YellowButton.setBorder(null);

        GreenButton.setSize(54, 54);
        GreenButton.setLocation(825, 301);
        GreenButton.setBackground(assets.clrDrawGreen);
        GreenButton.setBorder(null);
        
        BlueButton.setSize(54, 54);
        BlueButton.setLocation(825, 371);
        BlueButton.setBackground(assets.clrDrawBlue);
        BlueButton.setBorder(null);

        PurpleButton.setSize(54, 54);
        PurpleButton.setLocation(825, 441);
        PurpleButton.setBackground(assets.clrDrawPurple);
        PurpleButton.setBorder(null);

        RedButton.setSize(54, 54);
        RedButton.setLocation(825, 511);
        RedButton.setBackground(assets.clrDrawRed);
        RedButton.setBorder(null);

        OrangeButton.setSize(54, 54);
        OrangeButton.setLocation(825, 581);
        OrangeButton.setBackground(assets.clrDrawOrange);
        OrangeButton.setBorder(null);

        BlackButton.setSize(54, 54);
        BlackButton.setLocation(825, 651);
        BlackButton.setBackground(assets.clrDrawBlack);
        BlackButton.setBorder(null);

        ClearButton.addActionListener(this);
        SSizeButton.addActionListener(this);
        LSizeButton.addActionListener(this);
        YellowButton.addActionListener(this);
        GreenButton.addActionListener(this);
        BlueButton.addActionListener(this);
        PurpleButton.addActionListener(this);
        RedButton.addActionListener(this);
        OrangeButton.addActionListener(this);
        BlackButton.addActionListener(this);

        addMouseMotionListener(this);
        
        add(ClearButton);
        add(SSizeButton);
        add(LSizeButton);
        add(YellowButton);
        add(GreenButton);
        add(BlueButton);
        add(PurpleButton);
        add(RedButton);
        add(OrangeButton);
        add(BlackButton);
    }
}
