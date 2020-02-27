package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.AutoIntake;
import frc.robot.commands.AutoShoot;
import frc.robot.commands.DriveUntil;

public class TestAutoCommand extends SequentialCommandGroup {
  public TestAutoCommand() {
    addCommands(new AutoShoot(true).withTimeout(5));
    addCommands(new DriveUntil(1, () -> {return Robot.coprocessor.isBallFound;})
        .withTimeout(1));
    addCommands(new AutoIntake(4).withTimeout(7));
    addCommands(new DriveUntil(-1.5, () -> {return Robot.ballHandler.ballCnt != 4;})
        .withTimeout(1));
    addCommands(new AutoShoot(true));
  }
}
