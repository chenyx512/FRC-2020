package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public class Constants {
  // drivetrain physical characteristic
  public static final double kPDriveVel = 3.65;
  public static final double kTrackwidthMeters = 0.59182;
  public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackwidthMeters);
  public static final double kMaxSpeedMetersPerSecond = 0.5;
  public static final double kMaxAccelerationMetersPerSecondSquared = 0.5;

  // drivetrain control feedfroward
  public static final double ksVolts = 0.669;
  public static final double kvVoltSecondsPerMeter = 3.15;
  public static final double kaVoltSecondsSquaredPerMeter = 0.912;

  // encoder TODO tune this
  public static final double kEncoderUnitPerMeter = 3000.0;

  public static final double kRamseteB = 2;
  public static final double kRamseteZeta = 0.7;
}