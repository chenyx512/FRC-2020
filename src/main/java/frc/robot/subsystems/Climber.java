package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  // 1 is pull
  public CANSparkMax winch = new CANSparkMax(21, MotorType.kBrushless);
  // 1 is up
  public CANSparkMax hook = new CANSparkMax(40, MotorType.kBrushed);
  
  public Climber() {
    setSpark(winch);
    setSpark(hook);
  }

  private void setSpark(CANSparkMax spark) {
    spark.restoreFactoryDefaults();
  }
}