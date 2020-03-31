// package frc.robot;

// import edu.wpi.first.wpilibj.Joystick;


// /**
//  * The operate/2nd joystick is fully optional. if that stick is not needed, set
//  * it to null
//  */
// public class Control {
//     private static Control instance;
//     public static Control getInstance() {
//         if (instance == null)
//             synchronized (Control.class) {
//                 if (instance == null)
//                     instance = new Control();
//             }
//         return instance;
//     }
//     private Control() {
//     }

//     public Joystick drive = new Joystick(0);
//     public Joystick second = new Joystick(1);

//     // general controls
//     public boolean isEStop() {
//         return drive.getRawButton(1) || second.getRawButtonPressed(3);
//     }

//     public boolean isOverride() {
//         return drive.getRawButton(7) || second.getRawButton(6);
//     }

//     public boolean isCalibrateField() {
//         return drive.getRawButtonPressed(11) || second.getRawButtonPressed(8);
//     }

//     // drivetrain
//     public boolean isReversed() {
//         return getSlider() < 0.5;
//     }
    
//     public boolean isQuickTurn() {
//         return drive.getRawButton(2);
//     }

//     public double getForwardThrottle() {
//         return drive.getRawAxis(1) * -1;
//     }

//     public double getRotationThrottle() {
//         return drive.getRawAxis(0);
//     }
    
//     public double getSlider() {
//         return (1 - drive.getRawAxis(3)) / 2;
//     }

//     // BallHandler
//     private static final int AUTO_SHOOT_BUTTON = 5;
//     private static final int MANUAL_SHOOT_BUTTON = 10;
//     private static final int AUTO_INTAKE_BUTTON = 4;
//     private static final int MANUAL_INTAKE_BUTTON = 3;
//     private static final int EJECT_BUTTON = 6;

//     private static final int SECOND_AUTO_SHOOT_BUTTON = 4;
//     private static final int SECOND_AUTO_INTAKE_BUTTON = 1;
//     private static final int SECOND_MANUAL_INTAKE_BUTTON = 7;
//     private static final int SECOND_EJECT_BUTTON = 2;
//     public boolean isAutoShoot() {
//         return (drive.getRawButtonPressed(AUTO_SHOOT_BUTTON) || 
//                 second.getRawButtonPressed(SECOND_AUTO_SHOOT_BUTTON))
//                 && !isOverride();
//     }
//     public boolean isOverrideAutoShoot() {
//         return (drive.getRawButton(AUTO_SHOOT_BUTTON) || 
//                 second.getRawButton(SECOND_AUTO_SHOOT_BUTTON))
//                  && isOverride();
//     }
//     public boolean isManualShoot() {
//         return drive.getRawButtonPressed(MANUAL_SHOOT_BUTTON);
//     }
//     public boolean isAutoIntake() {
//         return drive.getRawButtonPressed(AUTO_INTAKE_BUTTON)
//             || second.getRawButtonPressed(SECOND_AUTO_INTAKE_BUTTON);
//     }
//     public boolean isOverrideAutoIntake() {
//         return (drive.getRawButton(AUTO_INTAKE_BUTTON) 
//             || second.getRawButton(SECOND_AUTO_INTAKE_BUTTON))
//             && isOverride();
//     }
//     public boolean isManualIntake() {
//         return (drive.getRawButtonPressed(MANUAL_INTAKE_BUTTON) ||
//                 second.getRawButtonPressed(SECOND_MANUAL_INTAKE_BUTTON))
//                 && !isOverride();
//     }
//     public boolean isOverrideManualIntake() {
//         return (drive.getRawButton(MANUAL_INTAKE_BUTTON)
//                 || second.getRawButton(SECOND_MANUAL_INTAKE_BUTTON))
//                 && isOverride();
//     }
//     public boolean isEject() {
//         return (drive.getRawButton(EJECT_BUTTON)
//                 || second.getRawButton(SECOND_EJECT_BUTTON))
//                 && !isOverride();
//     }
//     public boolean isResetBallCnt() {
//         return (drive.getRawButton(EJECT_BUTTON) 
//                 || second.getRawButton(SECOND_EJECT_BUTTON))
//                 && isOverride();
//     }
//     public boolean isChangeInner() {
//         return drive.getRawButtonPressed(12) || second.getRawButtonPressed(5);
//     }

//     // POV: 0 top 45 top right, -1 nothing
//     // PanelTurner
//     public double getWheelSpeed() {
//         if (isOverride())
//             return 0;
//         switch (drive.getPOV()) {
//             case 90:
//                 return 0.3;
//             case 270:
//                 return -0.3;
//             default:
//                 return 0;
//         }
//     }
//     public double getActuatorSpeed() {
//         if (isOverride())
//             return 0;
//         switch (drive.getPOV()) {
//             case 0:
//                 return 0.3;
//             case 180:
//                 return -0.3;
//             default:
//                 return 0;
//         }
//     }
//     public boolean isPositionControl() {
//         return drive.getRawButtonPressed(9);
//     }

//     // Climber
//     public double getWinchSpeed() {
//         if (!isOverride())
//             return 0;
//         switch (drive.getPOV()) {
//             case 90:
//                 return 1;
//             case 270:
//                 return -1;
//             default:
//                 return 0;
//         }
//     }
//     public double getHookSpeed() {
//         if (!isOverride())
//             return 0;
//         switch (drive.getPOV()) {
//             case 0:
//                 return 0.3;
//             case 180:
//                 return -0.3;
//             default:
//                 return 0;
//         }
//     }
// }

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
    public Joystick second = new Joystick(1);

    // general controls
    public boolean isEStop() {
        return drive.getRawButton(1) || second.getRawButton(3);
    }

    public boolean isOverride() {
        return drive.getRawButton(11);
    }

    public boolean isCalibrateField() {
        return drive.getRawButtonPressed(5) || second.getRawButtonPressed(4);
    }

    // drivetrain
    public boolean isReversed() {
        return getSlider() < 0.5;
    }
    
    public boolean isQuickTurn() {
        return drive.getRawButton(3);
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
    private static final int AUTO_SHOOT_BUTTON = 4;
    private static final int MANUAL_SHOOT_BUTTON = 12;
    private static final int AUTO_INTAKE_BUTTON = 15;
    private static final int MANUAL_INTAKE_BUTTON = 2;
    private static final int EJECT_BUTTON = 16;

    private static final int SECOND_EJECT_BUTTON = 2;
    private static final int SECOND_RESET_BALL_CNT = 6;

    public boolean isAutoShoot() {
        return drive.getRawButtonPressed(AUTO_SHOOT_BUTTON)
               && !isOverride();
    }
    public boolean isOverrideAutoShoot() {
        return drive.getRawButton(AUTO_SHOOT_BUTTON) 
               && isOverride();
    }
    public boolean isManualShoot() {
        return drive.getRawButtonPressed(MANUAL_SHOOT_BUTTON);
    }
    public boolean isAutoIntake() {
        return drive.getRawButtonPressed(AUTO_INTAKE_BUTTON);
    }
    public boolean isOverrideAutoIntake() {
        return drive.getRawButton(AUTO_INTAKE_BUTTON)
               && isOverride();
    }
    public boolean isManualIntake() {
        return drive.getRawButtonPressed(MANUAL_INTAKE_BUTTON)
               && !isOverride();
    }
    public boolean isOverrideManualIntake() {
        return drive.getRawButton(MANUAL_INTAKE_BUTTON)
               && isOverride();
    }
    public boolean isEject() {
        return (drive.getRawButton(EJECT_BUTTON) && !isOverride())
               || second.getRawButton(SECOND_EJECT_BUTTON);
    }
    public boolean isResetBallCnt() {
        return (drive.getRawButton(EJECT_BUTTON) && isOverride())
                || second.getRawButton(SECOND_RESET_BALL_CNT);
    }
    public boolean isChangeInner() {
        return drive.getRawButtonPressed(13) || second.getRawButtonPressed(1);
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
        return drive.getRawButtonPressed(14);
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
                return 0.5;
            case 135:
            case 180:
                return -0.5;
            default:
                return 0;
        }
    }
}