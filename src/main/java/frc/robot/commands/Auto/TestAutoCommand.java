package frc.robot.commands.Auto;

import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.manual.ManualIntake;
import frc.robot.commands.AutoIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.TrajectoryGoTo;

public class TestAutoCommand extends SequentialCommandGroup {
  public TestAutoCommand() {
    addCommands(new AutoShoot());
    addCommands(new AutoIntake(3));
    addCommands(new AutoShoot());
  }
}
