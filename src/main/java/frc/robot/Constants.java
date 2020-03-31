package frc.robot;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;

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
  public static final double DRIVE_MAX_V = 1.5; // 3
  public static final double INTAKE_MAX_V = 1.0;
  public static final double DRIVE_MAX_A = 1.5; //
  public static final double ks = 0.101;
  public static final double kv = 2.44;
  public static final double ka = 0.38;
  public static final DifferentialDriveVoltageConstraint driveVoltageConstraint = 
      new DifferentialDriveVoltageConstraint(
        new SimpleMotorFeedforward(ks, kv, ka),
        kDriveKinematics, 
        11
      );

  // drivetrain talons
  public static final Gains DRIVETRAIN_VELOCITY_GAINS = new Gains(
      1 / 2 * RPMpMPS, 1 / 100 * RPMpMPS, 0, 0, 0.2 * Constants.RPMpMPS, 1);
  public static final int DRIVETRAIN_VELOCITY_SLOT = 0;
  
  // shooter sparks
  public static final Gains SHOOTER_V_GAINS = new Gains(2e-4, 1e-8, 0, 1.0 / 5700, 300, 1);
  public static final double SHOOTER_KS = 0; // TODO actually tune this

  // Ramsete
  public static final double kRamseteB = 2;
  public static final double kRamseteZeta = 0.7;

  // AutoShoot
  public static final double MAX_SHOOT_ANGLE_ERROR = 1.5;
  public static final double AUTO_SHOOT_HOLD_TIME = 0.5;
  public static final double MAX_SHOOT_RPM_ERROR = 200;
  public static final double SHOOTER_ANGLE = 3.7; // deg to the left from center line
  public static final double MIN_SHOOT_GAP_TIME = 0.35; // 0.35
  public static final double MAX_SHOOTER_FREE_SPIN_TIME = 1.5;
  public static final double OUTER_MIN_SHOOT_DIS = 4.1;
  public static final double OUTER_MAX_SHOOT_DIS = 9.5;
  public static final double INNER_MIN_SHOOT_DIS = 4;
  public static final double INNER_MAX_SHOOT_DIS = 5;

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
