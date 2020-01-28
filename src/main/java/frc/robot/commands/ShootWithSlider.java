package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.subsystems.BallShooter;


public class ShootWithSlider extends CommandBase {
  BallShooter ballShooter;
  public ShootWithSlider(BallShooter _ballShooter) {
    ballShooter = _ballShooter;
    addRequirements(ballShooter);
  }

  @Override
  public void execute() {
    // double desiredRPM = 5700 * Control.getInstance().getSlider();
    // SmartDashboard.putNumber("shooter_desired_RPM", desiredRPM);
    // ballShooter.setRPM(desiredRPM);
    ballShooter.master.set(Control.getInstance().getSlider());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
