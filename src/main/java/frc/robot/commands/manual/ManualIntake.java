package frc.robot.commands.manual;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class ManualIntake extends CommandBase {
  private Control control = Control.getInstance();
  private int maxBallCnt;

  public ManualIntake(int _maxBallCnt) {
    addRequirements(Robot.ballHandler);
    maxBallCnt = _maxBallCnt;
  }

  public ManualIntake() {
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
    return Robot.ballHandler.ballCnt >= 5 && !control.isOverrideManualIntake();
  }
}
