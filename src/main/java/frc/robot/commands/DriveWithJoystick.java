package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.*;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Control;


public class DriveWithJoystick extends CommandBase {
  private final DriveSubsystem driveSubsystem;
  private final Control control = Control.getInstance();

  public DriveWithJoystick(DriveSubsystem _driveSubsystem) {
    driveSubsystem = _driveSubsystem;
    addRequirements(driveSubsystem);
  }

  @Override
  public void initialize() {
    driveSubsystem.drive.setSafetyEnabled(true);
  }

  @Override
  public void execute() {
    // TODO may be better to use closed loop, may be better to square input
    driveSubsystem.drive.curvatureDrive(
      control.getForwardThrottle(),
      control.getRotationThrottle() * 
          (control.isQuickTurn()? 1:Math.signum(control.getForwardThrottle())), 
      control.isQuickTurn()
    );
  }

  @Override
  public void end(boolean interrupted) {
    driveSubsystem.drive.setSafetyEnabled(false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
