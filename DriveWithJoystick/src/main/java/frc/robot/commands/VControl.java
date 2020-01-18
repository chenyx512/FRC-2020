package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Control;
import frc.robot.Robot;

public class VControl extends InstantCommand {
  public VControl() {
    addRequirements(Robot.driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double speed = Control.getInstance().getSlider() * 2;
    SmartDashboard.putNumber("desired_speed", speed);
    Robot.driveSubsystem.outputMetersPerSecond(speed, speed);
  }
}
