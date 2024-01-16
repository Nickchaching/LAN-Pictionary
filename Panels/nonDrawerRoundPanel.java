//Associating under the Panels Directory (or package)
package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.*;

public class nonDrawerRoundPanel extends JPanel implements ActionListener{
    //Properties
    public JLabel ItemLabel = new JLabel("");
    public JTextArea ChatArea = new JTextArea("");
    public JTextField ChatField = new JTextField();
    public JLabel CharLabel = new JLabel("0/50");
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

    public String getChatField(){
        String strText = ChatField.getText();
        ChatField.setText("");
        return strText;
    }

    public void updateChatArea(String strContent){
        String strChat[] = ChatArea.getText().split("\n");
        int intMessages = strChat.length;
        int intCount;
        if(intMessages < 11){
            if(assets.fntHelvetica13.getStringBounds(strContent, new FontRenderContext(new AffineTransform(), true, true)).getWidth() < 300){
                //Displaying New Content
                ChatArea.append(strContent + "\n");
            }
            else{
                //Displaying New Content
                String strLine1 = strContent;
                String strLine2 = "";
                while(assets.fntHelvetica13.getStringBounds(strLine1, new FontRenderContext(new AffineTransform(), true, true)).getWidth() >= 300){
                    strLine2 = strLine1.substring(strLine1.lastIndexOf(" ") + 1, strLine1.length()) + " " + strLine2;
			        strLine1 = strLine1.substring(0, strLine1.lastIndexOf(" "));
                }
                ChatArea.append(strLine1 + "\n");
                ChatArea.append(strLine2 + "\n");
            }
        }
        else if(intMessages == 11){
            if(assets.fntHelvetica13.getStringBounds(strContent, new FontRenderContext(new AffineTransform(), true, true)).getWidth() < 300){
                //Displaying New Content
                ChatArea.append(strContent + "\n");
            }
            else{
                //Shift Old Messages Up by 1
                for(intCount = 0; intCount < 10; intCount++){
                    strChat[intCount] = strChat[intCount + 1];
                }

                //Display Messages with Shift
                ChatArea.setText("");
                for(intCount = 0; intCount < 10; intCount++){
                    ChatArea.append(strChat[intCount] + "\n");
                }

                //Displaying New Content
                String strLine1 = strContent;
                String strLine2 = "";
                while(assets.fntHelvetica13.getStringBounds(strLine1, new FontRenderContext(new AffineTransform(), true, true)).getWidth() >= 300){
                    strLine2 = strLine1.substring(strLine1.lastIndexOf(" ") + 1, strLine1.length()) + " " + strLine2;
			        strLine1 = strLine1.substring(0, strLine1.lastIndexOf(" "));
                }
                ChatArea.append(strLine1 + "\n");
                ChatArea.append(strLine2 + "\n");
            }
        }
        else{
            //Shift Old Messages Up by 1
            for(intCount = 0; intCount < 11; intCount++){
                strChat[intCount] = strChat[intCount + 1];
            }

            //Display New Message
            if(assets.fntHelvetica13.getStringBounds(strContent, new FontRenderContext(new AffineTransform(), true, true)).getWidth() < 300){
                //Display Messages with Shift
                ChatArea.setText("");
                for(intCount = 0; intCount < 11; intCount++){
                    ChatArea.append(strChat[intCount] + "\n");
                }

                //Displaying New Content
                ChatArea.append(strContent + "\n");
            }
            else{
                //Shift Old Messages Up by 1 more
                for(intCount = 0; intCount < 10; intCount++){
                    strChat[intCount] = strChat[intCount + 1];
                }

                //Display Messages with Shift
                ChatArea.setText("");
                for(intCount = 0; intCount < 10; intCount++){
                    ChatArea.append(strChat[intCount] + "\n");
                }

                //Displaying New Content
                String strLine1 = strContent;
                String strLine2 = "";
                while(assets.fntHelvetica13.getStringBounds(strLine1, new FontRenderContext(new AffineTransform(), true, true)).getWidth() >= 300){
                    strLine2 = strLine1.substring(strLine1.lastIndexOf(" ") + 1, strLine1.length()) + " " + strLine2;
			        strLine1 = strLine1.substring(0, strLine1.lastIndexOf(" "));
                }
                ChatArea.append(strLine1 + "\n");
                ChatArea.append(strLine2 + "\n");
            }
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

        //Updating Chat Field
        limitChar();
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
        
        CharLabel.setSize(50, 50);
        CharLabel.setLocation(1200, 658);
        CharLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        CharLabel.setFont(assets.fntHelvetica13);

        ChatArea.setSize(350, 252);
        ChatArea.setLocation(915, 379);
        ChatArea.setBackground(assets.clrWhite);
        ChatArea.setFont(assets.fntHelvetica15);
        ChatArea.setEditable(false);

        ChatField.setSize(345, 54);
        ChatField.setLocation(910, 641);
        ChatField.setBackground(assets.clrWhite);
        ChatField.setFont(assets.fntHelvetica15);

        add(ItemLabel);
        add(CharLabel);
        add(ChatArea);
        add(ChatField);
        

        theTimer.start();
    }
}