package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;;
import frc.robot.Robot;

public class DriveWithJoystick extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    private final Joystick joystick;

    public DriveWithJoystick(DriveSubsystem _driveSubsystem, Joystick _joystick) {
        joystick = _joystick;
        driveSubsystem = _driveSubsystem;
        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() {
        driveSubsystem.drive.curvatureDrive(Robot.joystick.getRawAxis(1),
                Robot.joystick.getRawAxis(0), Robot.joystick.getRawButton(1));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
