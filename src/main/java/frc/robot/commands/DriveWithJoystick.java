package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.*;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Control;
import frc.robot.Robot;


public class DriveWithJoystick extends CommandBase {
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
    // double speed = Control.getInstance().getSlider() * 2.0;
    // Robot.driveSubsystem.outputMetersPerSecond(speed, speed);
    double speed = Control.getInstance().getForwardThrottle();
    speed = speed * speed * Math.signum(speed);
    Robot.driveSubsystem.drive.curvatureDrive(
      speed,
      Control.getInstance().getRotationThrottle() * 
          (Control.getInstance().isQuickTurn()? 1:Math.signum(Control.getInstance().getForwardThrottle())), 
      Control.getInstance().isQuickTurn()
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