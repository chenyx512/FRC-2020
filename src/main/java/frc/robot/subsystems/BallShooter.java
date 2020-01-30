package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class BallShooter extends SubsystemBase {
  public CANSparkMax master, slave;
  public CANEncoder encoder;
  public CANPIDController PIDController;

  public BallShooter() {
    master = new CANSparkMax(1, MotorType.kBrushless);
    slave = new CANSparkMax(2, MotorType.kBrushless);
    encoder = master.getEncoder();

    setSpark(master);
    setSpark(slave);

    master.setInverted(true);
    slave.follow(master, true);

    PIDController = master.getPIDController();
    setPID();
  }

  @Override
  public void periodic(){
    SmartDashboard.putNumber("shooter_rpm", encoder.getVelocity());
  }

  public void setRPM(double rpm){
    PIDController.setReference(rpm, ControlType.kVelocity, 0, 0);
  }

  private void setSpark(CANSparkMax spark) {
    spark.restoreFactoryDefaults();
  }

  private void setPID(){
    PIDController.setP(Constants.SHOOTER_V_GAINS.kP);
    PIDController.setI(0);
    PIDController.setFF(Constants.SHOOTER_V_GAINS.kF);
    PIDController.setD(Constants.SHOOTER_V_GAINS.kD);
    PIDController.setOutputRange(-1, 1);
  }
}
