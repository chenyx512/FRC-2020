package frc.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.*;
import frc.robot.commands.Auto.ShootPickupOursThreeShoot;
import frc.robot.commands.Auto.TestAutoCommand;
import frc.robot.subsystems.*;


public class Robot extends TimedRobot {
  public static DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static BallHandler ballHandler = new BallHandler();
  public static Coprocessor coprocessor = new Coprocessor();
  public static Climber climber = new Climber();
  public static PanelTurner panelTurner = new PanelTurner();

  public static ExecutorService cocurrentExecutor = Executors.newFixedThreadPool(1);
  
  private static Control control = Control.getInstance();
  private static AutoShoot autoShoot = new AutoShoot();
  private static AutoIntake autoIntake = new AutoIntake();
  private static Eject eject = new Eject();

  // private static PanelTurnPositionControl positionControl = new PanelTurnPositionControl();

  @Override
  public void robotInit() {
    driveSubsystem.setDefaultCommand(new DriveWithJoystick());
    ballHandler.setDefaultCommand(new HandleBallWithJoystick());
    climber.setDefaultCommand(new ClimbWithJoystick());
    panelTurner.setDefaultCommand(new TurnPanelWithJoystick());
  }

  @Override
  public void autonomousInit() {
    Robot.ballHandler.ballCnt = 3;
    new ShootPickupOursThreeShoot().schedule();
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
      // make sure to use wpilib above 2020.2, otherwise there would be a bug
      CommandScheduler.getInstance().cancelAll();
      return;
    }

    if (control.isCalibrateField())
      coprocessor.calibrate_field();

    // ballHandler
    if (control.isResetBallCnt())
      ballHandler.ballCnt = 0;
    
    if (control.isAutoShoot() || control.isOverrideShoot())
      autoShoot.schedule();
    else if(control.isAutoIntake() || control.isOverrideIntake())
      autoIntake.schedule();
    else if(control.isEject())
      eject.schedule();

    
    // PanelTurner
    
  }
}
