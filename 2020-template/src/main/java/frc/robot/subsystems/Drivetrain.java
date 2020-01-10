/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
  WPI_TalonSRX leftMaster;
  WPI_TalonSRX leftSlave1;
  WPI_TalonSRX leftSlave2;
  
  WPI_TalonSRX rightMaster;
  WPI_TalonSRX rightSlave1;
  WPI_TalonSRX rightSlave2;
  public DifferentialDrive drive;
  
  public Drivetrain() {
    leftMaster = new WPI_TalonSRX(1);
    leftSlave1 = new WPI_TalonSRX(3);
    leftSlave2 = new WPI_TalonSRX(4);

    rightMaster = new WPI_TalonSRX(2);
    rightSlave1 = new WPI_TalonSRX(5);
    rightSlave2 = new WPI_TalonSRX(6);
    drive = new DifferentialDrive(leftMaster, rightMaster);

    leftMaster.setInverted(false);
    leftSlave1.setInverted(false);
    leftSlave2.setInverted(true);
    leftSlave2.follow(leftMaster);
    leftSlave1.follow(leftMaster);
    
    rightMaster.setInverted(false);
    rightSlave1.setInverted(false);
    rightSlave2.setInverted(false);
    rightSlave2.follow(rightMaster);
    rightSlave1.follow(rightMaster);

    
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
