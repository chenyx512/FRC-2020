package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class AutoIntake extends CommandBase {
  private Control control = Control.getInstance();
  private int maxBallCnt;

  public AutoIntake(int _maxBallCnt) {
    addRequirements(Robot.ballHandler);
    maxBallCnt = _maxBallCnt;
  }

  public AutoIntake() {
    this(5);
  }

  @Override
  public void initialize() {
    Robot.ballHandler.state = BallHandlerState.INTAKE;
  }

  @Override
  public void end(boolean interrupted) {
    Robot.ballHandler.state = BallHandlerState.IDLE;
  }

  @Override
  public boolean isFinished() {
    return Robot.ballHandler.ballCnt >= 5 && !control.isOverrideIntake();
  }
}
