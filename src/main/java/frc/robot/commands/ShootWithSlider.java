package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Control;
import frc.robot.Robot;


public class ShootWithSlider extends CommandBase {
  public ShootWithSlider() {
    // addRequirements(Robot.ballShooter);
  }

  @Override
  public void execute() {
    double power = Control.getInstance().getSlider();
    if(power < 0.02)
      power = 0;
    double desiredRPM = 5700 * power;
    SmartDashboard.putNumber("shooter_desired_RPM", desiredRPM);
    // Robot.ballShooter.setRPM(desiredRPM);
    // Robot.ballShooter.master.set(Control.getInstance().getSlider());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
