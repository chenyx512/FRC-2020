package frc.robot;


import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;

import frc.robot.commands.DriveWithJoystick;
import frc.robot.commands.TurnToAngle;
import frc.robot.subsystems.BallShooter;
import frc.robot.subsystems.DriveSubsystem;


public class Robot extends TimedRobot {
  public static DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static DriveWithJoystick driveWithJoystick = new DriveWithJoystick();
  public static BallShooter ballShooter = new BallShooter();
  public static TurnToAngle turnToAngle = new TurnToAngle();

  @Override
  public void robotInit() {
    // warning: no subsystems should call Control.getInstance() in their constructor
    Control.getInstance(); // ensures keys get binded
    driveSubsystem.setDefaultCommand(driveWithJoystick);
  }

  /* RobotPeriodic is called after the coresponding periodic of the stage,
  *  such as teleopPeriodic
  */ 
  @Override
  public void robotPeriodic() {
    // sequence of running: subsystems, buttons, commands
    if(Control.getInstance().isEStop())
      turnToAngle.schedule();
      // CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
    NetworkTableInstance.getDefault().flush();
    
  }

  @Override
  public void teleopInit() {
    driveSubsystem.resetPose(
      new Pose2d(new Translation2d(0, 0), new Rotation2d(0))
    );
  }
  public void teleopPeriodic() {
    
  }
}
