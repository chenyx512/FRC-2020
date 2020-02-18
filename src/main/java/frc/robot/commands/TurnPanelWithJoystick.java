package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.Robot;

public class TurnPanelWithJoystick extends CommandBase {
  Control control = Control.getInstance();

  public TurnPanelWithJoystick() {
    addRequirements(Robot.panelTurner);
  }

  @Override
  public void execute() {
    Robot.panelTurner.actuator.set(control.getActuatorSpeed());
    Robot.panelTurner.wheel.set(control.getWheelSpeed());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
