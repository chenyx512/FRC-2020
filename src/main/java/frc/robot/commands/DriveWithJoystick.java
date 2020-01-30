package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.*;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Control;
import frc.robot.Robot;


public class DriveWithJoystick extends CommandBase {
  private final Control control = Control.getInstance();

  public DriveWithJoystick() {
    addRequirements(Robot.driveSubsystem);
  }

  @Override
  public void initialize() {
    Robot.driveSubsystem.drive.setSafetyEnabled(true);
  }

  @Override
  public void execute() {
    // TODO may be better to use closed loop, may be better to square input
    Robot.driveSubsystem.drive.curvatureDrive(
      control.getForwardThrottle(),
      control.getRotationThrottle() * 
          (control.isQuickTurn()? 1:Math.signum(control.getForwardThrottle())), 
      control.isQuickTurn()
    );
  }

  @Override
  public void end(boolean interrupted) {
    Robot.driveSubsystem.drive.setSafetyEnabled(false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
