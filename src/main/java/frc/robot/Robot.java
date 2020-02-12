package frc.robot;


import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;

import frc.robot.commands.DriveWithJoystick;
import frc.robot.commands.ShootWithSlider;
import frc.robot.subsystems.BallHandler;
import frc.robot.subsystems.BallShooter;
import frc.robot.subsystems.Coprocessor;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.BallHandler.BallHandlerState;


public class Robot extends TimedRobot {
  public static DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static DriveWithJoystick driveWithJoystick = new DriveWithJoystick();
  public static BallHandler ballHandler = new BallHandler();
  public static Coprocessor coprocessor = new Coprocessor();
  // warning: no subsystems should call Control.getInstance() in their constructor
  private static Control control = Control.getInstance();

  @Override
  public void robotInit() {
    driveSubsystem.setDefaultCommand(driveWithJoystick);
  }

  /* RobotPeriodic is called after the coresponding periodic of the stage,
  *  such as teleopPeriodic
  */ 
  @Override
  public void robotPeriodic() {
    // sequence of running: subsystems, buttons, commands
    CommandScheduler.getInstance().run();
    NetworkTableInstance.getDefault().flush();
  }

  @Override
  public void teleopPeriodic() {
    if(Control.getInstance().isEStop()){
      CommandScheduler.getInstance().cancelAll();
      ballHandler.state = BallHandlerState.IDLE;
      return;
    }
    // BallHandler
    if (control.isIntake())
      ballHandler.state = BallHandlerState.INTAKE;
    else if (control.isShoot())
      ballHandler.state = BallHandlerState.SHOOT;
    else if(control.isEject())
      ballHandler.state = BallHandlerState.EJECT;
  }
}
