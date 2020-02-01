package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.MaintainAngle;
import frc.robot.commands.RecalibrateField;


/**
 * The operate/2nd joystick is fully optional. if that stick is not needed, set
 * it to null
 */
public class Control {
    private static Control instance;
    public Joystick drive = new Joystick(0);

    public static Control getInstance() {
        if (instance == null)
            synchronized (Control.class) {
                if (instance == null)
                    instance = new Control();
            }
        return instance;
    }

    private Control() {
        JoystickButton maintainAngleButton = new JoystickButton(drive, 3);
        maintainAngleButton.whileHeld(new MaintainAngle());

        JoystickButton recalibrateFieldButton = new JoystickButton(drive, 4);
        recalibrateFieldButton.whenPressed(new RecalibrateField().withTimeout(1));
    }

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
    
    public boolean isEStop() {
        return drive.getRawButton(1);
    }
}