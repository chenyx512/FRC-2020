package frc.robot;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public class Constants {
  // coprocessor
  public static final boolean SEND_ENCODER_V = false;

  // encoder TODO tune physical characteristics
  // 1 rot = 11 * 24 / 2500 (wheel rot) * C (m);
  // rot / m = 
  public static final double GEAR_RATIO = 11.0 / 50.0 * 24.0 / 50.0; // MotorCnt / WheelCnt;
  public static final double WHEEL_DIAMETER =  6.28 * 0.0254 * Math.PI;
  public static final double ENCODER_UNITpMETER = (1 / GEAR_RATIO) / WHEEL_DIAMETER;
  public static final double RPMpMPS = ENCODER_UNITpMETER * 60.0;

  // drivetrain physical characteristic
  public static final double kTrackwidthMeters = 0.7112 / 0.93;
  public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackwidthMeters);
  public static final double kMaxSpeedMetersPerSecond = 3;
  public static final double kMaxAccelerationMetersPerSecondSquared = 2.5;
  // drivetrain control feedfroward in volts
  public static final double ks = 0.101;
  public static final double kv = 2.44;
  public static final double ka = 0.38;

  // drivetrain talons
  public static final Gains DRIVETRAIN_VELOCITY_GAINS = new Gains(
      1 / 2 * RPMpMPS, 1 / 100 * RPMpMPS, 0, 0, 0.2 * Constants.RPMpMPS, 1);
  public static final int DRIVETRAIN_VELOCITY_SLOT = 0;
  
  // shooter sparks
  public static final Gains SHOOTER_V_GAINS = new Gains(2e-4, 0, 0, 1.0 / 5700, 0, 1);

  // Ramsete
  public static final double kRamseteB = 2;
  public static final double kRamseteZeta = 0.7;

  // AutoShoot
  public static final double MAX_SHOOT_ANGLE_ERROR = 2;
  public static final double AUTO_SHOOT_HOLD_TIME = 0.3;
  public static final double MAX_SHOOT_RPM_ERROR = 200;

  public static class Gains {
    public final double kP;
    public final double kI;
    public final double kD;
    public final double kF;
    public final double kIzone;
    public final double kPeakOutput;

    public Gains(double _kP, double _kI, double _kD, double _kF, double _kIzone, double _kPeakOutput) {
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