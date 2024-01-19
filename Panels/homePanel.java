//Associating under the Panels Directory (or package)
package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import javax.swing.*;

public class homePanel extends JPanel{
    //Properties
    public JLabel PictionaryLabel = new JLabel("PICTONARY");
    public JLabel EnterNameLabel = new JLabel("Enter Name");
    public JLabel StartGameLabel = new JLabel("Start Game");
    public JTextField NameField = new JTextField();
    public JButton HostGameButton = new JButton("Host Game");
    public JButton JoinGameButton = new JButton("Join Game");
    public JButton DemoGameButton = new JButton("Demo Game");


    //Methods

    //Constructor
    public homePanel(){  
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);

        PictionaryLabel.setSize(1280, 150);
        PictionaryLabel.setLocation(0, 35);
        PictionaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        PictionaryLabel.setFont(assets.fntHelvetica125);
        PictionaryLabel.setForeground(assets.clrWhite);

        EnterNameLabel.setSize(640, 50);
        EnterNameLabel.setLocation(320, 250);
        EnterNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        EnterNameLabel.setFont(assets.fntHelvetica40);
        EnterNameLabel.setForeground(assets.clrWhite);

        NameField.setSize(640, 50);
        NameField.setLocation(320, 315);
        NameField.setHorizontalAlignment(SwingConstants.CENTER);
        NameField.setFont(assets.fntHelvetica30);
        NameField.setBackground(assets.clrLightGrey);
        NameField.setBorder(null);

        StartGameLabel.setSize(640, 50);
        StartGameLabel.setLocation(320, 415);
        StartGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        StartGameLabel.setFont(assets.fntHelvetica40);
        StartGameLabel.setForeground(assets.clrWhite);

        HostGameButton.setSize(640, 50);
        HostGameButton.setLocation(320, 480);
        HostGameButton.setFont(assets.fntHelvetica30);
        HostGameButton.setBackground(assets.clrLightGrey);
        HostGameButton.setBorder(null);

        JoinGameButton.setSize(640, 50);
        JoinGameButton.setLocation(320, 550);
        JoinGameButton.setFont(assets.fntHelvetica30);
        JoinGameButton.setBackground(assets.clrLightGrey);
        JoinGameButton.setBorder(null);

        DemoGameButton.setSize(640, 50);
        DemoGameButton.setLocation(320, 620);
        DemoGameButton.setFont(assets.fntHelvetica30);
        DemoGameButton.setBackground(assets.clrLightGrey);
        DemoGameButton.setBorder(null);

        add(PictionaryLabel);
        add(EnterNameLabel);
        add(NameField);
        add(StartGameLabel);
        add(HostGameButton);
        add(JoinGameButton);
        add(DemoGameButton);
    }
}
