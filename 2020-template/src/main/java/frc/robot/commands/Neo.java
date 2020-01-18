/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Joystick;

public class Neo extends CommandBase {
  Drivetrain drive;
  Joystick joystick;
  /**
   * Creates a new DriveWithJoystick.
   */
  public Neo(Drivetrain _drive, Joystick _joystick) {
    drive = _drive;
    joystick = _joystick;
    this.addRequirements(_drive);
  }

  // Called when the command is initially scYheduled.
  @Override
  public void initialize() {
      
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    drive.motor1.set(joystick.getY()*0.3);
   // drive.motor2.set(joystick.getY()*-1);
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
