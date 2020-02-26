package frc.robot.commands.Auto;

import java.util.List;

import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import frc.robot.Constants;

public class CriticalPoints {
  // All points follow WPI convention with origin at our target

  public static final Pose2d ourTrenchRunPreEntry = 
      new Pose2d(-2, -1.2, new Rotation2d(Math.toRadians(180)));
  public static final Pose2d ourTrenchRunEntry = 
      new Pose2d(-4.7, -1.7, new Rotation2d(Math.toRadians(180)));
  public static final Pose2d ourTrenchRunThreeBallStop = 
      new Pose2d(-8, -1.7, new Rotation2d(Math.toRadians(180)));

  public static final Pose2d barThreeBallSideEntry = 
      new Pose2d(-6.60, 3.26, new Rotation2d(Math.toRadians(-67.5)));
  public static final Pose2d barThreeBallSideStop = 
      new Pose2d(-5.78, 1.28, new Rotation2d(Math.toRadians(-67.5)));

  public static final Pose2d barTwoBallSideEntry = 
      new Pose2d(-5.15, 1.00, new Rotation2d(Math.toRadians(22.5)));
  public static final Pose2d barTwoBallSideStop = 
      new Pose2d(-5.97, 0.66, new Rotation2d(Math.toRadians(22.5)));
  
  public static Trajectory trenchPickUpThreeTrajectory;

  public static final Pose2d shootPoint = new Pose2d(-5, -1, new Rotation2d(Math.toRadians(190)));
}
