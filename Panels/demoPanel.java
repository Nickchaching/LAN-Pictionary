package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import javax.swing.event.*;

public class demoPanel extends JPanel implements ActionListener{
    //Properties
    JButton BackButton = new JButton("Back to Menu");
    //Drawing Buttons
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
    public JLabel CharLabel = new JLabel("0/50");
    public Timer theTimer = new Timer(1000/60, this);
    int intWidth = 1280;    
    int intDraw[][] = new int[536520][4];
    int intCounter = 0;
    int intPenSize = 10;
    int intPenColour = 7;
    //Chat
    public JTextArea ChatArea = new JTextArea("");
    public JTextField ChatField = new JTextField();
    //Demo Labels
    JLabel DemoLabel = new JLabel("DEMO");
    JLabel TimerLabel = new JLabel("Top bar shows remaining time.");
    JLabel DrawLabel = new JLabel("Draw something here.");
    JLabel ClearLabel = new JLabel("Clear");
    JLabel SSizeLabel = new JLabel("Small Pen");
    JLabel LSizeLabel = new JLabel("Large Pen");
    JLabel ColourLabel = new JLabel("Colour Selection");
    JLabel LeaderboardLabel = new JLabel("Earn points for guessing correctly.");
    JLabel HintLabel = new JLabel("Hints for guessing");
    JLabel ChatAreaLabel = new JLabel("Read and messages here.");
    JLabel ChatFieldLabel = new JLabel("Type a guess or message here.");
    JLabel PlayerLabel = new JLabel("Name");

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
        String strChat[] = ChatArea.getText().split("\n");
        int intMessages = strChat.length;
        int intCount;
        if(intMessages < 9){
            if(assets.fntHelvetica13.getStringBounds(strContent, new FontRenderContext(new AffineTransform(), true, true)).getWidth() < 290){
                //Displaying New Content
                ChatArea.append(strContent + "\n");
            }
            else{
                //Displaying New Content
                String strLine1 = strContent;
                String strLine2 = "";
                while(assets.fntHelvetica13.getStringBounds(strLine1, new FontRenderContext(new AffineTransform(), true, true)).getWidth() >= 290){
                    strLine2 = strLine1.substring(strLine1.lastIndexOf(" ") + 1, strLine1.length()) + " " + strLine2;
			        strLine1 = strLine1.substring(0, strLine1.lastIndexOf(" "));
                }
                ChatArea.append(strLine1 + "\n");
                ChatArea.append(strLine2 + "\n");
            }
        }
        else if(intMessages == 9){
            if(assets.fntHelvetica13.getStringBounds(strContent, new FontRenderContext(new AffineTransform(), true, true)).getWidth() < 290){
                //Displaying New Content
                ChatArea.append(strContent + "\n");
            }
            else{
                //Shift Old Messages Up by 1
                for(intCount = 0; intCount < 9; intCount++){
                    strChat[intCount] = strChat[intCount + 1];
                }

                //Display Messages with Shift
                ChatArea.setText("");
                for(intCount = 0; intCount < 9; intCount++){
                    ChatArea.append(strChat[intCount] + "\n");
                }

                //Displaying New Content
                String strLine1 = strContent;
                String strLine2 = "";
                while(assets.fntHelvetica13.getStringBounds(strLine1, new FontRenderContext(new AffineTransform(), true, true)).getWidth() >= 290){
                    strLine2 = strLine1.substring(strLine1.lastIndexOf(" ") + 1, strLine1.length()) + " " + strLine2;
			        strLine1 = strLine1.substring(0, strLine1.lastIndexOf(" "));
                }
                ChatArea.append(strLine1 + "\n");
                ChatArea.append(strLine2 + "\n");
            }
        }
        else{
            //Shift Old Messages Up by 1
            for(intCount = 0; intCount < 9; intCount++){
                strChat[intCount] = strChat[intCount + 1];
            }

            //Display New Message
            if(assets.fntHelvetica13.getStringBounds(strContent, new FontRenderContext(new AffineTransform(), true, true)).getWidth() < 290){
                //Display Messages with Shift
                ChatArea.setText("");
                for(intCount = 0; intCount < 9; intCount++){
                    ChatArea.append(strChat[intCount] + "\n");
                }

                //Displaying New Content
                ChatArea.append(strContent + "\n");
            }
            else{
                //Shift Old Messages Up by 1 more
                for(intCount = 0; intCount < 9; intCount++){
                    strChat[intCount] = strChat[intCount + 1];
                }

                //Display Messages with Shift
                ChatArea.setText("");
                for(intCount = 0; intCount < 9; intCount++){
                    ChatArea.append(strChat[intCount] + "\n");
                }

                //Displaying New Content
                String strLine1 = strContent;
                String strLine2 = "";
                while(assets.fntHelvetica13.getStringBounds(strLine1, new FontRenderContext(new AffineTransform(), true, true)).getWidth() >= 290){
                    strLine2 = strLine1.substring(strLine1.lastIndexOf(" ") + 1, strLine1.length()) + " " + strLine2;
			        strLine1 = strLine1.substring(0, strLine1.lastIndexOf(" "));
                }
                ChatArea.append(strLine1 + "\n");
                ChatArea.append(strLine2 + "\n");
            }
        }
    }

    public void updateItemLabel(String strContent){
        ItemLabel.setText(strContent);
    }



    public void updateChar(int intChange){
        int intCharCount = ChatField.getText().length();
        CharLabel.setText(intCharCount + "/50");
        if(intChange == 1 && intCharCount <= 51){
            intCharCount++;
        }
        else{
            intCharCount--;
        }
        if(intCharCount < 51){
            CharLabel.setForeground(assets.clrButtonFGDefault);
        }
        else{
            CharLabel.setForeground(assets.clrRed);
        }
    }

    public void limitChar(){
        if(ChatField.getText().length() >= 51){
            ChatField.setText(ChatField.getText().substring(0, ChatField.getText().length() - 1));
        }
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
        //Background
        g.setColor(assets.clrBackground);
        g.fillRect(0, 0, 1280, 720);
        //Drawing Area
        g.setColor(assets.clrWhite);
        g.fillRect(15, 25, 789, 680);
        //804 is end x coordinate, +65 is y
        //789 - width

        
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

        //Updating Chat Field
        limitChar();
    }
    
    //Constructor
    public demoPanel(){
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);
        
        //Buttons

        BackButton.setSize(150, 45);
        BackButton.setLocation(30, 645);
        BackButton.setFont(assets.fntHelvetica20);
        BackButton.setHorizontalAlignment(SwingConstants.CENTER);
        BackButton.setBackground(assets.clrLightGrey);
        BackButton.setBorder(null);
        
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

        //Labels

        ItemLabel.setSize(365, 50);
        ItemLabel.setLocation(900, 305);
        ItemLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ItemLabel.setFont(assets.fntHelvetica40);

        CharLabel.setSize(50, 50);
        CharLabel.setLocation(1200, 658);
        CharLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        CharLabel.setFont(assets.fntHelvetica13);

        TimerLabel.setSize(250, 30);
        TimerLabel.setLocation(30, 40);
        TimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TimerLabel.setFont(assets.fntHelvetica15);
        TimerLabel.setOpaque(true);
        TimerLabel.setBackground(assets.clrDrawYellow);

        DrawLabel.setSize(180, 30);
        DrawLabel.setLocation(30, 140);
        DrawLabel.setHorizontalAlignment(SwingConstants.CENTER);
        DrawLabel.setFont(assets.fntHelvetica15);
        DrawLabel.setOpaque(true);
        DrawLabel.setBackground(assets.clrDrawYellow);

        ClearLabel.setSize(60, 30);
        ClearLabel.setLocation(729, 40);
        ClearLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ClearLabel.setFont(assets.fntHelvetica15);
        ClearLabel.setOpaque(true);
        ClearLabel.setBackground(assets.clrDrawYellow);

        SSizeLabel.setSize(100, 30);
        SSizeLabel.setLocation(689, 105);
        SSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        SSizeLabel.setFont(assets.fntHelvetica15);
        SSizeLabel.setOpaque(true);
        SSizeLabel.setBackground(assets.clrDrawYellow);

        LSizeLabel.setSize(100, 30);
        LSizeLabel.setLocation(689, 170);
        LSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        LSizeLabel.setFont(assets.fntHelvetica15);
        LSizeLabel.setOpaque(true);
        LSizeLabel.setBackground(assets.clrDrawYellow);

        ColourLabel.setSize(140, 30);
        ColourLabel.setLocation(649, 441);
        ColourLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ColourLabel.setFont(assets.fntHelvetica15);
        ColourLabel.setOpaque(true);
        ColourLabel.setBackground(assets.clrDrawYellow);

        LeaderboardLabel.setSize(325, 30);
        LeaderboardLabel.setLocation(920, 245);
        LeaderboardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        LeaderboardLabel.setFont(assets.fntHelvetica15);
        LeaderboardLabel.setOpaque(true);
        LeaderboardLabel.setBackground(assets.clrDrawYellow);

        HintLabel.setSize(325, 30);
        HintLabel.setLocation(920, 315);
        HintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        HintLabel.setFont(assets.fntHelvetica15);
        HintLabel.setOpaque(true);
        HintLabel.setBackground(assets.clrDrawYellow);

        ChatAreaLabel.setSize(325, 30);
        ChatAreaLabel.setLocation(920, 387);
        ChatAreaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ChatAreaLabel.setFont(assets.fntHelvetica15);
        ChatAreaLabel.setOpaque(true);
        ChatAreaLabel.setBackground(assets.clrDrawYellow);

        ChatFieldLabel.setSize(325,30);
        ChatFieldLabel.setLocation(920, 590);
        ChatFieldLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ChatFieldLabel.setFont(assets.fntHelvetica15);
        ChatFieldLabel.setOpaque(true);
        ChatFieldLabel.setBackground(assets.clrDrawYellow);

        DemoLabel.setSize(789, 100);
        DemoLabel.setLocation(15, 25);
        DemoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        DemoLabel.setFont(assets.fntHelvetica50);
        DemoLabel.setBorder(null);

        PlayerLabel.setSize(325,50);
        PlayerLabel.setLocation(920, 40);
        PlayerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        PlayerLabel.setFont(assets.fntHelvetica30);
        PlayerLabel.setBackground(assets.clrLightGrey);
        PlayerLabel.setOpaque(true);

        //Chat and Panel
        
        ChatArea.setSize(350, 195);
        ChatArea.setLocation(915, 432);
        ChatArea.setBackground(assets.clrWhite);
        ChatArea.setFont(assets.fntHelvetica15);
        ChatArea.setEditable(false);

        ChatField.setSize(345, 54);
        ChatField.setLocation(910, 641);
        ChatField.setBackground(assets.clrWhite);
        ChatField.setFont(assets.fntHelvetica15);

        //Add Components to Panel

        add(BackButton);
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
        add(CharLabel);
        add(DemoLabel);
        add(TimerLabel);
        add(DrawLabel);
        add(ClearLabel);
        add(SSizeLabel);
        add(LSizeLabel);
        add(ColourLabel);
        add(LeaderboardLabel);
        add(HintLabel);
        add(ChatAreaLabel);
        add(ChatFieldLabel);
        add(PlayerLabel);

        add(ChatArea);
        add(ChatField);

        theTimer.start();
    }
}
