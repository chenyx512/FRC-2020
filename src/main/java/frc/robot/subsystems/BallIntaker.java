package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BallIntaker extends SubsystemBase {
  public enum IntakeState {
    IDLE,
    EJECT,
    INTAKE,
  }

  // for all sparks, inward directions to shooter should be +
  public CANSparkMax ballIntake = new CANSparkMax(26, MotorType.kBrushless);
  public CANSparkMax intakeConveyer = new CANSparkMax(21, MotorType.kBrushless);
  public DigitalInput beamBreaker = new DigitalInput(0);

  public IntakeState state = IntakeState.IDLE;
  
  public BallIntaker() {
    setSpark(ballIntake);
    setSpark(intakeConveyer);
    
    intakeConveyer.setIdleMode(IdleMode.kBrake);
    ballIntake.setInverted(true);
  }

  @Override
  public void periodic() {
    switch(state) {
      case IDLE:
        ballIntake.set(0);
        intakeConveyer.set(0);
        break;
      case INTAKE:
        ballIntake.set(1);
        intakeConveyer.set(1);
        break;
      case EJECT:
        ballIntake.set(-1);
        intakeConveyer.set(-1);
        break;
    }
  }

  private void setSpark(CANSparkMax spark) {
    spark.restoreFactoryDefaults();
  }
}
