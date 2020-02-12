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
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;


public class DriveSubsystem extends SubsystemBase {
  public CANSparkMax leftMaster, leftSlave1, leftSlave2;
  public CANSparkMax rightMaster, rightSlave1, rightSlave2;
  public CANPIDController leftController, rightController;
  public CANEncoder leftEncoder, rightEncoder;
  public DifferentialDrive drive;

  /**
   * Creates a new ExampleSubsystem.
   */
  public DriveSubsystem() {
    leftMaster = new CANSparkMax(12, MotorType.kBrushless);
    leftSlave1 = new CANSparkMax(13, MotorType.kBrushless);
    leftSlave2 = new CANSparkMax(14, MotorType.kBrushless);
    rightMaster = new CANSparkMax(11, MotorType.kBrushless);
    rightSlave1 = new CANSparkMax(15, MotorType.kBrushless);
    rightSlave2 = new CANSparkMax(16, MotorType.kBrushless);

    setSpark(leftMaster);
    setSpark(leftSlave1);
    setSpark(leftSlave2);
    setSpark(rightMaster);
    setSpark(rightSlave1);
    setSpark(rightSlave2);

    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);
    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);
    rightMaster.setInverted(true);

    leftController = leftMaster.getPIDController();
    rightController = rightMaster.getPIDController();
    leftEncoder = leftMaster.getEncoder();
    rightEncoder = rightMaster.getEncoder();

    drive = new DifferentialDrive(leftMaster, rightMaster);
    drive.setRightSideInverted(false);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("left_position", leftEncoder.getPosition() / Constants.ENCODER_UNIT2METER);
    SmartDashboard.putNumber("right_position", rightEncoder.getPosition() / Constants.ENCODER_UNIT2MPS);
    SmartDashboard.putNumber("right_mps", rightEncoder.getVelocity() / Constants.ENCODER_UNIT2METER);
    SmartDashboard.putNumber("left_mps", leftEncoder.getVelocity()/ Constants.ENCODER_UNIT2MPS);
  }

  private void setSpark(CANSparkMax spark) {
    spark.restoreFactoryDefaults();
  }

  private void setPID(CANPIDController controller){
    controller.setP(Constants.SHOOTER_V_GAINS.kP);
    controller.setI(0);
    controller.setFF(Constants.SHOOTER_V_GAINS.kF);
    controller.setD(Constants.SHOOTER_V_GAINS.kD);
    controller.setOutputRange(-1, 1);
  }

  // methods below required for Ramsete Command

  // public Pose2d getPose() {
  //   return odometry.getPoseMeters();
  // }

  // public DifferentialDriveWheelSpeeds getWheelSpeeds() {
  //   return new DifferentialDriveWheelSpeeds(
  //       leftMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter,
  //       rightMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter
  //   );
  // }

  // public void outputMetersPerSecond(double leftMPS, double rightMPS) {
  //   // double leftSpeed = leftMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter * 10;
  //   // double rightSpeed = rightMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter * 10;
  //   // System.out.printf("L set %1.3f actual %1.3f err %1.3f || R set %1.3f actual %1.3f err %1.3f\n", leftMPS, leftSpeed,
  //   //     leftMPS - leftSpeed, rightMPS, rightSpeed, rightMPS - rightSpeed);
  //   // set value position diff / 100ms (see set documentation)
  //   leftMaster.set(
  //       ControlMode.Velocity, leftMPS * Constants.kEncoderUnitPerMeter / 10, 
  //       DemandType.ArbitraryFeedForward, Constants.ks * Math.signum(leftMPS) + Constants.kv * leftMPS
  //   );
  //   rightMaster.set(
  //       ControlMode.Velocity, rightMPS * Constants.kEncoderUnitPerMeter / 10,
  //       DemandType.ArbitraryFeedForward, Constants.ks * Math.signum(rightMPS) + Constants.kv * rightMPS
  //   );
  // }
}
