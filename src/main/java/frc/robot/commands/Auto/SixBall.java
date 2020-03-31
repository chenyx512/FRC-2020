package frc.robot.commands.Auto;

import frc.robot.Robot;
import frc.robot.commands.AutoIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.DriveUntil;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class SixBall extends SequentialCommandGroup {
  public SixBall() {
    addCommands(new AutoShoot(true).withTimeout(5));
    addCommands(new DriveUntil(1.5).withTimeout(0.5));
    addCommands(new DriveUntil(1, 1, () -> {return Robot.coprocessor.isBallFound;})
        .withTimeout(1));
    addCommands(new AutoIntake(3, false).withTimeout(5));
    addCommands(new DriveUntil(-1.5).withTimeout(1));
    addCommands(new AutoShoot(false));
  }
}
