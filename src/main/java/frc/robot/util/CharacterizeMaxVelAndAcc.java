package frc.robot.util;

import com.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class CharacterizeMaxVelAndAcc extends CommandBase {
  private double lastTime, startTime;
  private double maxSpeed, lastSpeed;

  public CharacterizeMaxVelAndAcc() {
    addRequirements(Robot.driveSubsystem);
  }

  @Override
  public void initialize() {
    System.out.println("start characterization routine");
    startTime = Timer.getFPGATimestamp();
  }

  @Override
  public void execute() {
    Robot.driveSubsystem.setOpenLoop(new DriveSignal(1, 1));
    DifferentialDriveWheelSpeeds thisWheelSpeed = Robot.driveSubsystem.getWheelSpeeds();
    double meanSpeed = (thisWheelSpeed.leftMetersPerSecond + thisWheelSpeed.rightMetersPerSecond)/2;
    // if (meanSpeed <= maxSpeed && maxSpeed > ) {
    //   this.cancel();
    //   return;
    // }
    maxSpeed = meanSpeed;
    double thisTime = Timer.getFPGATimestamp();
    double thisA = (meanSpeed - lastSpeed) / (thisTime - lastTime);
    double totA = meanSpeed / (thisTime - startTime);
    System.out.printf("time %.2f this_v %.2f this_a %.2f tot_a %.2f\n", thisTime, meanSpeed, thisA, totA);    
    lastSpeed = meanSpeed;
    lastTime = thisTime;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("done");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
