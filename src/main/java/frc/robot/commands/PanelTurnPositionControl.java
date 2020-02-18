package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class PanelTurnPositionControl extends CommandBase {
  private double desiredEncoderPosition;

  public PanelTurnPositionControl() {
    addRequirements(Robot.panelTurner);
  }

  @Override
  public void initialize() {
    desiredEncoderPosition = Robot.panelTurner.wheelEncoder.getPosition() + 10;
  }

  @Override
  public void execute() {
    Robot.panelTurner.wheel.set(0.5);
    Robot.panelTurner.actuator.set(0);
  }

  @Override
  public void end(boolean interrupted) {
    Robot.panelTurner.wheel.set(0);
  }

  @Override
  public boolean isFinished() {
    return Robot.panelTurner.wheelEncoder.getPosition() < desiredEncoderPosition;
  }
}
