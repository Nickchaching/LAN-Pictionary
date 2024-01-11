package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class drawerRoundPanel extends JPanel implements ActionListener{
    //Properties
    public JButton ClearButton = new JButton("X");
    public JButton SSizeButton = new JButton("✎");
    public JButton LSizeButton = new JButton("✎");
    public JButton YellowButton = new JButton();
    public JButton GreenButton = new JButton();
    public JButton BlueButton = new JButton();
    public JButton PurpleButton = new JButton();
    public JButton RedButton = new JButton();
    public JButton OrangeButton = new JButton();
    public JButton BlackButton = new JButton();
    public JLabel ItemLabel = new JLabel("");
    public JTextArea ChatArea = new JTextArea("");
    public JTextField ChatField = new JTextField();
    public Timer theTimer = new Timer(1000/60, this);
    int intWidth = 1280;
    
    int intDraw[][] = new int[536520][4];
    int intCounter = 0;
    int intPenSize = 10;
    int intPenColour = 7;

    
    //Methods
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == theTimer){
            repaint();
        }
    }

    public String getChatField(){
        String strText = ChatField.getText();
        ChatField.setText("");
        return strText;
    }

    public void updateChatArea(String strContent){
        ChatArea.append(strContent + "\n");
    }

    public void updateItemLabel(String strContent){
        ItemLabel.setText(strContent);
    }

    public void updateTimer(double dblPercent){
        intWidth = (int)(1280 * dblPercent/100);
    }

    public int[] updateDraw(int intX, int intY){
        int intDrawData[] = new int[4];
        intDrawData[0] = intX;
        intDrawData[1] = intY;
        intDrawData[2] = intPenSize;
        intDrawData[3] = intPenColour;
        intDraw[intCounter] = intDrawData;
        intCounter++;
        return intDrawData;
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

    public void updateSize(int intSize){
        this.intPenSize = intSize;
    }

    public void updateColour(int intColour){
        this.intPenColour = intColour;
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
        add(ItemLabel);
        add(ChatArea);
        add(ChatField);

        theTimer.start();
    }
}
