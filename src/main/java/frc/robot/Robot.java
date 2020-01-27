package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj2.command.*;

import frc.robot.commands.DriveWithJoystick;
import frc.robot.subsystems.DriveSubsystem;


public class Robot extends TimedRobot {
  public static DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static DriveWithJoystick driveWithJoystick = new DriveWithJoystick(driveSubsystem);

  @Override
  public void robotInit() {
    driveSubsystem.setDefaultCommand(driveWithJoystick);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    driveSubsystem.resetPose(
      new Pose2d(new Translation2d(0, 0), new Rotation2d(0))
    );
  }
}
