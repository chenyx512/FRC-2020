/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

public class DriveSubsystem extends SubsystemBase {
  public WPI_TalonSRX leftMaster, rightMaster;
  public WPI_TalonSRX leftSlave, rightSlave;
  public PigeonIMU pigeon;
  
  public DifferentialDrive drive;

  public DifferentialDriveOdometry odometry;
  
  /**
   * Creates a new ExampleSubsystem.
   */
  public DriveSubsystem() {
    leftSlave = new WPI_TalonSRX(1);
    leftMaster = new WPI_TalonSRX(3);
    rightSlave = new WPI_TalonSRX(4);
    rightMaster = new WPI_TalonSRX(2);

    pigeon = new PigeonIMU(rightSlave);
    odometry = new DifferentialDriveOdometry(
      Rotation2d.fromDegrees(pigeon.getFusedHeading())
    );

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
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("leftPosition", leftMaster.getSelectedSensorPosition());
    SmartDashboard.putNumber("leftVelocity", leftMaster.getSelectedSensorVelocity());
    SmartDashboard.putNumber("rightPosition", rightMaster.getSelectedSensorPosition());
    SmartDashboard.putNumber("rightVelocity", rightMaster.getSelectedSensorVelocity());

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
  }

  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(
      leftMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter,
      rightMaster.getSelectedSensorVelocity() / Constants.kEncoderUnitPerMeter
    );
  }

  public void tankDriveVolts(double leftVolts, double rightVolts) {
    System.out.printf("set v to %2.1f %2.1f\n", leftVolts, rightVolts);
    leftMaster.setVoltage(leftVolts * 12);
    rightMaster.setVoltage(rightVolts * 12);
  }

  public void resetPose(Pose2d pose) {
    leftMaster.setSelectedSensorPosition(0);
    rightMaster.setSelectedSensorPosition(0);
    odometry.resetPosition(pose, Rotation2d.fromDegrees(pigeon.getFusedHeading()));
  }
}
