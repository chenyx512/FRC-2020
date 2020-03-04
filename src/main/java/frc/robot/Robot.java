package frc.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.team254.lib.util.DriveSignal;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.commands.*;
import frc.robot.commands.Auto.*;
import frc.robot.commands.manual.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class Robot extends TimedRobot {
  public static DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static BallHandler ballHandler = new BallHandler();
  public static Coprocessor coprocessor = new Coprocessor();
  public static Climber climber = new Climber();
  public static PanelTurner panelTurner = new PanelTurner();

  public static ExecutorService cocurrentExecutor = 
      Executors.newFixedThreadPool(1);
  
  public static double matchStartTime;

  public static DriveWithJoystick driveWithJoystick = new DriveWithJoystick();

  private static Control control = Control.getInstance();
  private static AutoShoot autoShoot = new AutoShoot(false);
  private static ManualShoot manualShoot = new ManualShoot();
  private static AutoIntake autoIntake = new AutoIntake();
  private static ManualIntake manualIntake = new ManualIntake();
  private static Eject eject = new Eject();

  private static PanelTurnPositionControl positionControl = 
      new PanelTurnPositionControl();

  @Override
  public void robotInit() {
    driveSubsystem.setDefaultCommand(driveWithJoystick);
    ballHandler.setDefaultCommand(new HandleBallWithJoystick());
    climber.setDefaultCommand(new ClimbWithJoystick());
    panelTurner.setDefaultCommand(new TurnPanelWithJoystick());
  }

  @Override
  public void autonomousInit() {
    Robot.ballHandler.ballCnt = 3;
    matchStartTime = Timer.getFPGATimestamp();
    new StartEndCommand(panelTurner::startTurnWheel, panelTurner::stop, panelTurner)
        .withTimeout(2).schedule();
    while (coprocessor.isConnected 
           && !coprocessor.isFieldCalibrated()
           && coprocessor.isPoseGood
           && coprocessor.isTargetGood
           && Timer.getFPGATimestamp() - matchStartTime < 3) {
      coprocessor.calibrate_field();
      Timer.delay(0.02);
    }
    if (!coprocessor.isConnected || !coprocessor.isTargetGood) {
              System.out.println("emergency move");
              new SequentialCommandGroup(
                new ManualShoot(3).withTimeout(4),
                new DriveUntil(1).withTimeout(3)
              ).schedule();
    } else if (!coprocessor.isPoseGood) {
      new SixBall().schedule();
    } else {
      new SevenBallStable().schedule();
    }
  }

  /* RobotPeriodic is called after the coresponding periodic of the stage,
  *  such as teleopPeriodic
  */ 
  @Override
  public void robotPeriodic() {
    // sequence of running: subsystems, buttons, commands

    String streamMode = "";
    if (manualShoot.isScheduled() || autoShoot.isScheduled())
      streamMode = "shoot";
    else if (manualIntake.isScheduled() || autoIntake.isScheduled())
      streamMode = "intake";
    else
      streamMode = control.isReversed()? "shoot":"intake";
    NetworkTableInstance.getDefault().getEntry("/odom/video_output").setString(streamMode);
    
    if (control.isCalibrateField())
      coprocessor.calibrate_field();

    CommandScheduler.getInstance().run();
    NetworkTableInstance.getDefault().flush();
  }

  @Override
  public void teleopPeriodic() {
    if(Control.getInstance().isEStop()){
      // make sure to use wpilib above 2020.2, otherwise there would be a bug
      CommandScheduler.getInstance().cancelAll();
      ballHandler.state = BallHandlerState.IDLE;
      driveSubsystem.setOpenLoop(new DriveSignal(0, 0, true));
      return;
    }

    // ballHandler
    if (control.isResetBallCnt())
      ballHandler.ballCnt = 0;
    if (control.isChangeInner())
      autoShoot.innerGoal ^= true;
    SmartDashboard.putBoolean("isInner", autoShoot.innerGoal);
    
    if (control.isAutoShoot() || control.isOverrideAutoShoot())
      autoShoot.schedule();
    else if (control.isAutoIntake() || control.isOverrideAutoIntake())
      autoIntake.schedule();
    else if (control.isManualShoot())
      manualShoot.schedule();
    else if(control.isManualIntake() || control.isOverrideManualIntake())
      manualIntake.schedule();
    else if(control.isEject())
      eject.schedule();

    // PanelTurner
    if (control.isPositionControl())
      positionControl.schedule();
  }
}
