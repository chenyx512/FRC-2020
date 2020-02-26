package frc.robot.commands.manual;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class Eject extends CommandBase {
  private Control control = Control.getInstance();

  public Eject() {
    addRequirements(Robot.ballHandler);
  }

  public void initialize() {
    Robot.ballHandler.state = BallHandlerState.EJECT;
  }

  @Override
  public void end(boolean interrupted) {
    Robot.ballHandler.state = BallHandlerState.IDLE;
  }

  @Override
  public boolean isFinished() {
    return !control.isEject();
  }
}
