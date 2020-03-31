package frc.robot.commands;

import com.team254.lib.util.TimeDelayedBoolean;
import com.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class PanelTurnPositionControl extends CommandBase {
  private double desiredEncoderPosition;
  private boolean finishTurn;
  private TimeDelayedBoolean isDone;

  public PanelTurnPositionControl() {
    addRequirements(Robot.panelTurner);
    addRequirements(Robot.driveSubsystem);
  }

  @Override
  public void initialize() {
    System.out.println("start position control");
    desiredEncoderPosition = Robot.panelTurner.wheelEncoder.getPosition() + 32;
    finishTurn = false;
    isDone = new TimeDelayedBoolean(Timer.getFPGATimestamp());
  }

  @Override
  public void execute() {
    Robot.panelTurner.actuator.set(0);
    finishTurn = 
        Robot.panelTurner.wheelEncoder.getPosition() > desiredEncoderPosition;
    if (!finishTurn) {
      Robot.panelTurner.wheel.set(0.24);
      Robot.driveSubsystem.setOpenLoop(new DriveSignal(0, 0, true));
    } else {
      Robot.panelTurner.wheel.set(0);
      Robot.driveSubsystem.setOpenLoop(new DriveSignal(0.05, 0.05, true));
    }
  }

  @Override
  public void end(boolean interrupted) {
    System.out.println("end position control");
    Robot.panelTurner.wheel.set(0);
  }

  @Override
  public boolean isFinished() {
    return isDone.get();
  }
}
