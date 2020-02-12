package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Control;


public class BallShooter extends SubsystemBase {
  public enum ShooterState {
    IDLE,
    EJECT,
    SHOOT,
  }

  public CANSparkMax shooterMaster = new CANSparkMax(1, MotorType.kBrushless);
  public CANSparkMax shooterSlave = new CANSparkMax(2, MotorType.kBrushless);
  public CANSparkMax shooterConveyer = new CANSparkMax(23, MotorType.kBrushless);
  public CANEncoder encoder;
  public CANPIDController shooterPIDController;

  public DigitalInput beamBreaker = new DigitalInput(1);

  public ShooterState state = ShooterState.IDLE;

  public BallShooter() {
    setSpark(shooterMaster);
    setSpark(shooterSlave);
    setSpark(shooterConveyer);
    
    shooterConveyer.setInverted(true);
    shooterConveyer.setIdleMode(IdleMode.kBrake);

    shooterMaster.setInverted(true);
    shooterSlave.follow(shooterMaster, true);

    encoder = shooterMaster.getEncoder();
    shooterPIDController = shooterMaster.getPIDController();
    setShooterPID();
  }

  @Override
  public void periodic(){
    SmartDashboard.putNumber("ball_handler/shooter_rpm", encoder.getVelocity());
    
    switch (state) {
      case IDLE:
        shooterMaster.set(0);
        shooterConveyer.set(0);
        break;
      case SHOOT:
        // TODO tune this with distance and feed with beam
        shooterConveyer.set(1);
        shooterPIDController.setReference(5700 * Control.getInstance().getSlider(), 
            ControlType.kVelocity);
        break;
      case EJECT:
        shooterConveyer.set(-1);
        shooterMaster.set(-0.5);
        break;
    }
  }

  public void setShooter(double rpm){
    shooterPIDController.setReference(rpm, ControlType.kVelocity, 0, 0);
  }

  private void setSpark(CANSparkMax spark) {
    spark.restoreFactoryDefaults();
  }

  private void setShooterPID(){
    shooterPIDController.setP(Constants.SHOOTER_V_GAINS.kP);
    shooterPIDController.setI(0);
    shooterPIDController.setFF(Constants.SHOOTER_V_GAINS.kF);
    shooterPIDController.setD(Constants.SHOOTER_V_GAINS.kD);
    shooterPIDController.setOutputRange(-1, 1);
  }
}
