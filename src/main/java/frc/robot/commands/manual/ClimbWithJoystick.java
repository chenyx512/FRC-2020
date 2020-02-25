package frc.robot.commands.manual;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.Robot;

public class ClimbWithJoystick extends CommandBase {
  private Control control = Control.getInstance();

  public ClimbWithJoystick() {
    addRequirements(Robot.climber);
  }

  @Override
  public void execute() {
    // TODO limit and overrides
    Robot.climber.winch.set(control.getWinchSpeed());
    Robot.climber.hook.set(control.getHookSpeed());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
