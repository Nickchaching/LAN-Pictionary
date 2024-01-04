//Associating under the Panels Directory (or package)
package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import javax.swing.*;

public class nonDrawerPRPanel extends JPanel{
    //Properties
    public JLabel RoundLabel = new JLabel("ROUND 1/5");
    public JLabel DrawerChoosingLabel = new JLabel("Drawer is Choosing");
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

    public void initializePanel(int intRound){
        RoundLabel.setText("Round "+intRound);
    }

    public void updateTimer(double dblPercent){
        intWidth = (int)(1280 * dblPercent/100);
        repaint();
    }

    //Constructor
    public nonDrawerPRPanel(){
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);
        
        RoundLabel.setSize(800, 50);
        RoundLabel.setLocation(25, 20);
        RoundLabel.setHorizontalAlignment(SwingConstants.LEFT);
        RoundLabel.setFont(assets.fntHelvetica30);
        RoundLabel.setForeground(assets.clrWhite);
        
        DrawerChoosingLabel.setSize(1000, 100);
        DrawerChoosingLabel.setLocation(140, 300);
        DrawerChoosingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        DrawerChoosingLabel.setFont(assets.fntHelvetica75);
        DrawerChoosingLabel.setForeground(assets.clrWhite);


        add(RoundLabel);
        add(DrawerChoosingLabel);
    }
}
