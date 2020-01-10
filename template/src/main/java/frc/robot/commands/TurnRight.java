package frc.robot.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Robot;

public class TurnRight extends CommandBase {
    private final DriveSubsystem driveSubsystem;
    double time1;

    public TurnRight(DriveSubsystem _driveSubsystem) {
        driveSubsystem = _driveSubsystem;
        addRequirements(driveSubsystem);
    }


    @Override
    public void initialize() {
        time1 = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        driveSubsystem.drive.arcadeDrive(0,1);
    }

    @Override
    public boolean isFinished() {
        if (Timer.getFPGATimestamp() - time1 >= 0.5)
            return true;
        return false;
    }

    @Override
    public void end(boolean isInterrupted) {

    }
}
