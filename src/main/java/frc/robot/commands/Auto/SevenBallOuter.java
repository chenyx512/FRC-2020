package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.AutoIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.DriveUntil;

public class SevenBallOuter extends SequentialCommandGroup {
  public SevenBallOuter() {
    addCommands(new AutoShoot(true).withTimeout(5));
    addCommands(new DriveUntil(1, 1, () -> {return Robot.coprocessor.isBallFound;})
        .withTimeout(1));
    addCommands(new AutoIntake(3, true).withTimeout(5));
    addCommands(new DriveUntil(0.5, 0, () ->
        {return (((Robot.coprocessor.fieldTheta - 190) % 360) + 360) % 360 < 180;})
        .withTimeout(1));
    addCommands(new AutoIntake(4, true).withTimeout(3));
    addCommands(new AutoShoot(true));
  }
}
