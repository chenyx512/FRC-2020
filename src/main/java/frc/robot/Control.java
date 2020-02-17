package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;


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
    private Control() {
    }

    public Joystick drive = new Joystick(0);

    // general controls
    public boolean isEStop() {
        return drive.getRawButtonPressed(1);
    }

    private boolean isOverride() {
        return drive.getRawButton(12);
    }

    public boolean isCalibrateField() {
        return drive.getRawButtonPressed(11);
    }

    // drivetrain
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

    // BallHandler
    private static final int SHOOT_BUTTON = 3;
    private static final int INTAKE_BUTTON = 4;
    private static final int EJECT_BUTTON = 5;
    public boolean isAutoShoot() {
        return drive.getRawButtonPressed(SHOOT_BUTTON) && !isOverride();
    }
    public boolean isOverrideShoot() {
        return drive.getRawButton(SHOOT_BUTTON) && isOverride();
    }
    public boolean isAutoIntake() {
        return drive.getRawButtonPressed(INTAKE_BUTTON) && !isOverride();
    }
    public boolean isOverrideIntake() {
        return drive.getRawButton(INTAKE_BUTTON) && isOverride();
    }
    public boolean isEject() {
        return drive.getRawButton(EJECT_BUTTON) && !isOverride();
    }
    public boolean isResetBallCnt() {
        return drive.getRawButton(EJECT_BUTTON) && isOverride();
    }

    // PanelTurner
    public double getWheelSpeed() {
        return 0;
    }
    public double getActuatorSpeed() {
        return 0;
    }
    public boolean isPositionControl() {
        return false;
    }

    // Climber
    public double getWinchSpeed() {
        return 0;
    }
    public double getHookSpeed() {
        return 0;
    }
}