package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj.geometry.*;

public class CriticalPoints {
  // All points follow WPI convention with origin at our target

  public static final Pose2d ourTrenchRunEntry = 
      new Pose2d(-5, -1.7, new Rotation2d(Math.toRadians(180)));
  public static final Pose2d ourTrenchRunThreeBallStop = 
      new Pose2d(-8, -1.7, new Rotation2d(Math.toRadians(180)));
}
