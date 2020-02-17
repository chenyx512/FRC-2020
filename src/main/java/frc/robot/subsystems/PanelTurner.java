package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PanelTurner extends SubsystemBase {
  public CANSparkMax wheel = new CANSparkMax(41, MotorType.kBrushless);
  // 1 is up
  public CANSparkMax actuator = new CANSparkMax(42, MotorType.kBrushless);
  public CANEncoder wheelEncoder;

  public PanelTurner() {
    setSpark(wheel);
    setSpark(actuator);
    wheelEncoder = wheel.getEncoder();
  }

  private void setSpark(CANSparkMax spark) {
    spark.restoreFactoryDefaults();
    spark.setIdleMode(IdleMode.kBrake);
  }
}
