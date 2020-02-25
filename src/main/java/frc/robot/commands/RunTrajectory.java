package frc.robot.commands;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class RunTrajectory extends CommandBase {
  private Trajectory trajectory;

  public RunTrajectory(Trajectory _trajectory) {
    addRequirements(Robot.driveSubsystem);
    trajectory = _trajectory;
  }

  @Override
  public void initialize() {
    Robot.driveSubsystem.setTrajectory(trajectory);
  }

  @Override
  public boolean isFinished() {
    return Robot.driveSubsystem.isTrajectoryDone();
  }
}
