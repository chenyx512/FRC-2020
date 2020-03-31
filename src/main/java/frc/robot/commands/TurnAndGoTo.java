package frc.robot.commands;

import com.team254.lib.util.TimeDelayedBoolean;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class TurnAndGoTo extends CommandBase {
  private static final double angleP = 1.0 / 30.0, disP = 1.5;
  private Translation2d translation;
  private Rotation2d rotation;
  private TimeDelayedBoolean isTranslationDone;
  private double targetAngle, currentAngle, angleError, angleSpeed, linearSpeed;
  private boolean isDone;

  public TurnAndGoTo(Translation2d _translation, Rotation2d _rotation) {
    addRequirements(Robot.driveSubsystem);
    rotation = _rotation;
    translation = _translation;
    isTranslationDone = new TimeDelayedBoolean(0.3);
    isDone = false;
  }

  public TurnAndGoTo(Translation2d _translation) {
    this(_translation, null);
  }

  @Override
  public void initialize() {
    System.out.println("start turnAndGo");
  }

  @Override
  public void execute() {
    var pose = Robot.coprocessor.getPose();
    currentAngle = Robot.coprocessor.fieldTheta;
    var requiredTranslation = translation.minus(pose.getTranslation());
    if (!isTranslationDone.get()){
      targetAngle = Math.toDegrees(
          Math.atan2(requiredTranslation.getY(), requiredTranslation.getX()));
      isTranslationDone.update(requiredTranslation.getNorm() < 0.5);
    }
    else if (rotation != null)
      targetAngle = rotation.getDegrees();
    else {
      isDone = true;
      return;
    }
    
    angleError = ((targetAngle - currentAngle)%360+360)%360;
    if (angleError>180)
      angleError -= 360;
    if (Double.isNaN(angleError))
      angleError = 0;
    if (isTranslationDone.get(Timer.getFPGATimestamp() - 0.02)
        && Math.abs(angleError) < 8) {
      isDone = true;
      return;
    } 
    
    angleSpeed = angleP * angleError;
    if (Math.abs(angleSpeed) > 1.2)
      angleSpeed = 1.2 * Math.signum(angleSpeed);
    if (Math.abs(angleError) > 2)
      angleSpeed += Math.signum(angleError) * 0.2;

    linearSpeed = 0;
    if (!isTranslationDone.get() && Math.abs(angleError) < 10)
      linearSpeed = requiredTranslation.getNorm() * disP;
    if (Math.abs(linearSpeed) > 2)
      linearSpeed = Math.signum(linearSpeed) * 2;
      
    Robot.driveSubsystem.setVelocity(linearSpeed + angleSpeed * -1, 
                                     linearSpeed + angleSpeed);
  }

  @Override
  public void end(boolean interrupted) {
    System.out.println(interrupted? "interrupted TurnAndGo":"done TurnAndGo");
  }

  @Override
  public boolean isFinished() {
    if (!Robot.coprocessor.isConnected || !Robot.coprocessor.isPoseGood 
        || !Robot.coprocessor.isFieldCalibrated())
      return true;
    return isDone;
  }
}
