//Associating under the Panels Directory (or package)
package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class nonDrawerRoundPanel extends JPanel implements ActionListener{
    //Properties
    public JLabel ItemLabel = new JLabel("");
    public JTextArea ChatArea = new JTextArea();
    public JTextField ChatField = new JTextField();
    public Timer theTimer = new Timer(1000/60, this);
    int intWidth = 1280;

    int intDraw[][] = new int[536520][4];
    int intCounter = 0;

    
    //Methods
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == theTimer){
            repaint();
        }
    }

    public void updateItemLabel(int intObjectLength){
        String strLabel = "";
        int intCount;
        for(intCount = 0; intCount < intObjectLength; intCount++){
            strLabel = strLabel + "-";
        }
        ItemLabel.setText(strLabel);
    }

    public void updateTimer(double dblPercent){
        intWidth = (int)(1280 * dblPercent/100);
    }

    public void updateDraw(int intDrawData[]){
        if(intDrawData[0] == -1){
            clearScreen();
        }
        else{
            intDraw[intCounter][0] = intDrawData[0]; 
            intDraw[intCounter][1] = intDrawData[1]; 
            intDraw[intCounter][2] = intDrawData[2];
            intDraw[intCounter][3] = intDrawData[3];
            //Increase intCount
            intCounter++;
        }
    }

    public void clearScreen(){
        //Variables
        int intRow = 0;
        int intColumn = 0;
        //Reset Array Data
        for(intRow = 0; intRow <= intCounter; intRow++){
            for(intColumn = 0; intColumn < 4; intColumn++){
                intDraw[intRow][intColumn] = 0;
            }
        }
        intCounter = 0;
    }

    public void redrawComponents(Graphics g){
        int intCount;

        for(intCount = 0; intCount < intCounter; intCount++){
            //Variables
            int intX = intDraw[intCount][0];
            int intY = intDraw[intCount][1];
            int intSize = intDraw[intCount][2];
            int intColour = intDraw[intCount][3];
            Color clrColour;

            //Convert Colour Integer to Colour 
            if(intColour == 1){
                clrColour = assets.clrDrawYellow;
            }
            else if(intColour == 2){
                clrColour = assets.clrDrawGreen;
            }
            else if(intColour == 3){
                clrColour = assets.clrDrawBlue;
            }
            else if(intColour == 4){
                clrColour = assets.clrDrawPurple;
            }
            else if(intColour == 5){
                clrColour = assets.clrDrawRed;
            }
            else if(intColour == 6){
                clrColour = assets.clrDrawOrange;
            }
            else{
                clrColour = assets.clrDrawBlack;
            }
            
            //Draw Graphics
            g.setColor(clrColour);
            g.fillOval(intX, intY, intSize, intSize);
        }
    }

    public void paintComponent(Graphics g){
        g.setColor(assets.clrBackground);
        g.fillRect(0, 0, 1280, 720);
        g.setColor(assets.clrWhite);
        g.fillRect(15, 25, 789, 680);

        
        g.setColor(assets.clrWhite);
        g.fillRect(0, 0, 1280, 10);

        //Temporary Layout
        g.fillRect(900, 25, 365, 270);
        g.fillRect(900, 305, 365, 50);
        g.fillRect(900, 365, 365, 340);


        g.setColor(assets.clrRed);
        g.fillRect(0, 0, intWidth, 10);

        //Drawing Live Update Code
        redrawComponents(g);
    }
    
    //Constructor
    public nonDrawerRoundPanel(){
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);

        ItemLabel.setSize(365, 50);
        ItemLabel.setLocation(900, 305);
        ItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ItemLabel.setFont(assets.fntHelvetica40);
        
        ChatArea.setSize(365, 266);
        ChatArea.setLocation(900, 365);
        ChatArea.setBackground(assets.clrWhite);
        ChatArea.setEditable(false);

        ChatField.setSize(345, 54);
        ChatField.setLocation(910, 641);
        ChatField.setBackground(assets.clrWhite);

        add(ItemLabel);
        add(ChatArea);
        add(ChatField);

        theTimer.start();
    }
}