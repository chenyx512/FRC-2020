package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.team254.lib.util.DriveSignal;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.util.TrajectoryFollower;


public class DriveSubsystem extends SubsystemBase {
  public enum DriveControlState {
    OPEN_LOOP,
    VELOCITY_CONTROL,
    TRAJECTORY_FOLLOWING,
  }

  public CANSparkMax leftMaster, leftSlave1, leftSlave2;
  public CANSparkMax rightMaster, rightSlave1, rightSlave2;
  public CANPIDController leftController, rightController;
  public CANEncoder leftEncoder, rightEncoder;
  private boolean isBrakeMode = false;
  private DriveControlState driveControlState = DriveControlState.OPEN_LOOP;
  TrajectoryFollower trajectoryFollower = new TrajectoryFollower();

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
    isBrakeMode = !isBrakeMode;
    setBrakeMode(!isBrakeMode);

    leftSlave1.follow(leftMaster);
    leftSlave2.follow(leftMaster);
    rightSlave1.follow(rightMaster);
    rightSlave2.follow(rightMaster);
    rightMaster.setInverted(true);

    leftController = leftMaster.getPIDController();
    rightController = rightMaster.getPIDController();
    leftEncoder = leftMaster.getEncoder();
    rightEncoder = rightMaster.getEncoder();
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    setPID(leftController);
    setPID(rightController);
  }

  @Override
  public void periodic() {
    if (driveControlState == DriveControlState.TRAJECTORY_FOLLOWING) {
      var speed = trajectoryFollower.update(Robot.coprocessor.getPose());
      setVelocity(speed.leftMetersPerSecond, speed.rightMetersPerSecond);
    }
    SmartDashboard.putNumber("left_position", leftEncoder.getPosition() / Constants.ENCODER_UNITpMETER);
    SmartDashboard.putNumber("right_position", rightEncoder.getPosition() / Constants.ENCODER_UNITpMETER);
    SmartDashboard.putNumber("right_mps", rightEncoder.getVelocity() / Constants.RPMpMPS);
    SmartDashboard.putNumber("left_mps", leftEncoder.getVelocity()/ Constants.RPMpMPS);
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(
        leftEncoder.getVelocity() / Constants.RPMpMPS,
        rightEncoder.getVelocity() / Constants.RPMpMPS
    );
  }

  public boolean isTrajectoryDone() {
    return driveControlState == DriveControlState.TRAJECTORY_FOLLOWING &&
      trajectoryFollower.isDone();
  }

  public void setTrajectory(Trajectory trajectory) {
    if (driveControlState != DriveControlState.TRAJECTORY_FOLLOWING) {
      setBrakeMode(true);
      driveControlState = DriveControlState.TRAJECTORY_FOLLOWING;
      System.out.println("enter trajectory following mode");
    }
    trajectoryFollower.startTrajectory(trajectory);
  }

  public void setVelocity(double leftMPS, double rightMPS) {
    // double LActual = leftEncoder.getVelocity() / Constants.RPMpMPS;
    // System.out.printf("L want %7.2f get %7.2f err %7.2f\n", leftMPS, LActual, leftMPS - LActual);
    
    if (driveControlState == DriveControlState.OPEN_LOOP) {
      setBrakeMode(true);
      driveControlState = DriveControlState.VELOCITY_CONTROL;
      System.out.println("enter velocity control mode");
    }

    leftController.setReference(
      leftMPS * Constants.RPMpMPS,
      ControlType.kVelocity, 
      0, 
      Constants.ks * Math.signum(leftMPS) + Constants.kv * leftMPS
    );
    rightController.setReference(
      rightMPS * Constants.RPMpMPS,
      ControlType.kVelocity, 
      0, 
      Constants.ks * Math.signum(rightMPS) + Constants.kv * rightMPS
    );
  }

  public void setOpenLoop(DriveSignal driveSignal) {
    if (driveControlState != DriveControlState.OPEN_LOOP) {
      setBrakeMode(false);
      driveControlState = DriveControlState.OPEN_LOOP;
      System.out.println("enter open loop mode");
    }
    leftMaster.set(driveSignal.getLeft());
    rightMaster.set(driveSignal.getRight());
  }

  private void setSpark(CANSparkMax spark) {
    spark.restoreFactoryDefaults();
    spark.setOpenLoopRampRate(0.5);
    spark.setClosedLoopRampRate(0.5);
  }

  private void setPID(CANPIDController controller){
    controller.setP(Constants.DRIVETRAIN_VELOCITY_GAINS.kP);
    controller.setI(0);
    controller.setFF(Constants.DRIVETRAIN_VELOCITY_GAINS.kF);
    controller.setD(Constants.DRIVETRAIN_VELOCITY_GAINS.kD);
    controller.setOutputRange(-1, 1);
  }

  private void setBrakeMode(boolean on) {
    if (isBrakeMode == on)
      return;
    isBrakeMode = on;
    IdleMode mode = on? IdleMode.kBrake : IdleMode.kCoast;
    leftMaster.setIdleMode(mode);
    rightMaster.setIdleMode(mode);
    rightSlave1.setIdleMode(mode);
    rightSlave2.setIdleMode(mode);
    leftSlave1.setIdleMode(mode);
    leftSlave2.setIdleMode(mode);
  }
}
