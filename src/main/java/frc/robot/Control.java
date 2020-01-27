package frc.robot;


import edu.wpi.first.wpilibj.Joystick;


/**
 * The operate/2nd joystick is fully optional. if that stick is not needed, set
 * it to null
 */
public class Control {
    private static Control instance;

    public static Control getInstance() {
        if (instance == null)
            synchronized (Control.class) {
                if (instance == null)
                    instance = new Control();
            }
        return instance;
    }

    public Joystick drive = new Joystick(0);

    // drivetrain starts
    public boolean isQuickTurn() {
        return drive.getRawButton(2);
    }

    public double getForwardThrottle() {
        return drive.getRawAxis(1) * -1;
    }

    public double getRotationThrottle() {
        return drive.getRawAxis(0);
    }
    
    public double getSlider() {
        return (1 - drive.getRawAxis(3)) / 2;
    }
}