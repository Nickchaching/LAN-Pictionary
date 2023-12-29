import javax.swing.JFrame;

import Panels.drawingPanel;

public class Drawing {
    //Properties
    drawingPanel theDrawPanel = new drawingPanel();
    JFrame drawFrame = new JFrame();

    //Methods

    //Constructor
    public Drawing(){


        //Initialzing the Frame
        drawFrame.setContentPane(theDrawPanel);
        drawFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawFrame.setResizable(false);
        drawFrame.pack();
        drawFrame.setVisible(true);

    }
    
    //Main Method
    public static void main(String args[]){
        new Drawing();
    }
}
