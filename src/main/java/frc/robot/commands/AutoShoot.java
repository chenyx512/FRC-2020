package frc.robot.commands;


import com.team254.lib.util.TimeDelayedBoolean;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;


public class AutoShoot extends CommandBase {
  private static final double P = 1.0 / 60.0;
  private double targetAngle, currentAngle, error;
  private boolean startShooting;
  private Control control = Control.getInstance();
  private TimeDelayedBoolean isDone;

  public AutoShoot() {
    addRequirements(Robot.driveSubsystem);
    addRequirements(Robot.ballHandler);
  }

  @Override
  public void initialize() {
    System.out.println("start auto shooting");
    if(!Robot.coprocessor.isConnected || !Robot.coprocessor.isTargetGood){
      System.out.println("coprocessor and/or CV not working, cancle AutoShoot");
      this.cancel();
    } else if ((!Robot.coprocessor.isPoseGood || !Robot.coprocessor.isFieldCalibrated()) && 
               !Robot.coprocessor.isTargetFound) {
      System.out.println("field not calibrated and no target found, cancle AutoShoot");
      this.cancel();
    } else if (!Robot.coprocessor.isPoseGood) {
      System.out.println("shooting without T265 hasn't been implemented");
      this.cancel();
    } else if (Robot.ballHandler.ballCnt == 0 && !control.isOverrideShoot()) {
      System.out.println("no ball, cancle auto shoot");
      this.cancel();
    }

    isDone = new TimeDelayedBoolean(Constants.AUTO_SHOOT_HOLD_TIME);
    startShooting = false;
  }

  @Override
  public void execute() {
    targetAngle = Robot.coprocessor.targetFieldTheta;
    currentAngle = Robot.coprocessor.fieldTheta;
    targetAngle -= Constants.SHOOTER_ANGLE;

    error = ((targetAngle - currentAngle)%360+360)%360;
    if(error>180)
      error -= 360;
    
    SmartDashboard.putNumber("auto_shoot/target_angle", targetAngle);
    SmartDashboard.putNumber("auto_shoot/current_angle", currentAngle);
    SmartDashboard.putNumber("auto_shoot/error", error);
    
    double speed = P * error;
    if (Math.abs(error) > 0.5)
      speed += Math.signum(error) * 0.1;
    Robot.driveSubsystem.setVelocity(speed * -1, speed);

    if ((Math.abs(error) < Constants.MAX_SHOOT_ANGLE_ERROR && Robot.coprocessor.isTargetFound)
        || control.isOverrideShoot())
      startShooting = true;
    
    Robot.ballHandler.desiredRPM = calculateRPM();
    Robot.ballHandler.state = 
        startShooting ? BallHandlerState.SHOOT : BallHandlerState.PRESPIN;
    
    // make sure shooter turns for a while for ball to get out
    if (Robot.ballHandler.ballCnt == 0)
      isDone.update(true);
  }

  @Override
  public void end(boolean interrupted) {
    System.out.println("auto shoot end");
    Robot.ballHandler.state = BallHandlerState.IDLE;
  }

  public boolean isFinished() {
    return isDone.get() && !control.isOverrideShoot();
  }

  private double calculateRPM() {
    double dis = Robot.coprocessor.targetDis;
    if (dis < 3.8)
      return 5400;
    if (dis < 5)
      return 4800 + (5 - dis) * 250;
    if (dis < 7)
      return 4800 + (dis - 5) * 225;
    return 5250 + (dis - 7) * 300;
  } 
}