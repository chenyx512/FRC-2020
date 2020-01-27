package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import frc.robot.Constants;


public class DriveSubsystem extends SubsystemBase {
  public WPI_TalonSRX leftMaster, rightMaster, leftSlave, rightSlave;
  public PigeonIMU pigeon;

  public DifferentialDrive drive;
  public DifferentialDriveOdometry odometry;

  private NetworkTable odom_table = NetworkTableInstance.getDefault().getTable("odom");
  private NetworkTableEntry encoder_v_entry = odom_table.getEntry("encoder_v");
  private NetworkTableEntry pose_x = odom_table.getEntry("x");
  private NetworkTableEntry pose_y = odom_table.getEntry("y");
  private NetworkTableEntry pose_yaw = odom_table.getEntry("euler_1");

  /**
   * Creates a new ExampleSubsystem.
   */
  public DriveSubsystem() {
    leftSlave = new WPI_TalonSRX(1);
    leftMaster = new WPI_TalonSRX(3);
    rightSlave = new WPI_TalonSRX(4);
    rightMaster = new WPI_TalonSRX(2);

    setTalon(leftSlave);
    setTalon(leftMaster);
    setTalon(rightSlave);
    setTalon(rightMaster);

    rightMaster.setInverted(true);
    rightSlave.setInverted(true);

    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    leftMaster.setSensorPhase(true);
    rightMaster.setSensorPhase(true);

    drive = new DifferentialDrive(leftMaster, rightMaster);
    drive.setRightSideInverted(false);

    pigeon = new PigeonIMU(rightSlave);

    odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(pigeon.getFusedHeading()));
  }

  @Override
  public void periodic() {
    double left_mps = leftMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter * 10;
    double right_mps = rightMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter * 10;

    if (Constants.SEND_ENCODER_V)
      encoder_v_entry.setDouble((left_mps + right_mps) / 2);

    SmartDashboard.putNumber("leftPosition_m", 
        leftMaster.getSelectedSensorPosition() / Constants.kEncoderUnitPerMeter);
    SmartDashboard.putNumber("rightPosition_m",
        rightMaster.getSelectedSensorPosition() / Constants.kEncoderUnitPerMeter);
    SmartDashboard.putNumber("leftVelocity_mps", left_mps);
    SmartDashboard.putNumber("rightVelocity_mps", right_mps);

    SmartDashboard.putNumber("pigeon_angle", pigeon.getFusedHeading());

    odometry.update(
        Rotation2d.fromDegrees(pigeon.getFusedHeading()),
        leftMaster.getSelectedSensorPosition() / Constants.kEncoderUnitPerMeter,
        rightMaster.getSelectedSensorPosition() / Constants.kEncoderUnitPerMeter
    );

    var pose = odometry.getPoseMeters();
    SmartDashboard.putNumber("pose_x", pose.getTranslation().getX());
    SmartDashboard.putNumber("pose_y", pose.getTranslation().getY());
    SmartDashboard.putNumber("pose_theta", pose.getRotation().getDegrees());
  }

  private void setTalon(WPI_TalonSRX talon) {
    talon.configFactoryDefault();

    talon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms);
    talon.configVelocityMeasurementWindow(8);

    talon.config_kP(Constants.DRIVETRAIN_VELOCITY_SLOT, Constants.DRIVETRAIN_VELOCITY_GAINS.kP);
    talon.config_kI(Constants.DRIVETRAIN_VELOCITY_SLOT, Constants.DRIVETRAIN_VELOCITY_GAINS.kI);
    talon.config_kD(Constants.DRIVETRAIN_VELOCITY_SLOT, Constants.DRIVETRAIN_VELOCITY_GAINS.kD);
    talon.config_kF(Constants.DRIVETRAIN_VELOCITY_SLOT, Constants.DRIVETRAIN_VELOCITY_GAINS.kF);
    talon.config_IntegralZone(Constants.DRIVETRAIN_VELOCITY_SLOT, Constants.DRIVETRAIN_VELOCITY_GAINS.kIzone);
    talon.configClosedLoopPeakOutput(Constants.DRIVETRAIN_VELOCITY_SLOT,
        Constants.DRIVETRAIN_VELOCITY_GAINS.kPeakOutput);
    talon.selectProfileSlot(Constants.DRIVETRAIN_VELOCITY_SLOT, 0);
    talon.configAllowableClosedloopError(Constants.DRIVETRAIN_VELOCITY_SLOT, 0);

    // TODO ramping and current control
  }

  public void resetPose(Pose2d pose) {
    leftMaster.setSelectedSensorPosition(0);
    rightMaster.setSelectedSensorPosition(0);
    odometry.resetPosition(pose, Rotation2d.fromDegrees(pigeon.getFusedHeading()));
  }

  // methods below required for Ramsete Command

  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(
        leftMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter,
        rightMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter
    );
  }

  public void outputMetersPerSecond(double leftMPS, double rightMPS) {
    // double leftSpeed = leftMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter * 10;
    // double rightSpeed = rightMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter * 10;
    // System.out.printf("L set %1.3f actual %1.3f err %1.3f || R set %1.3f actual %1.3f err %1.3f\n", leftMPS, leftSpeed,
    //     leftMPS - leftSpeed, rightMPS, rightSpeed, rightMPS - rightSpeed);
    // set value position diff / 100ms (see set documentation)
    leftMaster.set(
        ControlMode.Velocity, leftMPS * Constants.kEncoderUnitPerMeter / 10, 
        DemandType.ArbitraryFeedForward, Constants.ks * Math.signum(leftMPS) + Constants.kv * leftMPS
    );
    rightMaster.set(
        ControlMode.Velocity, rightMPS * Constants.kEncoderUnitPerMeter / 10,
        DemandType.ArbitraryFeedForward, Constants.ks * Math.signum(rightMPS) + Constants.kv * rightMPS
    );
  }
}
