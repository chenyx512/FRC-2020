package frc.robot.util;

import com.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import frc.robot.Constants;

public class TrajectoryFollower {
  private RamseteController controller = new RamseteController();
  private Trajectory trajectory;
  private double startTime;

  public void startTrajectory(Trajectory _trajectory) {
    trajectory = _trajectory;
    startTime = Timer.getFPGATimestamp();
  }

  public DifferentialDriveWheelSpeeds update(Pose2d currentPose) {
    var chasisSpeed = controller.calculate(
      currentPose, 
      trajectory.sample(Timer.getFPGATimestamp() - startTime)
    );
    return Constants.kDriveKinematics.toWheelSpeeds(chasisSpeed);
  }

  public boolean isDone() {
    return Timer.getFPGATimestamp() - startTime > trajectory.getTotalTimeSeconds();
  }

  public double trajectoryTimeLeft() {
    double RV = startTime + trajectory.getTotalTimeSeconds() - Timer.getFPGATimestamp();
    return Math.max(0, RV);
  }
}
