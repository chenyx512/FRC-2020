package frc.robot.commands.Auto;


import frc.robot.Robot;
import frc.robot.commands.AutoIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.DriveUntil;
import frc.robot.commands.TrajectoryGoTo;
import frc.robot.commands.TurnAndGoTo;

import java.util.List;

import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class StealOneSevenBall extends SequentialCommandGroup {
  public StealOneSevenBall() {
    addCommands(new DriveUntil(1.5).withTimeout(1));
    addCommands(new AutoIntake(4, false).withTimeout(3));
    addCommands(new DriveUntil(-1.5).withTimeout(0.8));
    addCommands(new TurnAndGoTo(new Translation2d(-5.5, 3.2)).withTimeout(4));
    addCommands(new AutoShoot(false).withTimeout(5));
    addCommands(
      new ParallelRaceGroup(
        new TurnAndGoTo(new Translation2d(-5.8, 1.4)).withTimeout(5),
        new WaitUntilCommand(()->{return Robot.coprocessor.isBallFound;})
      )
    );
    addCommands(new DriveUntil(-1.5, -3, ()->{return false;}).withTimeout(1));
    addCommands(new AutoShoot(false));
  }
}
