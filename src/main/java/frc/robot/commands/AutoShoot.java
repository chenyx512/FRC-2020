package frc.robot.commands;


import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;


public class AutoShoot extends CommandBase {
  private static final double P = 1.0 / 40.0;
  private double targetAngle, currentAngle, error;
  private double shutShooterTime;
  private Control control = Control.getInstance();

  public AutoShoot() {
    addRequirements(Robot.driveSubsystem);
    addRequirements(Robot.ballHandler);
  }

  @Override
  public void initialize() {
    if(!Robot.coprocessor.isConnected || !Robot.coprocessor.isTargetGood){
      System.out.println("coprocessor and/or CV not working, cancle AutoShoot");
      this.cancel();
    } else if ((!Robot.coprocessor.isPoseGood || !Robot.coprocessor.isFieldCalibrated()) && 
               !Robot.coprocessor.isTargetFound) {
      System.out.println("field not calibrated and no target found, cancle AutoShoot");
      this.cancel();
    }
    if (!Robot.coprocessor.isPoseGood) {
      System.out.println("shooting without T265 hasn't been implemented");
      this.cancel();
    }
    shutShooterTime = -1;
  }

  @Override
  public void execute() {
    targetAngle = Robot.coprocessor.targetFieldTheta;
    currentAngle = Robot.coprocessor.robotTheta;
    error = ((targetAngle - currentAngle)%360+360)%360;
    if(error>180)
      error -= 360;
    
    SmartDashboard.putNumber("auto_shoot/target_angle", targetAngle);
    SmartDashboard.putNumber("auto_shoot/current_angle", currentAngle);
    SmartDashboard.putNumber("auto_shoot/error", error);
    
    double power = P * error;
    power += Math.signum(error) * 0.15;
    Robot.driveSubsystem.drive.tankDrive(power * -1, power);
    // TODO use speed

    Robot.ballHandler.desiredRPM = calculateRPM();
    Robot.ballHandler.state = (Math.abs(error) < Constants.MAX_SHOOT_ANGLE_ERROR && Robot.coprocessor.isTargetFound) ?
        BallHandlerState.SHOOT : BallHandlerState.PRESPIN;
    
    // make sure shooter turns for a while for ball to get out
    if (Robot.ballHandler.ballCnt == 0 && shutShooterTime > 0)
      shutShooterTime = Timer.getFPGATimestamp() + Constants.AUTO_SHOOT_HOLD_TIME;
  }

  @Override
  public void end(boolean interrupted) {
    Robot.ballHandler.state = BallHandlerState.IDLE;
  }

  public boolean isFinished() {
    return shutShooterTime > 0 && Timer.getFPGATimestamp() > shutShooterTime && 
        !control.isOverrideShoot();
  }

  private double calculateRPM() {
    return Control.getInstance().getSlider() * 5700;
  }
}