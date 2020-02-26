package frc.robot.commands.Auto;

import java.util.List;

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

public class ShootPickThreeShoot extends SequentialCommandGroup {
  public ShootPickThreeShoot() {
    TrajectoryConfig intakeConfig = new TrajectoryConfig(
      Constants.INTAKE_MAX_V, Constants.DRIVE_MAX_A);
    intakeConfig.setKinematics(Constants.kDriveKinematics);
    intakeConfig.addConstraint(Constants.driveVoltageConstraint);
    intakeConfig.setStartVelocity(0);
    intakeConfig.setEndVelocity(0);
    Trajectory barThreeSideTrajectory = TrajectoryGenerator.generateTrajectory(
      List.of(CriticalPoints.barThreeBallSideEntry, CriticalPoints.barThreeBallSideStop),
      intakeConfig);
    Trajectory barTwoSideTrajectory = TrajectoryGenerator.generateTrajectory(
      List.of(CriticalPoints.barTwoBallSideEntry, CriticalPoints.barTwoBallSideStop),
      intakeConfig);
    

    addCommands(new AutoShoot());
    addCommands(new TrajectoryGoTo(
      List.of(CriticalPoints.barThreeBallSideEntry), 
      false, Constants.DRIVE_MAX_V, Constants.DRIVE_MAX_A , 0
    ));
    addCommands(new ParallelRaceGroup(
      new RunTrajectory(barThreeSideTrajectory),
      new ManualIntake(3)
    ));
    addCommands(new TrajectoryGoTo(
      List.of(CriticalPoints.barTwoBallSideEntry), 
      true, Constants.DRIVE_MAX_V, Constants.DRIVE_MAX_A , 0
    ));
    addCommands(new ParallelRaceGroup(
      new RunTrajectory(barTwoSideTrajectory),
      new ManualIntake(5)
    ));
    // addCommands(new AutoShoot());
  }
}
