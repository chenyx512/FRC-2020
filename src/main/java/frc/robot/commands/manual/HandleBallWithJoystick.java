package frc.robot.commands.manual;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class HandleBallWithJoystick extends CommandBase {
  public HandleBallWithJoystick() {
    addRequirements(Robot.ballHandler);
  }

  @Override
  public void initialize() {
    Robot.ballHandler.state = BallHandlerState.IDLE;
  }

  @Override
  public void execute() {
    // TODO add overrides when everything fails
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
