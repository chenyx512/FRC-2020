package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.team254.lib.util.MinTimeBoolean;
import com.team254.lib.util.TimeDelayedBoolean;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Control;


public class BallHandler extends SubsystemBase {
  public enum BallHandlerState {
    IDLE,
    INTAKE,
    PRESPIN,
    SHOOT,
    EJECT,
  }
  public BallHandlerState state = BallHandlerState.IDLE;
  public int ballCnt = 0;
  public double desiredRPM;
  
  private enum ShootingState {
    HOLD,
    SHOOT
  }
  private BallHandlerState lastState = state;
  private ShootingState shootingState = ShootingState.HOLD;
  // shooter
  private CANSparkMax shooterMaster = new CANSparkMax(31, MotorType.kBrushless);
  private CANSparkMax shooterSlave = new CANSparkMax(32, MotorType.kBrushless);
  private CANSparkMax shooterConveyer = new CANSparkMax(23, MotorType.kBrushless);
  public CANEncoder encoder;
  private CANPIDController shooterPIDController;
  private DigitalInput shooterBeamBreaker = new DigitalInput(0);
  // auto
  private MinTimeBoolean isHoldingForLastBall = new MinTimeBoolean(Constants.MIN_SHOOT_GAP_TIME);
  private TimeDelayedBoolean isFreeSpinning = new TimeDelayedBoolean(Constants.MAX_SHOOTER_FREE_SPIN_TIME);
  // intake
  private CANSparkMax ballIntake = new CANSparkMax(26, MotorType.kBrushless);
  private CANSparkMax intakeConveyer = new CANSparkMax(22, MotorType.kBrushless);
  private DigitalInput intakeBeamBreaker = new DigitalInput(1);

  private static final boolean BALL = false, NO_BALL = true;
  
  private boolean lastIntakeBeam, lastShooterBeam;

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
    shooterMaster.setClosedLoopRampRate(0.3);
  }

  @Override
  public void periodic() {
    // desiredRPM = Control.getInstance().getSlider() * 2000 + 3700;
    switch (state) {
      case IDLE:
        ballIntake.set(0);
        intakeConveyer.set(0);
        shooterConveyer.set(0);
        shooterMaster.set(0);
        isFreeSpinning.update(false);
        break;
      
      case PRESPIN:
        ballIntake.set(0);
        intakeConveyer.set(0);
        shooterConveyer.set(shooterBeamBreaker.get() == NO_BALL? 0.7 : 0);
        shooterPIDController.setReference(desiredRPM, ControlType.kVelocity,
            0, Constants.SHOOTER_KS);
        isFreeSpinning.update(true);
        break;

      case SHOOT:
        // if we just start shooting, 
        if (lastState != BallHandlerState.SHOOT)
          shootingState = ShootingState.HOLD;
        // if a ball just got shot, 
        if (lastShooterBeam == BALL && shooterBeamBreaker.get() == NO_BALL) {
          shootingState = ShootingState.HOLD;
          isHoldingForLastBall.update(true);
          isFreeSpinning.update(false);
        }
        // we start shooting only if there is enough gap from last shot and 
        // and that the wheel spins fast enough
        // or if desiredRPM is unattainable after free spinning for a while
        if (isHoldingForLastBall.get() == false && 
            Math.abs(desiredRPM - encoder.getVelocity()) < Constants.MAX_SHOOT_RPM_ERROR
            || isFreeSpinning.get())
          shootingState = ShootingState.SHOOT;

        ballIntake.set(0);
        // run intake conveyer to make sure the last ball gets shot
        // but not when there are too many balls to prevent jamming
        intakeConveyer.set(ballCnt <= 2 || Control.getInstance().isOverride()? 1 : 0);
        shooterConveyer.set(shootingState == ShootingState.SHOOT || 
                            shooterBeamBreaker.get() == NO_BALL? 1 : 0);
        shooterPIDController.setReference(desiredRPM, ControlType.kVelocity,
            0, Constants.SHOOTER_KS);
        isFreeSpinning.update(true);
        break;

      case INTAKE:
        ballIntake.set(1);
        intakeConveyer.set(1);
        shooterConveyer.set(shooterBeamBreaker.get() == BALL? 0 : 0.7);
        shooterMaster.set(0);
        isFreeSpinning.update(false);
        break;
      
      case EJECT:
        ballIntake.set(-1);
        intakeConveyer.set(-1);
        shooterConveyer.set(-1);
        shooterPIDController.setReference(-1000, ControlType.kVelocity,
            0, 1);
        break;
    }
    lastState = state;

    updateBallCnt();
    SmartDashboard.putNumber("ball_handler/cnt", ballCnt);
    SmartDashboard.putString("ball_handler/state", state.toString());
    SmartDashboard.putNumber("ball_handler/shooter_rpm", encoder.getVelocity());
    SmartDashboard.putNumber("ball_handler/desired_rpm", desiredRPM);
    SmartDashboard.putString("ball_handler/intakeBeam", 
      intakeBeamBreaker.get() == BALL? "BALL" : "NO_BALL");
    SmartDashboard.putString("ball_handler/shooterBeam", 
      shooterBeamBreaker.get() == BALL? "BALL" : "NO_BALL");
  }

  private void updateBallCnt() {
    if (lastIntakeBeam == NO_BALL && intakeBeamBreaker.get() == BALL &&
        state != BallHandlerState.EJECT)
      ballCnt ++;
    else if (lastIntakeBeam == BALL && intakeBeamBreaker.get() == NO_BALL &&
        state == BallHandlerState.EJECT)
      ballCnt --;
    lastIntakeBeam = intakeBeamBreaker.get();

    if (lastShooterBeam == BALL && shooterBeamBreaker.get() == NO_BALL &&
        state != BallHandlerState.EJECT)
      ballCnt --;
    if (lastShooterBeam == NO_BALL && shooterBeamBreaker.get() == BALL &&
        state == BallHandlerState.EJECT)
      ballCnt ++;
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
    shooterPIDController.setI(Constants.SHOOTER_V_GAINS.kI);
    shooterPIDController.setFF(Constants.SHOOTER_V_GAINS.kF);
    shooterPIDController.setD(Constants.SHOOTER_V_GAINS.kD);
    shooterPIDController.setOutputRange(-1, 1);
  }
}
