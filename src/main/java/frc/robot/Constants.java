package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public class Constants {
  // coprocessor
  public static final boolean SEND_ENCODER_V = false;

  // encoder
  public static final double ENCODER_UNIT2METER = 11.0 / 50 * 24 / 50 * 6.25 * 0.0254;
  public static final double ENCODER_UNIT2MPS = ENCODER_UNIT2METER / 60;

  // drivetrain physical characteristic
  public static final double kPDriveVel = 3.65;
  public static final double kTrackwidthMeters = 0.59182;
  public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackwidthMeters);
  public static final double kMaxSpeedMetersPerSecond = 1;
  public static final double kMaxAccelerationMetersPerSecondSquared = 1;

  // drivetrain control feedfroward, div by 12 from characterization data which is in volts
  public static final double ks = 0.669 / 12;
  public static final double kv = 3.15 / 12;
  public static final double ka = 0.3912 / 12;

  // drivetrain talons
  public static final Gains DRIVETRAIN_VELOCITY_GAINS = new Gains(4, 0, 10, 0, 20, 1);
  public static final int DRIVETRAIN_VELOCITY_SLOT = 0;
  
  // shooter sparks
  public static final Gains SHOOTER_V_GAINS = new Gains(2e-4, 0, 0, 1.0 / 5700, 0, 1);

  // Ramsete
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

/*
motor 11 to 50
50 link 24
24 to 50
11.0 / 50 * 24 / 50 * 6.25 * 0.0254
*/