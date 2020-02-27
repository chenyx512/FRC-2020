package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class AutoIntake extends CommandBase {
  private static final double angleP = 1.0 / 60.0;

  private boolean isCanceled;
  private int targetBallCnt;
  private Control control = Control.getInstance();

  public AutoIntake(int _targetBallCnt) {
    targetBallCnt = _targetBallCnt;
    addRequirements(Robot.driveSubsystem);
    addRequirements(Robot.ballHandler);
  }

  public AutoIntake() {
    this(5);
  }

  @Override
  public void initialize() {
    isCanceled = false;
    System.out.printf("start auto intake with %d ball\n", 
        Robot.ballHandler.ballCnt);
    if (Robot.ballHandler.ballCnt == 5 && !control.isOverride()) {
      System.out.println("have 5 balls already, cancel this");
      isCanceled = true;
    } else { // actually start
      Robot.ballHandler.state = BallHandlerState.INTAKE;
    }
  }

  @Override
  public void execute() {
    if (!Robot.coprocessor.isConnected 
        || !Robot.coprocessor.isBallGood 
        || !Robot.coprocessor.isBallFound) {
      Robot.driveWithJoystick.execute();
      NetworkTableInstance.getDefault().getEntry("/drivetrain/auto_state").setString(
          "AUTO_INTAKE_NO_BALL");
      return;
    }
    double targetAngle = Robot.coprocessor.ballFieldTheta;
    double currentAngle = Robot.coprocessor.fieldTheta;
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
      linearSpeed = 0.5 + 0.7 * (Robot.coprocessor.ballDis);
    
    Robot.driveSubsystem.setVelocity(linearSpeed + angleSpeed * -1, 
                                     linearSpeed + angleSpeed);
    NetworkTableInstance.getDefault().getEntry("/drivetrain/auto_state").setString(
        "AUTO_INTAKE_BALL");
  }

  @Override
  public void end(boolean interrupted) {
    Robot.ballHandler.state = BallHandlerState.IDLE;
    NetworkTableInstance.getDefault().getEntry("/drivetrain/auto_state").setString(
        "MANUAL");
  }

  @Override
  public boolean isFinished() {
    return (!control.isOverrideAutoIntake() && Robot.ballHandler.ballCnt >= targetBallCnt) 
           || !Robot.coprocessor.isConnected || !Robot.coprocessor.isBallGood
           || isCanceled;
  }
}
