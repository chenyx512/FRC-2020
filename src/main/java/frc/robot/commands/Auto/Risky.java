package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.AutoIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.DriveUntil;

public class Risky extends SequentialCommandGroup {
  public Risky() {
    addCommands(new AutoIntake(3, false).withTimeout(5));
    addCommands(new DriveUntil(2, 2, () -> {return Robot.coprocessor.isBallFound;})
        .withTimeout(1.5));
    addCommands(new DriveUntil(-1.5, -1.5, () -> {return Robot.coprocessor.isBallFound;})
        .withTimeout(1.5));
    addCommands(new AutoShoot(true).withTimeout(5));
  }
}
