/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Robot;

/**
 * An example command that uses an example subsystem.
 */
public class DriveWithJoystick extends CommandBase {
  private final DriveSubsystem driveSubsystem;
  private final Joystick joystick;

  public DriveWithJoystick(DriveSubsystem _driveSubsystem, Joystick _joystick) {
    joystick = _joystick;
    driveSubsystem = _driveSubsystem;

    addRequirements(driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    driveSubsystem.drive.curvatureDrive(
      joystick.getRawAxis(1) * -1 * 0.5,
      joystick.getRawAxis(0), 
      joystick.getRawButton(2)
    );
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
