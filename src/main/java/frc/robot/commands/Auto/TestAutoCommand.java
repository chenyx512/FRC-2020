package frc.robot.commands.Auto;

import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AutoIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.TrajectoryGoTo;

public class TestAutoCommand extends SequentialCommandGroup {
  public TestAutoCommand() {
    addCommands(new AutoShoot());
    addCommands(new ParallelRaceGroup(
      new TrajectoryGoTo(
        List.of(new Pose2d(-2.0, 0.0, new Rotation2d(180.0))),
        true
      ),
      new AutoIntake()
    ));
    addCommands(new AutoShoot());
  }
}
