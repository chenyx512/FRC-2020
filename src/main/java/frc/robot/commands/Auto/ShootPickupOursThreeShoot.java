package frc.robot.commands.Auto;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.commands.*;
import frc.robot.commands.manual.*;

import java.util.List;

import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ShootPickupOursThreeShoot extends SequentialCommandGroup {
  public ShootPickupOursThreeShoot() {
    TrajectoryConfig intakeConfig = new TrajectoryConfig(
      Constants.INTAKE_MAX_V, Constants.DRIVE_MAX_A);
    intakeConfig.setKinematics(Constants.kDriveKinematics);
    intakeConfig.addConstraint(Constants.driveVoltageConstraint);
    intakeConfig.setStartVelocity(Constants.INTAKE_MAX_V);
    intakeConfig.setEndVelocity(0);
    Trajectory intakeTrajectory = TrajectoryGenerator.generateTrajectory(
      List.of(CriticalPoints.ourTrenchRunEntry, CriticalPoints.ourTrenchRunThreeBallStop),
      intakeConfig);

    addCommands(new AutoShoot());
    addCommands(
      new TrajectoryGoTo(
      List.of(CriticalPoints.ourTrenchRunPreEntry),
      true, Constants.DRIVE_MAX_V, Constants.DRIVE_MAX_A , 0));
    addCommands(new ParallelRaceGroup(
      new TrajectoryGoTo(
      List.of(CriticalPoints.ourTrenchRunEntry, 
        CriticalPoints.ourTrenchRunThreeBallStop),
      false, Constants.INTAKE_MAX_V, Constants.DRIVE_MAX_A , 0),
      new ManualIntake(3))
    );
    addCommands(new AutoShoot());
    // addCommands(new ParallelRaceGroup(
    //   new TrajectoryGoTo(List.of(CriticalPoints.shootPoint), 
    //     true, Constants.DRIVE_MAX_V, Constants.DRIVE_MAX_A),
    //   new SequentialCommandGroup(
    //     new WaitCommand(0.05),
    //     new PrespinAfter(() -> {
    //       return Robot.driveSubsystem.trajectoryTimeLeft() < 0.5;
    //     })
    //   )
    // ));
  }
}
