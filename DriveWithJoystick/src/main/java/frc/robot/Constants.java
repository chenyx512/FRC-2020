package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public class Constants {
  // encoder
  public static final double kEncoderUnitPerMeter = 3000.0;

  // drivetrain physical characteristic
  public static final double kPDriveVel = 3.65;
  public static final double kTrackwidthMeters = 0.59182;
  public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackwidthMeters);
  public static final double kMaxSpeedMetersPerSecond = 1;
  public static final double kMaxAccelerationMetersPerSecondSquared = 1;

  // drivetrain control feedfroward, div by 12 because characterization is
  // volts/unit
  public static final double ks = 0.669 / 12;
  public static final double kv = 3.15 / 12;
  public static final double ka = 0.3912 / 12;

  // talons
  public static final double DRIVETRAIN_ENCODER_K = 0.3015; // actual:read, 0.4854
  public static final double DRIVETRAIN_DEFAULT_OPEN_RAMP = 0.55;
  public static final int DRIVE_PEAK_CURRENT_DURANTION = 100; // ms
  public static final int DRIVE_PEAK_CURRENT_LIMIT = 50; // amps
  public static final int DRIVE_CONTINUOUS_CURRENT_LIMIT = 20; // amps
  public static final double TALON_MAX_VOLTAGE = 12.5;
  // public static final Gains DRIVETRAIN_VELOCITY_GAINS = new Gains(3.5, 0.01, 40, 0, 20, 1);
  public static final Gains DRIVETRAIN_VELOCITY_GAINS = new Gains(4, 0, 10, 0, 20, 1);
  public static final int DRIVETRAIN_VELOCITY_SLOT = 0;
  

  public static final double kRamseteB = 2;
  public static final double kRamseteZeta = 0.7;

  public static class Gains {
    public final double kP;
    public final double kI;
    public final double kD;
    public final double kF;
    public final int kIzone;
    public final double kPeakOutput;

    public Gains(double _kP, double _kI, double _kD, double _kF, int _kIzone, double _kPeakOutput) {
      kP = _kP;
      kI = _kI;
      kD = _kD;
      kF = _kF;
      kIzone = _kIzone;
      kPeakOutput = _kPeakOutput;
    }
  }
}