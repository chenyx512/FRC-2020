package frc.robot.commands.Auto;

import java.util.concurrent.Callable;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class PrespinAfter extends CommandBase {
  private Callable<Boolean> isStartFunc;
  private boolean isStart = false;

  public PrespinAfter(Callable<Boolean> _isStartFunc) {
    isStartFunc = _isStartFunc;
    addRequirements(Robot.ballHandler);
  }

  @Override
  public void execute() {
    try {
      if (!isStart && isStartFunc.call())
        Robot.ballHandler.state = BallHandlerState.PRESPIN;
        isStart = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void end(boolean interrupted) {
    Robot.ballHandler.state = BallHandlerState.IDLE;
  }
}
