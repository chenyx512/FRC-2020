package frc.robot.commands.manual;

import com.team254.lib.util.TimeDelayedBoolean;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class ManualShoot extends CommandBase {
  private int targetBallCnt, shootNum;
  private TimeDelayedBoolean isDone = new TimeDelayedBoolean(Constants.AUTO_SHOOT_HOLD_TIME);

  public ManualShoot(int _shootNum) {
    addRequirements(Robot.ballHandler);
    addRequirements(Robot.driveSubsystem);
    shootNum = _shootNum;
  }

  public ManualShoot() {
    this(1);
  }

  @Override
  public void initialize() {
    System.out.printf("manual shoot %d balls with %d balls\n",
                      shootNum, Robot.ballHandler.ballCnt);
    // if ballCnt is 0, keep shooting everything
    targetBallCnt = Robot.ballHandler.ballCnt - shootNum;
    Robot.driveSubsystem.setVelocity(0, 0);
    Robot.ballHandler.desiredRPM = 5000;
    Robot.ballHandler.state = BallHandlerState.SHOOT;
  }

  @Override
  public void execute() {
    if (Robot.ballHandler.ballCnt <= targetBallCnt)
      isDone.update(true);
  }

  @Override
  public void end(boolean interrupted) {
    Robot.ballHandler.state = BallHandlerState.IDLE;
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
