package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PanelTurner extends SubsystemBase {
  public CANSparkMax wheel = new CANSparkMax(33, MotorType.kBrushless);
  // 1 is up
  public CANSparkMax actuator = new CANSparkMax(29, MotorType.kBrushless);
  public CANEncoder wheelEncoder;

  public PanelTurner() {
    setSpark(wheel);
    setSpark(actuator);
    wheelEncoder = wheel.getEncoder();
    actuator.setSmartCurrentLimit(20);
    wheel.setIdleMode(IdleMode.kBrake);
    wheel.burnFlash();
  }

  private void setSpark(CANSparkMax spark) {
    spark.restoreFactoryDefaults();
    spark.setIdleMode(IdleMode.kBrake);
  }

  public void startTurnWheel() {
    wheel.set(-1);
  }

  public void stop() {
    wheel.set(0);
  }
}
