package frc.robot.commands.manual;

import com.team254.lib.util.TimeDelayedBoolean;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Control;
import frc.robot.Robot;
import frc.robot.subsystems.BallHandler.BallHandlerState;

public class ManualShoot extends CommandBase {
  private int targetBallCnt, shootNum;
  private TimeDelayedBoolean isDone;
  private double startTime;

  public ManualShoot(int _shootNum) {
    addRequirements(Robot.ballHandler);
    addRequirements(Robot.driveSubsystem);
    shootNum = _shootNum;
  }

  public ManualShoot() {
    this(1);
  }

  @Override
  public void initialize() {
    System.out.printf("manual shoot %d balls with %d balls\n",
                      shootNum, Robot.ballHandler.ballCnt);
    startTime = Timer.getFPGATimestamp();
    // if ballCnt is 0, keep shooting everything
    isDone = new TimeDelayedBoolean(Constants.AUTO_SHOOT_HOLD_TIME);
    targetBallCnt = Robot.ballHandler.ballCnt - shootNum;
    Robot.driveSubsystem.setVelocity(0, 0);
    Robot.ballHandler.state = BallHandlerState.SHOOT;
    NetworkTableInstance.getDefault().getEntry("/drivetrain/auto_state").setString(
        "MANUAL_SHOOT");
  }

  @Override
  public void execute() {
    System.out.printf("%.2f %.2f %s\n", Timer.getFPGATimestamp() - startTime,
        Robot.ballHandler.encoder.getVelocity(),
        Robot.ballHandler.shootingState.toString());
    Robot.ballHandler.desiredRPM = Control.getInstance().getSlider() * 1500 + 4100;
    if (Robot.ballHandler.ballCnt <= targetBallCnt){
      isDone.update(true);
      Robot.ballHandler.state = BallHandlerState.PRESPIN;
    }
  }

  @Override
  public void end(boolean interrupted) {
    Robot.ballHandler.state = BallHandlerState.IDLE;NetworkTableInstance.getDefault().getEntry("/drivetrain/auto_state").setString(
      "MANUAL");
  }

  @Override
  public boolean isFinished() {
    return isDone.get();
  }
}
