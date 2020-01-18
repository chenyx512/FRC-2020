/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.commands.DriveWithJoystick;
import frc.robot.commands.VControl;
import frc.robot.commands.getAutonomousCommand;

import java.util.List;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static DriveSubsystem driveSubsystem = new DriveSubsystem();
  public static DriveWithJoystick driveWithJoystick = new DriveWithJoystick(driveSubsystem);
  public static getAutonomousCommand driveTrajectory = new getAutonomousCommand();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    driveSubsystem.setDefaultCommand(driveWithJoystick);
    JoystickButton vControlButton = new JoystickButton(Control.getInstance().drive, 1);
    vControlButton.whileHeld(new VControl());
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    driveSubsystem.resetPose(
      new Pose2d(new Translation2d(0, 0), new Rotation2d(0))
    );
  }

  @Override
  public void autonomousInit() {
    var command = getAutonomousCommands();
    command.schedule();
  }

  public Command getAutonomousCommands() {

    // Create a voltage constraint to ensure we don't accelerate too fast
    var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(Constants.ks * 12,
        Constants.kv * 12, Constants.ka * 12), Constants.kDriveKinematics, 11);

    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(Constants.kMaxSpeedMetersPerSecond,
        Constants.kMaxAccelerationMetersPerSecondSquared);
    // Add kinematics to ensure max speed is actually obeyed
    config.setKinematics(Constants.kDriveKinematics);
    // Apply the voltage constraint
    config.addConstraint(autoVoltageConstraint);
    config.setReversed(false);

    // An example trajectory to follow. All units in meteers.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),
        // Pass through these two interior waypoints, making an 's' curve path
        List.of(new Translation2d(-3, 0)),
        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(-3, -2, new Rotation2d(0)),
        // Pass config
        config);


    RamseteCommand ramseteCommand = new RamseteCommand(
      exampleTrajectory, 
      driveSubsystem::getPose,
      new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
      Constants.kDriveKinematics,
      driveSubsystem::outputMetersPerSecond, 
      driveSubsystem
    );

    // Run path following command, then stop at the end.
    return ramseteCommand.andThen(() -> driveSubsystem.outputMetersPerSecond(0, 0));
  }
}
