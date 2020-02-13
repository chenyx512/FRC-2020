package frc.robot.subsystems;

import javax.lang.model.util.ElementScanner6;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
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
  public CANSparkMax intakeConveyer = new CANSparkMax(22, MotorType.kBrushless);
  public DigitalInput intakeBeamBreaker = new DigitalInput(1);

  public BallHandlerState state = BallHandlerState.IDLE;
  public int ballCnt = 0;  
  private double shutShooterTime;
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

    ballIntake.setInverted(true);
    shooterConveyer.setInverted(true);
    shooterMaster.setInverted(true);
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
    double desiredRPM = control.getSlider() * 5600;
    SmartDashboard.putNumber("ball_handler/cnt", ballCnt);
    SmartDashboard.putString("ball_handler/state", state.toString());
    SmartDashboard.putNumber("ball_handler/shooter_rpm", encoder.getVelocity());
    SmartDashboard.putNumber("ball_handler/desired_rpm", desiredRPM);

    switch (state) {
      case IDLE:
        ballIntake.set(0);
        intakeConveyer.set(0);
        shooterConveyer.set(0);
        shooterPIDController.setReference(0, ControlType.kVelocity);
        break;
      
      case SHOOT:
        ballIntake.set(0);
        shooterPIDController.setReference(desiredRPM, ControlType.kVelocity);
        intakeConveyer.set(1);
        if(shooterBeamBreaker.get() == BALL && 
            Math.abs(desiredRPM - encoder.getVelocity()) > 100)
          shooterConveyer.set(0);
        else
          shooterConveyer.set(1);
        if (ballCnt == 0) {
          if (shutShooterTime < Timer.getFPGATimestamp())
            state = BallHandlerState.IDLE;
        } else
          shutShooterTime = Timer.getFPGATimestamp() + 0.5;
        break;

      case INTAKE:
        if (ballCnt == 5) 
            state = BallHandlerState.IDLE;
        else {
          ballIntake.set(1);
          intakeConveyer.set(1);
          shooterConveyer.set(shooterBeamBreaker.get() == BALL? 0:1);
          shooterPIDController.setReference(0, ControlType.kVelocity);
        }
        break;
      
      case EJECT:
        ballIntake.set(-1);
        intakeConveyer.set(-1);
        shooterConveyer.set(0);
        shooterPIDController.setReference(0, ControlType.kVelocity);
        break;
    }
  }

  private void updateBallCnt() {
    if (lastIntakeBeam == NOBALL && intakeBeamBreaker.get() == BALL &&
        state != BallHandlerState.EJECT)
      ballCnt ++;
    else if (lastIntakeBeam == BALL && intakeBeamBreaker.get() == NOBALL &&
        state == BallHandlerState.EJECT)
      ballCnt --;
    lastIntakeBeam = intakeBeamBreaker.get();

    if (lastShooterBeam == BALL && shooterBeamBreaker.get() == NOBALL)
      ballCnt --;
    lastShooterBeam = shooterBeamBreaker.get();

    if(ballCnt > 5) 
      ballCnt = 5;
    if(ballCnt < 0)
      ballCnt = 0;
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
