package frc.robot.commands.Auto;

import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.commands.manual.ManualIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.RunTrajectory;
import frc.robot.commands.TrajectoryGoTo;


public class PickTwoShootPickThreeShoot extends SequentialCommandGroup {
  public PickTwoShootPickThreeShoot() {
    addCommands(new TrajectoryGoTo(
      List.of(new Translation2d(-6.11, 2.06), new Translation2d(-6.35, 0.50)),
      new Pose2d(-5, 0, new Rotation2d(0)),
      false, Constants.DRIVE_MAX_V, Constants.DRIVE_MAX_A, 0
    ));
  }
}
