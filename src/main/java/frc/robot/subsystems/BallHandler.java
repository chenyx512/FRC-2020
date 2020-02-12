package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Control;
import frc.robot.subsystems.BallIntaker.IntakeState;
import frc.robot.subsystems.BallShooter.ShooterState;

public class BallHandler extends SubsystemBase {
  public enum BallHandlerState {
    IDLE,
    INTAKE,
    SHOOT,
    EJECT,
  }
  public static final boolean BALL = false, NOBALL = true;
  
  public BallShooter shooter = new BallShooter();
  public BallIntaker intaker = new BallIntaker();

  public BallHandlerState state = BallHandlerState.IDLE;
  public int ballCnt = 0;  
  private boolean lastIntakeBeam, lastShooterBeam;

  public BallHandler() {
    lastIntakeBeam = intaker.beamBreaker.get();
    lastShooterBeam = shooter.beamBreaker.get();
  }

  @Override
  public void periodic() {
    updateBallCnt();
    // TODO what if beam breaker / ballCnt is messed up
    SmartDashboard.putNumber("ball_handler/cnt", ballCnt);
    SmartDashboard.putString("ball_handler/state", state.toString());

    switch (state) {
      case IDLE:
        intaker.state = IntakeState.IDLE;
        shooter.state = ShooterState.IDLE;
        break;
      case SHOOT:
        intaker.state = IntakeState.IDLE;
        shooter.state = ShooterState.SHOOT;
        if (ballCnt == 0)
          state = BallHandlerState.IDLE;
        break;
      case INTAKE:
        shooter.state = ShooterState.IDLE;
        intaker.state = IntakeState.INTAKE;
        if (ballCnt == 5) 
          state = BallHandlerState.IDLE;
        break;
      case EJECT:
        intaker.state = IntakeState.EJECT;
        shooter.state = ShooterState.EJECT;
        break;
    }
  }

  private void updateBallCnt() {
    if (lastIntakeBeam == NOBALL && intaker.beamBreaker.get() == BALL 
        && intaker.state == IntakeState.INTAKE)
      ballCnt ++;
    if (lastIntakeBeam == BALL && intaker.beamBreaker.get() == NOBALL 
        && intaker.state == IntakeState.EJECT)
      ballCnt --;
    lastIntakeBeam = intaker.beamBreaker.get();

    if (lastShooterBeam == BALL && shooter.beamBreaker.get() == NOBALL
        && shooter.state == ShooterState.SHOOT)
      ballCnt --;
    if (lastShooterBeam == NOBALL && shooter.beamBreaker.get() == BALL
        && shooter.state == ShooterState.EJECT)
      ballCnt ++;
    lastShooterBeam = shooter.beamBreaker.get();
  }
}
