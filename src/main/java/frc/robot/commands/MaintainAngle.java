package frc.robot.commands;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;


public class MaintainAngle extends InstantCommand {
  private static final double P = 0.0166;
  private double targetAngle, currentAngle;

  public MaintainAngle() {
    addRequirements(Robot.driveSubsystem);
  }

  @Override
  public void execute() {
    if(!Robot.coprocessor.isWorking() || !Robot.coprocessor.isTargetFound){
      System.out.println("coprocessor not working, return");
      return;
    }else if (!Robot.coprocessor.isFieldCalibrated() && 
               !Robot.coprocessor.isTargetFound) {
      System.out.println("field not calibrated and no target found, return");
      return;
    }else if (!Robot.coprocessor.isTargetFound) {
      System.out.println("no visual, turning according to calibrated field pose");
      targetAngle = Robot.coprocessor.targetFieldAzm;
      currentAngle = Robot.coprocessor.fieldAzm;
    }else {
      targetAngle = Robot.coprocessor.targetT265Azm;
      currentAngle = Robot.coprocessor.t265Azm;
    }
    
    double error = ((targetAngle - currentAngle)%360+360)%360;
    if(error>180)
      error -= 360;
    
    double power = P * error;
    if(Math.abs(error)>0.2)
      power += Math.signum(error) * 0.05;

    Robot.driveSubsystem.outputMetersPerSecond(power * -1, power);
  }

}