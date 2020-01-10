package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.DriveWithJoystick;
import frc.robot.subsystems.Drivetrain;


public class Robot extends TimedRobot {
  public static Joystick joystick;
  public static Drivetrain drivetrain;

  @Override
  public void robotInit() {
    
    joystick = new Joystick(0);
    drivetrain = new Drivetrain();
    drivetrain.setDefaultCommand(new DriveWithJoystick(drivetrain, joystick));
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }
}
