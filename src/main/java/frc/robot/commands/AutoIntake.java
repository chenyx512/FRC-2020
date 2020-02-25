package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class AutoIntake extends CommandBase {
  private static final double angleP = 1.0 / 60.0;

  private boolean isCanceled;
  private int targetBallCnt;
  private Control control = Control.getInstance();

  public AutoIntake() {
    addRequirements(Robot.driveSubsystem);
    addRequirements(Robot.ballHandler);
  }

  @Override
  public void initialize() {
    isCanceled = false;
    System.out.printf("start auto intake with %d ball\n", 
        Robot.ballHandler.ballCnt);
    if (!Robot.coprocessor.isConnected
        || !Robot.coprocessor.isBallFound) {
      System.out.println("isBallFound false, cancel this");
      isCanceled = true;
    } else if (Robot.ballHandler.ballCnt == 5 
               && !control.isOverride()) {
      System.out.println("have 5 balls already, cancel this");
      isCanceled = true;
    } else { // actually start
      targetBallCnt = Robot.ballHandler.ballCnt + 1;
      Robot.ballHandler.state = BallHandlerState.INTAKE;
    }
  }

  @Override
  public void execute() {
    double targetAngle = Robot.coprocessor.ballAngle;
    double currentAngle = Robot.coprocessor.robotTheta;
    double angleError = ((targetAngle - currentAngle)%360+360)%360;
    if(angleError>180)
      angleError -= 360;
    if(Double.isNaN(angleError))
      angleError = 0;
    
    double angleSpeed = angleP * angleError;
    if (Math.abs(angleError) > 0.5)
      angleSpeed += Math.signum(angleError) * 0.1;
    
    double linearSpeed = 0;
    if (Math.abs(angleError) < 10)
      linearSpeed = 0.6;
    
    Robot.driveSubsystem.setVelocity(linearSpeed + angleSpeed * -1, 
                                     linearSpeed + angleSpeed);
  }

  @Override
  public void end(boolean interrupted) {
    Robot.ballHandler.state = BallHandlerState.IDLE;
  }

  @Override
  public boolean isFinished() {
    return (!control.isOverrideAutoIntake() && 
                Robot.ballHandler.ballCnt >= targetBallCnt) 
           || !Robot.coprocessor.isConnected
           || !Robot.coprocessor.isBallFound;
  }
}
