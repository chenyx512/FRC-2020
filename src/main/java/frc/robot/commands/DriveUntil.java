package frc.robot.commands;

import java.util.concurrent.Callable;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class DriveUntil extends CommandBase {
  private double velocity;
  private Callable<Boolean> isDone;

  public DriveUntil(double _velocity, Callable<Boolean> _isDone) {
    addRequirements(Robot.driveSubsystem);
    isDone = _isDone;
  }

  public DriveUntil(double _velocity) {
    this(_velocity, () -> {return true;});
  }

  @Override
  public void initialize() {
    System.out.println("drive until");
  }

  @Override
  public void execute() {
    Robot.driveSubsystem.setVelocity(velocity, velocity);
  }

  @Override
  public void end(boolean interrupted) {
    System.out.println("end drive");
  }

  @Override
  public boolean isFinished() {
    try {
      return isDone.call();
    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }
  }
}
