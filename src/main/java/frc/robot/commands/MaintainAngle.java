package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;


public class MaintainAngle extends InstantCommand {
  private static final double P = 0.0166;

  public MaintainAngle() {
    addRequirements(Robot.driveSubsystem);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    if(!Robot.coprocessor.isWorking() || !Robot.coprocessor.isTargetFound){
      System.out.println("not satisfied stuff, return");
      return;
    }
    double currentAngle = Robot.coprocessor.poseAzm - 5;
    double error = ((Robot.coprocessor.targetAbsAzm - currentAngle)%360+360)%360;
    if(error>180)
      error -= 360;
    
    double power = P * error;
    if(Math.abs(error)>0.2)
      power += Math.signum(error) * 0.05;

    Robot.driveSubsystem.outputMetersPerSecond(power * -1, power);
  }

}