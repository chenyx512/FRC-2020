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
    private Control() {
    }

    public Joystick drive = new Joystick(0);

    // general controls
    public boolean isEStop() {
        return drive.getRawButtonPressed(1);
    }

    public boolean isOverride() {
        return drive.getRawButton(7);
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
    private static final int SHOOT_BUTTON = 5;
    private static final int INTAKE_BUTTON = 3;
    private static final int EJECT_BUTTON = 6;
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

    // POV: 0 top 45 top right, -1 nothing
    // PanelTurner
    public double getWheelSpeed() {
        if (isOverride())
            return 0;
        switch (drive.getPOV()) {
            case 90:
                return 0.3;
            case 270:
                return -0.3;
            default:
                return 0;
        }
    }
    public double getActuatorSpeed() {
        if (isOverride())
            return 0;
        switch (drive.getPOV()) {
            case 0:
                return 0.3;
            case 180:
                return -0.3;
            default:
                return 0;
        }
    }
    public boolean isPositionControl() {
        return false;
    }

    // Climber
    public double getWinchSpeed() {
        if (!isOverride())
            return 0;
        switch (drive.getPOV()) {
            case 90:
                return 1;
            case 270:
                return -1;
            default:
                return 0;
        }
    }
    public double getHookSpeed() {
        if (!isOverride())
            return 0;
        switch (drive.getPOV()) {
            case 0:
                return 0.3;
            case 180:
                return -0.3;
            default:
                return 0;
        }
    }
}