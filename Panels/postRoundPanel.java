//Associating under the Panels Directory (or package)
package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import javax.swing.*;

public class postRoundPanel extends JPanel{
    //Properties
    public JLabel TheWordWasLabel = new JLabel("THE WORD WAS");
    public JButton WordLabel = new JButton("Choice 1");
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

    public void initializePanel(String strWord){
        WordLabel.setText(strWord);
    }

    public void updateTimer(double dblPercent){
        intWidth = (int)(1280 * dblPercent/100);
        repaint();
    }

    //Constructor
    public postRoundPanel(){
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);
        
        TheWordWasLabel.setSize(1000, 100);
        TheWordWasLabel.setLocation(140, 200);
        TheWordWasLabel.setHorizontalAlignment(SwingConstants.CENTER);
        TheWordWasLabel.setFont(assets.fntHelvetica75);
        TheWordWasLabel.setForeground(assets.clrWhite);

        WordLabel.setSize(300, 150);
        WordLabel.setLocation(490, 350);
        WordLabel.setFont(assets.fntHelvetica30);
        WordLabel.setBackground(assets.clrLightGrey);
        WordLabel.setBorder(null);

        add(TheWordWasLabel);
        add(WordLabel);
    }
}
