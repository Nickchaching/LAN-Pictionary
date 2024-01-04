//Associating under the Panels Directory (or package)
package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import javax.swing.*;

public class drawerPRPanel extends JPanel{
    //Properties
    public JLabel RoundLabel = new JLabel("ROUND 1/5");
    public JLabel ChooseOneLabel = new JLabel("CHOOSE ONE TO DRAW");
    public JButton Choice1Button = new JButton("Choice 1");
    public JButton Choice2Button = new JButton("Choice 2");
    int intWidth = 1280;

    //Methods
    public void paintComponent(Graphics g){
        g.setColor(assets.clrBackground);
        g.fillRect(0, 0, 1280, 720);
        g.setColor(assets.clrLightGrey);
        g.fillRect(0, 0, 1280, 10);
        g.setColor(assets.clrRed);
        g.fillRect(0, 0, intWidth, 10);
    }

    public void initializePanel(int intRound, String strOptions[]){
        RoundLabel.setText("Round "+intRound);
        Choice1Button.setText(strOptions[0]);
        Choice2Button.setText(strOptions[1]);
    }

    public void updateTimer(double dblPercent){
        intWidth = (int)(1280 * dblPercent/100);
        repaint();
    }

    //Constructor
    public drawerPRPanel(){
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);
        
        RoundLabel.setSize(800, 50);
        RoundLabel.setLocation(25, 20);
        RoundLabel.setHorizontalAlignment(SwingConstants.LEFT);
        RoundLabel.setFont(assets.fntHelvetica30);
        RoundLabel.setForeground(assets.clrWhite);
        
        ChooseOneLabel.setSize(1000, 100);
        ChooseOneLabel.setLocation(140, 200);
        ChooseOneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ChooseOneLabel.setFont(assets.fntHelvetica75);
        ChooseOneLabel.setForeground(assets.clrWhite);

        Choice1Button.setSize(300, 150);
        Choice1Button.setLocation(240, 350);
        Choice1Button.setFont(assets.fntHelvetica30);
        Choice1Button.setBackground(assets.clrLightGrey);
        Choice1Button.setBorder(null);

        Choice2Button.setSize(300, 150);
        Choice2Button.setLocation(740, 350);
        Choice2Button.setFont(assets.fntHelvetica30);
        Choice2Button.setBackground(assets.clrLightGrey);
        Choice2Button.setBorder(null);

        add(RoundLabel);
        add(ChooseOneLabel);
        add(Choice1Button);
        add(Choice2Button);
    }
}
