package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Robot;

public class TrajectoryGoTo extends CommandBase {
  // cubic mode
  private Pose2d finalPose;
  private List<Translation2d> interiorWaypoints;
  // cubic mode
  private List<Pose2d> waypoints;

  private Trajectory trajectory;
  private Future<Trajectory> trajectoryFuture;
  private TrajectoryConfig trajectoryConfig;
  private double calculationStartTime;

  private TrajectoryGoTo(boolean reversed) {
    addRequirements(Robot.driveSubsystem);
    trajectoryConfig = new TrajectoryConfig(Constants.kMaxSpeedMetersPerSecond,
        Constants.kMaxAccelerationMetersPerSecondSquared);
    trajectoryConfig.setKinematics(Constants.kDriveKinematics);
    trajectoryConfig.addConstraint(Constants.driveVoltageConstraint);
    trajectoryConfig.setReversed(reversed);
  }

  public TrajectoryGoTo(List<Pose2d> _waypoints, boolean reversed) {
    this(reversed);
    waypoints = new ArrayList<Pose2d>(_waypoints);
  }

  public TrajectoryGoTo(List<Translation2d> _interiorWaypoints, Pose2d _finalPose, boolean reversed) {
    this(reversed);
    interiorWaypoints =new ArrayList<Translation2d>(_interiorWaypoints);
    finalPose = _finalPose;
  }

  @Override
  public void initialize() {
    var wheelSpeed = Robot.driveSubsystem.getWheelSpeeds();
    trajectoryConfig.setStartVelocity((wheelSpeed.leftMetersPerSecond + wheelSpeed.rightMetersPerSecond) / 2);

    Pose2d startPose = Robot.coprocessor.getPose();
    System.out.println(startPose.toString());
    if (waypoints != null)
      waypoints.add(0, startPose);

    Callable<Trajectory> getTrajectory = () -> {
      // there is some memory exposure but should be fine
      if (waypoints == null)
        return TrajectoryGenerator.generateTrajectory(startPose, interiorWaypoints, finalPose, trajectoryConfig);
      else
        return TrajectoryGenerator.generateTrajectory(waypoints, trajectoryConfig);
    };

    System.out.println("start trajectory calculation");
    calculationStartTime = Timer.getFPGATimestamp();
    trajectoryFuture = Robot.cocurrentExecutor.submit(getTrajectory);
  }

  @Override
  public void execute() {
    if (trajectory == null && !trajectoryFuture.isDone())
      return;
    if (trajectory == null) {
      try {
        trajectory = trajectoryFuture.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
      System.out.printf("starting trajectory after %4.2f sec of calculation", 
          Timer.getFPGATimestamp()-calculationStartTime);
      Robot.driveSubsystem.setTrajectory(trajectory);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (interrupted && trajectory == null) 
      System.out.println("WARNING: TrajectoryGoTo interrupted while trajectory calculation thread running");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return trajectory != null && Robot.driveSubsystem.isTrajectoryDone();
  }
}
