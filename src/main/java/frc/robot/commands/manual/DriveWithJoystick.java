package frc.robot.commands.manual;


import com.team254.lib.util.CheesyDriveHelper;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Control;
import frc.robot.Robot;


public class DriveWithJoystick extends CommandBase {
  private CheesyDriveHelper cheesyDrive = new CheesyDriveHelper();
  private Control control = Control.getInstance();

  public DriveWithJoystick() {
    addRequirements(Robot.driveSubsystem);
  }

  @Override
  public void execute() {
    // if(control.isOverride()){
    //   double speed = control.getSlider() * 3;
    //   Robot.driveSubsystem.setVelocity(speed, speed);
    //   return;
    // }
    double speed = control.getForwardThrottle();
    boolean isQuickTurn = control.isQuickTurn();
    
    speed = speed * speed * Math.signum(speed);
    if (!control.isOverride())  // override gives full power for hitting
      speed *= 0.8;

    double rotation_sign = (isQuickTurn || speed>0? 1:-1) ;
    double rotation = control.getRotationThrottle();
    rotation *= 0.7;

    if (control.isReversed())
      speed *= -1;

    Robot.driveSubsystem.setOpenLoop(cheesyDrive.cheesyDrive(
      speed,
      rotation * rotation_sign, 
      isQuickTurn, 
      false
    ));
    
  }
}
