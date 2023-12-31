//Associating under the Panels Directory (or package)
package Panels;

//Importing Graphics Dependencies
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class serverLobbyPanel extends JPanel implements ActionListener{
    //Properties
    public JLabel PlayersLabel = new JLabel("PLAYERS");
    public JLabel ThemesLabel = new JLabel("THEMES");
    public JPanel PlayersPanel = new JPanel();
    public JScrollPane PlayersScroll = new JScrollPane(PlayersPanel);
    public JLabel PlayerLabels[];
    public JButton Theme1Button = new JButton("Testing 1");
    public JButton Theme2Button = new JButton("Testing 2");
    public JButton Theme3Button = new JButton();
    public JButton Theme4Button = new JButton();
    public JButton Theme5Button = new JButton();
    public JButton Theme6Button = new JButton();
    public JButton Theme7Button = new JButton();
    public JButton Theme8Button = new JButton();
    public JButton StartGameButton = new JButton("START GAME");
    public Timer theTimer = new Timer(1000/60, this);
    private int intVelo;

    //Methods
    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == theTimer){
            if(PlayersPanel.getPreferredSize().getHeight() > 540){
                if(PlayersScroll.getViewport().getViewPosition().getY() >= PlayersPanel.getPreferredSize().getHeight() - 535){
                    intVelo = -1;
                }
                else if(PlayersScroll.getViewport().getViewPosition().getY() <= 0){
                    intVelo = 1;
                }
                PlayersScroll.getViewport().setViewPosition(new Point(0, (int)PlayersScroll.getViewport().getViewPosition().getY() + intVelo));
                repaint();
            }
        }
    }
    
    public void paintComponent(Graphics g){
        g.setColor(assets.clrBackground);
        g.fillRect(0, 0, 1280, 720);
        g.setColor(assets.clrGrey);
        g.fillRect(840, 0, 440, 720);
    }

    public void updatePlayerList(String strPlayerList[][]){
        int intCount;

        PlayersPanel.setBounds(0, 0, 680, 60 * strPlayerList.length);
        PlayersPanel.setPreferredSize(new Dimension(680, 60 * strPlayerList.length));
        PlayerLabels = new JLabel[strPlayerList.length];

        for(intCount = 0; intCount < strPlayerList.length; intCount++){
            PlayerLabels[intCount] = new JLabel(strPlayerList[intCount][1]+" at "+strPlayerList[intCount][0]);
            PlayerLabels[intCount].setSize(680,50);
            PlayerLabels[intCount].setLocation(0, intCount * 60);
            PlayerLabels[intCount].setHorizontalAlignment(SwingConstants.CENTER);
            PlayerLabels[intCount].setFont(assets.fntHelvetica30);
            PlayerLabels[intCount].setBackground(assets.clrLightGrey);
            PlayerLabels[intCount].setOpaque(true);
            PlayersPanel.add(PlayerLabels[intCount]);
        }
        
        PlayersPanel.repaint();

    }

    public void updateThemeSelection(int intTheme){
        switch(intTheme){
            case 1:
                resetThemeButtons();
                Theme1Button.setBackground(assets.clrLightGreen);
                Theme1Button.setForeground(assets.clrLightGrey);
                break;
            case 2:
                resetThemeButtons();
                Theme2Button.setBackground(assets.clrLightGreen);
                Theme2Button.setForeground(assets.clrLightGrey);
                break;
            case 3:
                resetThemeButtons();
                Theme3Button.setBackground(assets.clrLightGreen);
                Theme3Button.setForeground(assets.clrLightGrey);
                break;
            case 4:
                resetThemeButtons();
                Theme4Button.setBackground(assets.clrLightGreen);
                Theme4Button.setForeground(assets.clrLightGrey);
                break;
            case 5:
                resetThemeButtons();
                Theme5Button.setBackground(assets.clrLightGreen);
                Theme5Button.setForeground(assets.clrLightGrey);
                break;
            case 6:
                resetThemeButtons();
                Theme6Button.setBackground(assets.clrLightGreen);
                Theme6Button.setForeground(assets.clrLightGrey);
                break;
            case 7:
                resetThemeButtons();
                Theme7Button.setBackground(assets.clrLightGreen);
                Theme7Button.setForeground(assets.clrLightGrey);
                break;
            case 8:
                resetThemeButtons();
                Theme8Button.setBackground(assets.clrLightGreen);
                Theme8Button.setForeground(assets.clrLightGrey);
                break;
        }
    }

    public void resetThemeButtons(){
        Theme1Button.setBackground(assets.clrLightGrey);
        Theme1Button.setForeground(assets.clrButtonFGDefault);
        Theme2Button.setBackground(assets.clrLightGrey);
        Theme2Button.setForeground(assets.clrButtonFGDefault);
        Theme3Button.setBackground(assets.clrLightGrey);
        Theme3Button.setForeground(assets.clrButtonFGDefault);
        Theme4Button.setBackground(assets.clrLightGrey);
        Theme4Button.setForeground(assets.clrButtonFGDefault);
        Theme5Button.setBackground(assets.clrLightGrey);
        Theme5Button.setForeground(assets.clrButtonFGDefault);
        Theme6Button.setBackground(assets.clrLightGrey);
        Theme6Button.setForeground(assets.clrButtonFGDefault);
        Theme7Button.setBackground(assets.clrLightGrey);
        Theme7Button.setForeground(assets.clrButtonFGDefault);
        Theme8Button.setBackground(assets.clrLightGrey);
        Theme8Button.setForeground(assets.clrButtonFGDefault);
    }

    //Constructor
    public serverLobbyPanel(){
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);

        PlayersLabel.setSize(300, 60);
        PlayersLabel.setLocation(270, 40);
        PlayersLabel.setHorizontalAlignment(SwingConstants.CENTER);
        PlayersLabel.setFont(assets.fntHelvetica50);
        PlayersLabel.setForeground(assets.clrWhite);

        ThemesLabel.setSize(300, 60);
        ThemesLabel.setLocation(910, 40);
        ThemesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ThemesLabel.setFont(assets.fntHelvetica50);
        ThemesLabel.setForeground(assets.clrWhite);

        PlayersPanel.setBackground(assets.clrBackground);
        PlayersPanel.setLayout(null);
        PlayersScroll.setSize(new Dimension(680, 535));
        PlayersScroll.setLocation(80, 125);
        PlayersScroll.setBorder(null);
        PlayersScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        PlayersScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        Theme1Button.setSize(360, 50);
        Theme1Button.setLocation(880, 125);
        Theme1Button.setFont(assets.fntHelvetica30);
        Theme1Button.setBackground(assets.clrLightGrey);
        Theme1Button.setBorder(null);

        Theme2Button.setSize(360, 50);
        Theme2Button.setLocation(880, 185);
        Theme2Button.setFont(assets.fntHelvetica30);
        Theme2Button.setBackground(assets.clrLightGrey);
        Theme2Button.setBorder(null);
        
        Theme3Button.setSize(360, 50);
        Theme3Button.setLocation(880, 245);
        Theme3Button.setFont(assets.fntHelvetica30);
        Theme3Button.setBackground(assets.clrLightGrey);
        Theme3Button.setBorder(null);

        Theme4Button.setSize(360, 50);
        Theme4Button.setLocation(880, 305);
        Theme4Button.setFont(assets.fntHelvetica30);
        Theme4Button.setBackground(assets.clrLightGrey);
        Theme4Button.setBorder(null);

        Theme5Button.setSize(360, 50);
        Theme5Button.setLocation(880, 365);
        Theme5Button.setFont(assets.fntHelvetica30);
        Theme5Button.setBackground(assets.clrLightGrey);
        Theme5Button.setBorder(null);

        Theme6Button.setSize(360, 50);
        Theme6Button.setLocation(880, 425);
        Theme6Button.setFont(assets.fntHelvetica30);
        Theme6Button.setBackground(assets.clrLightGrey);
        Theme6Button.setBorder(null);

        Theme7Button.setSize(360, 50);
        Theme7Button.setLocation(880, 485);
        Theme7Button.setFont(assets.fntHelvetica30);
        Theme7Button.setBackground(assets.clrLightGrey);
        Theme7Button.setBorder(null);

        Theme8Button.setSize(360, 50);
        Theme8Button.setLocation(880, 545);
        Theme8Button.setFont(assets.fntHelvetica30);
        Theme8Button.setBackground(assets.clrLightGrey);
        Theme8Button.setBorder(null);

        StartGameButton.setSize(360, 50);
        StartGameButton.setLocation(880, 625);
        StartGameButton.setFont(assets.fntHelvetica30);
        StartGameButton.setBackground(assets.clrBackground);
        StartGameButton.setForeground(assets.clrLightGrey);
        StartGameButton.setBorder(null);


        add(PlayersLabel);
        add(ThemesLabel);
        add(PlayersScroll);
        add(Theme1Button);
        add(Theme2Button);
        add(Theme3Button);
        add(Theme4Button);
        add(Theme5Button);
        add(Theme6Button);
        add(Theme7Button);
        add(Theme8Button);
        add(StartGameButton);

        theTimer.start();
    }
}
