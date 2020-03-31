package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;

import java.util.ArrayList;

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
  // TODO think about state transition
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
  private TrajectoryFollower trajectoryFollower = new TrajectoryFollower();

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
      final var speed = trajectoryFollower.update(Robot.coprocessor.getPose());
      setVelocity(speed.leftMetersPerSecond, speed.rightMetersPerSecond);
    }
    SmartDashboard.putNumber("left_position", leftEncoder.getPosition() / Constants.ENCODER_UNITpMETER);
    SmartDashboard.putNumber("right_position", rightEncoder.getPosition() / Constants.ENCODER_UNITpMETER);
    SmartDashboard.putNumber("right_mps", rightEncoder.getVelocity() / Constants.RPMpMPS);
    SmartDashboard.putNumber("left_mps", leftEncoder.getVelocity()/ Constants.RPMpMPS);
    NetworkTableInstance.getDefault().getEntry("/drivetrain/state").setString(
        driveControlState.toString());
    if (Constants.SEND_ENCODER_V)
      NetworkTableInstance.getDefault().getEntry("/odom/encoder_v").setDouble(
        rightEncoder.getVelocity() / Constants.RPMpMPS / 2 +
        leftEncoder.getVelocity()/ Constants.RPMpMPS / 2
      );
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

  public double trajectoryTimeLeft() {
    if (driveControlState != DriveControlState.TRAJECTORY_FOLLOWING)
      return 0;
    return trajectoryFollower.trajectoryTimeLeft();
  }

  public void setTrajectory(final Trajectory trajectory) {
    if (driveControlState != DriveControlState.TRAJECTORY_FOLLOWING) {
      setBrakeMode(true);
      driveControlState = DriveControlState.TRAJECTORY_FOLLOWING;
      System.out.println("enter trajectory following mode");
    }
    final var states = trajectory.getStates();
    final double totalTime = trajectory.getTotalTimeSeconds();
    System.out.printf("start trajectory with time %.2f sec and %d states\n",
        totalTime, states.size());
    final double[] stateList = new double[200];
    for(int i=0;i<100;i++) {
      final int stateStep = states.size() * i / 100;
      final var coord = states.get(stateStep).poseMeters.getTranslation();
      stateList[i * 2] = coord.getX();
      stateList[i * 2 + 1] = coord.getY();
    }
    NetworkTableInstance.getDefault().getEntry("/drivetrain/trajectory").
        setDoubleArray(stateList);
    trajectoryFollower.startTrajectory(trajectory);
  }

  public void setVelocity(final double leftMPS, final double rightMPS) {
    // double LActual = leftEncoder.getVelocity() / Constants.RPMpMPS;
    // System.out.printf("L want %7.2f get %7.2f err %7.2f\n", leftMPS, LActual, leftMPS - LActual);
    System.out.printf("%f %f\n", leftMPS, rightMPS);
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

  public void setOpenLoop(final DriveSignal driveSignal) {
    if (driveControlState != DriveControlState.OPEN_LOOP) {
      driveControlState = DriveControlState.OPEN_LOOP;
      System.out.println("enter open loop mode");
    }
    setBrakeMode(driveSignal.getBrakeMode());
    leftMaster.set(driveSignal.getLeft());
    rightMaster.set(driveSignal.getRight());
  }

  private void setSpark(final CANSparkMax spark) {
    spark.restoreFactoryDefaults();
    spark.setOpenLoopRampRate(0.4);
    spark.setClosedLoopRampRate(0.4);
    // spark.enableVoltageCompensation(12.0);
    spark.setSmartCurrentLimit(40);
  }

  private void setPID(final CANPIDController controller){
    controller.setP(Constants.DRIVETRAIN_VELOCITY_GAINS.kP);
    controller.setI(0);
    controller.setFF(Constants.DRIVETRAIN_VELOCITY_GAINS.kF);
    controller.setD(Constants.DRIVETRAIN_VELOCITY_GAINS.kD);
    controller.setOutputRange(-1, 1);
  }

  private void setBrakeMode(final boolean on) {
    if (isBrakeMode == on)
      return;
    isBrakeMode = on;
    final IdleMode mode = on? IdleMode.kBrake : IdleMode.kCoast;
    leftMaster.setIdleMode(mode);
    rightMaster.setIdleMode(mode);
    rightSlave1.setIdleMode(mode);
    rightSlave2.setIdleMode(mode);
    leftSlave1.setIdleMode(mode);
    leftSlave2.setIdleMode(mode);
  }
}
