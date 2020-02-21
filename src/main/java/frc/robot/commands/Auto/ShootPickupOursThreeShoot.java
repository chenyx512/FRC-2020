package frc.robot.commands.Auto;

import frc.robot.commands.*;

import java.util.List;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShootPickupOursThreeShoot extends SequentialCommandGroup {
  public ShootPickupOursThreeShoot() {
    addCommands(new AutoShoot());
    addCommands(new ParallelRaceGroup(
      new TrajectoryGoTo(
        List.of(CriticalPoints.ourTrenchRunEntry, CriticalPoints.ourTrenchRunThreeBallStop), 
        false
      ),
      new AutoIntake(3)
    ));
    addCommands(new TrajectoryGoTo(List.of(CriticalPoints.shootPoint), true));
    addCommands(new AutoShoot());
  }
}
