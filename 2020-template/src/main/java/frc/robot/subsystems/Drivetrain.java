/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

//import frc.robot.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANDigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drivetrain extends SubsystemBase {
  public CANSparkMax motor1;
  public CANSparkMax motor2;
  public Drivetrain() {
    
    motor1 = new CANSparkMax(1, MotorType.kBrushless);
   // motor2 = new CANSparkMax(2, MotorType.kBrushless);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
