package frc.robot.commands;

import com.team254.lib.util.InterpolatingDouble;
import com.team254.lib.util.InterpolatingTreeMap;
import com.team254.lib.util.TimeDelayedBoolean;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;


public class AutoShoot extends CommandBase {
  private static final InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> 
    dis2rpm = new InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble>(100);
  public boolean innerGoal;
  private static final double angleP = 1.0 / 40.0, disP = 1;
  private double targetAngle, currentAngle, angleError;
  private double maxDis, minDis;
  private double startTime = 0;
  private boolean startShooting, isCanceled, isChangeDis, isWaitForDisStablize;
  private Control control = Control.getInstance();
  private TimeDelayedBoolean isDone, isDisStable;

  public AutoShoot(boolean _innerGoal) {
    addRequirements(Robot.driveSubsystem);
    addRequirements(Robot.ballHandler);
    innerGoal = _innerGoal;

    dis2rpm.put(new InterpolatingDouble(3.4), new InterpolatingDouble(5340.0));
    dis2rpm.put(new InterpolatingDouble(4.1), new InterpolatingDouble(5340.0));
    dis2rpm.put(new InterpolatingDouble(4.6), new InterpolatingDouble(5100.0));
    dis2rpm.put(new InterpolatingDouble(5.9), new InterpolatingDouble(4940.0));
    dis2rpm.put(new InterpolatingDouble(6.4), new InterpolatingDouble(5000.0));
    dis2rpm.put(new InterpolatingDouble(7.35), new InterpolatingDouble(5100.0));
    dis2rpm.put(new InterpolatingDouble(7.8), new InterpolatingDouble(5300.0));
    dis2rpm.put(new InterpolatingDouble(8.6), new InterpolatingDouble(5600.0));
  }

  public AutoShoot() {
    this(false);
  }

  @Override
  public void initialize() {
    System.out.printf("START auto shooting with targetTheta:%.1f, robotFieldTheta:%.1f, targetDis:%.1f, targetFound:%b, ballCnt %d\n",
        Robot.coprocessor.targetFieldTheta, Robot.coprocessor.fieldTheta, Robot.coprocessor.targetDis, 
        Robot.coprocessor.isTargetFound, Robot.ballHandler.ballCnt);
    
    isCanceled = false;
    if(!Robot.coprocessor.isConnected || !Robot.coprocessor.isTargetGood){
      System.out.println("coprocessor and/or CV not working, cancle AutoShoot");
      isCanceled = true;
    } else if ((!Robot.coprocessor.isPoseGood || !Robot.coprocessor.isFieldCalibrated()) && 
               !Robot.coprocessor.isTargetFound) {
      System.out.println("field not calibrated and no target found, cancle AutoShoot");
      isCanceled = true;
    } else if (!Robot.coprocessor.isPoseGood) {
      System.out.println("shoot without T265");
    } else if (Robot.ballHandler.ballCnt == 0 && !control.isOverrideAutoShoot()) {
      System.out.println("no ball, cancle auto shoot");
      isCanceled = true;
    }

    startTime = Timer.getFPGATimestamp();
    isDone = new TimeDelayedBoolean(Constants.AUTO_SHOOT_HOLD_TIME);
    isDisStable = new TimeDelayedBoolean(0.4);
    minDis = innerGoal ? Constants.INNER_MIN_SHOOT_DIS : Constants.OUTER_MIN_SHOOT_DIS;
    maxDis = innerGoal ? Constants.INNER_MAX_SHOOT_DIS : Constants.OUTER_MAX_SHOOT_DIS;
    if (Robot.coprocessor.isPoseGood &&
      (Robot.coprocessor.targetDis > maxDis || Robot.coprocessor.targetDis < minDis)){
      isChangeDis = true;
      System.out.println("change of distance required");
    }
    isWaitForDisStablize = false;
    startShooting = false;
    NetworkTableInstance.getDefault().getEntry("/drivetrain/auto_state").setString(
        "AUTO_SHOOT");
  }

  @Override
  public void execute() {
    targetAngle = Robot.coprocessor.targetFieldTheta;
    currentAngle = Robot.coprocessor.fieldTheta;
    targetAngle -= Constants.SHOOTER_ANGLE;
    if (!Robot.coprocessor.isPoseGood) {
      targetAngle = Robot.coprocessor.targetRelativeDirLeft;
      currentAngle = 0;
    }
    if (innerGoal)
      targetAngle -= Robot.coprocessor.innerAngleDelta;

    angleError = ((targetAngle - currentAngle)%360+360)%360;
    if(angleError>180)
      angleError -= 360;
    if(Double.isNaN(angleError))
      angleError = 0;
    
    SmartDashboard.putNumber("auto_shoot/target_angle", targetAngle);
    SmartDashboard.putNumber("auto_shoot/current_angle", currentAngle);
    SmartDashboard.putNumber("auto_shoot/error", angleError);
    
    double angleSpeed = angleP * angleError;
    if (Math.abs(angleSpeed) > 1.2)
      angleSpeed = 1.2 * Math.signum(angleSpeed);
    if (Math.abs(angleError) > 0.4)
      angleSpeed += Math.signum(angleError) * 0.08;

    if (isChangeDis 
        && Robot.coprocessor.targetDis < maxDis
        && Robot.coprocessor.targetDis > minDis) {
      isChangeDis = false;
      isWaitForDisStablize = true;
      isDisStable.update(true);
    } else if (isWaitForDisStablize && isDisStable.get()) {
      isWaitForDisStablize = false;
    }
    double linearSpeed = 0;
    if (Math.abs(angleError) < 10 && isChangeDis){
      if (Robot.coprocessor.targetDis > maxDis)
        linearSpeed = disP * (maxDis - Robot.coprocessor.targetDis) - 0.4;
      else if (Robot.coprocessor.targetDis < minDis)
        linearSpeed = disP * (minDis - Robot.coprocessor.targetDis) + 0.4;
    }
    Robot.driveSubsystem.setVelocity(linearSpeed + angleSpeed * -1, 
                                     linearSpeed + angleSpeed);

    if ((Math.abs(angleError) < Constants.MAX_SHOOT_ANGLE_ERROR 
        && Robot.coprocessor.isTargetFound
        && (!isChangeDis && !isWaitForDisStablize))
        || control.isOverrideAutoShoot())
      startShooting = true;
    
    Robot.ballHandler.desiredRPM = calculateRPM(
      innerGoal? Robot.coprocessor.targetDis:Robot.coprocessor.innerDis
    );
    Robot.ballHandler.state = 
        startShooting ? BallHandlerState.SHOOT : BallHandlerState.PRESPIN;
    
    // make sure shooter turns for a while for ball to get out
    if (Robot.ballHandler.ballCnt == 0)
      isDone.update(true);
    
    // System.out.printf("at %.2f desired %.2f actual %.2f\n",
    //     Timer.getFPGATimestamp() - startTime, Robot.ballHandler.desiredRPM,
    //     Robot.ballHandler.encoder.getVelocity());
  }

  @Override
  public void end(boolean interrupted) {
    System.out.printf("END auto shooting with targetTheta:%.1f, robotFieldTheta:%.1f, targetDis:%.1f, targetFound:%b, ballCnt %d\n",
        Robot.coprocessor.targetFieldTheta, Robot.coprocessor.fieldTheta, Robot.coprocessor.targetDis, 
        Robot.coprocessor.isTargetFound, Robot.ballHandler.ballCnt);
    Robot.ballHandler.state = BallHandlerState.IDLE;
    NetworkTableInstance.getDefault().getEntry("/drivetrain/auto_state").setString(
        "MANUAL");
  }

  public boolean isFinished() {
    if (!Robot.coprocessor.isPoseGood && !Robot.coprocessor.isTargetFound)
      return true;
    return isCanceled || (isDone.get() && !control.isOverrideAutoShoot());
  }

  private double calculateRPM(double dis) {
    return dis2rpm.getInterpolated(new InterpolatingDouble(dis)).value;
    // if (dis < 5)
    //   return 5150 - 150;
    // else 
    //   return Math.max(5550, 5150 + 200 * (dis - 5)) - 150;
  } 
}
