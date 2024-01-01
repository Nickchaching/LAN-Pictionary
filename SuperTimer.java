//Custom Timer with the ability to get time remaining

import java.awt.event.*;
import javax.swing.*;

public class SuperTimer extends Timer{
    //Variables
    long startTime;
    int timerDuration;

    //Methods
    public void start(){
        startTime = System.currentTimeMillis();
        super.start();
    }

    public long getRemainingTime(){
        return timerDuration - (System.currentTimeMillis() - startTime);
    }

    //Constructor
    public SuperTimer(int intMS, ActionListener listener){
        super(intMS, listener);
        timerDuration = intMS;
    }
}
