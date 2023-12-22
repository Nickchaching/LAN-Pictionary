//Associating under the Panels Directory (or package)
package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import javax.swing.*;

public class serverSelectionPanel extends JPanel{
    //Properties
    public JLabel AvailableServersLabel = new JLabel("Available Servers");
    public JButton Server1Button = new JButton();
    public JButton Server2Button = new JButton();
    public JButton Server3Button = new JButton();
    public JButton Server4Button = new JButton();
    public JButton Server5Button = new JButton();

    //Methods

    //Constructor
    public serverSelectionPanel(){
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);
        
        AvailableServersLabel.setSize(640, 100);
        AvailableServersLabel.setLocation(320, 25);
        AvailableServersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        AvailableServersLabel.setFont(assets.fntHelvetica75);
        AvailableServersLabel.setForeground(assets.clrWhite);

        Server1Button.setSize(1000, 50);
        Server1Button.setLocation(140, 175);
        Server1Button.setFont(assets.fntHelvetica30);
        Server1Button.setBackground(assets.clrLightGrey);
        Server1Button.setBorder(null);

        Server2Button.setSize(1000, 50);
        Server2Button.setLocation(140, 275);
        Server2Button.setFont(assets.fntHelvetica30);
        Server2Button.setBackground(assets.clrLightGrey);
        Server2Button.setBorder(null);

        Server3Button.setSize(1000, 50);
        Server3Button.setLocation(140, 375);
        Server3Button.setFont(assets.fntHelvetica30);
        Server3Button.setBackground(assets.clrLightGrey);
        Server3Button.setBorder(null);

        Server4Button.setSize(1000, 50);
        Server4Button.setLocation(140, 475);
        Server4Button.setFont(assets.fntHelvetica30);
        Server4Button.setBackground(assets.clrLightGrey);
        Server4Button.setBorder(null);

        Server5Button.setSize(1000, 50);
        Server5Button.setLocation(140, 575);
        Server5Button.setFont(assets.fntHelvetica30);
        Server5Button.setBackground(assets.clrLightGrey);
        Server5Button.setBorder(null);

        Server1Button.setVisible(false);
        Server2Button.setVisible(false);
        Server3Button.setVisible(false);
        Server4Button.setVisible(false);
        Server5Button.setVisible(false);

        add(AvailableServersLabel);
        add(Server1Button);
        add(Server2Button);
        add(Server3Button);
        add(Server4Button);
        add(Server5Button);
    }
}
