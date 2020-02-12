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

public class BallHandler extends SubsystemBase {
  public enum BallHandlerState {
    IDLE,
    INTAKE,
    SHOOT,
    EJECT,
  }
  public static final boolean BALL = false, NOBALL = true;
  
  // shooter
  public CANSparkMax shooterMaster = new CANSparkMax(31, MotorType.kBrushless);
  public CANSparkMax shooterSlave = new CANSparkMax(32, MotorType.kBrushless);
  public CANSparkMax shooterConveyer = new CANSparkMax(23, MotorType.kBrushless);
  public CANEncoder encoder;
  public CANPIDController shooterPIDController;
  public DigitalInput shooterBeamBreaker = new DigitalInput(0);
  // intake
  public CANSparkMax ballIntake = new CANSparkMax(26, MotorType.kBrushless);
  public CANSparkMax intakeConveyer = new CANSparkMax(21, MotorType.kBrushless);
  public DigitalInput intakeBeamBreaker = new DigitalInput(0);

  public BallHandlerState state = BallHandlerState.IDLE;
  public int ballCnt = 0;  
  private boolean lastIntakeBeam, lastShooterBeam;
  private Control control = Control.getInstance();

  public BallHandler() {
    lastIntakeBeam = intakeBeamBreaker.get();
    lastShooterBeam = shooterBeamBreaker.get();

    setSpark(shooterMaster);
    setSpark(shooterSlave);
    setSpark(shooterConveyer);
    setSpark(ballIntake);
    setSpark(intakeConveyer);

    shooterConveyer.setInverted(true);
    shooterMaster.setInverted(true);
    ballIntake.setInverted(true);
    shooterSlave.follow(shooterMaster, true);

    encoder = shooterMaster.getEncoder();
    shooterPIDController = shooterMaster.getPIDController();
    setShooterPID();
    
    intakeConveyer.setIdleMode(IdleMode.kBrake);
    shooterConveyer.setIdleMode(IdleMode.kBrake);
    shooterMaster.setIdleMode(IdleMode.kBrake);
    shooterMaster.setClosedLoopRampRate(0.5);
  }

  @Override
  public void periodic() {
    updateBallCnt();
    // TODO what if beam breaker / ballCnt is messed up
    SmartDashboard.putNumber("ball_handler/cnt", ballCnt);
    SmartDashboard.putString("ball_handler/state", state.toString());
    SmartDashboard.putNumber("ball_handler/shooter_rpm", encoder.getVelocity());

    switch (state) {
      case IDLE:
        ballIntake.set(0);
        intakeConveyer.set(0);
        shooterConveyer.set(0);
        shooterPIDController.setReference(0, ControlType.kVelocity);
        break;
      case SHOOT:
        ballIntake.set(0);
        // wait for spin up
        intakeConveyer.set(1);
        shooterConveyer.set(1);
        shooterPIDController.setReference(
            control.getSlider() * 5700, ControlType.kVelocity);
        if (ballCnt == 0) 
          state = BallHandlerState.IDLE;
        break;
      case INTAKE:
        ballIntake.set(1);
        intakeConveyer.set(1);
        shooterConveyer.set(shooterBeamBreaker.get() == BALL? 0:1);
        shooterPIDController.setReference(0, ControlType.kVelocity);
        if (ballCnt == 5) 
          state = BallHandlerState.IDLE;
        break;
      case EJECT:
        ballIntake.set(-1);
        intakeConveyer.set(-1);
        shooterConveyer.set(-1);
        shooterPIDController.setReference(-1000, ControlType.kVelocity);
        break;
    }
  }

  private void updateBallCnt() {
    if (lastIntakeBeam == NOBALL && intakeBeamBreaker.get() == BALL)
      ballCnt ++;
    lastIntakeBeam = intakeBeamBreaker.get();

    if (lastShooterBeam == BALL && shooterBeamBreaker.get() == NOBALL)
      ballCnt --;
    lastShooterBeam = shooterBeamBreaker.get();
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
