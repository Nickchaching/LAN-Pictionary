package Panels;


//Importing Graphics Dependencies
import java.awt.*;
import javax.swing.*;

public class drawingPanel extends JPanel{
    //Properties
    //Colors
    Color clrWhite = new Color(255, 255, 255);
    //Palette Buttons
    JButton blackButton = new JButton();
    JButton clearButton = new JButton("x");
    //Methods
    public void paintComponent(Graphics g){
        //Background of Canvas
        g.setColor(clrWhite);
        g.fillRect(20, 20, 680, 680);
    }
    //Constructor
    public drawingPanel(){
        //Panel
        setPreferredSize(new Dimension(1280, 720));
        setLayout(null);
        setBackground(assets.clrBackground);
        //Buttons
        //blackButton.setLocation(); 
        
    }
}
