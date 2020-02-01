package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

/** This command is used to recalibrate field AFTER competition starts
 *  To calibrate BEFORE competition starts, 
 *  change "odom/field_calibration_start" to true in dashboard
 */
public class RecalibrateField extends CommandBase {

  public RecalibrateField() {
    // make sure there is no motion when this is performed
    addRequirements(Robot.driveSubsystem);
    addRequirements(Robot.ballShooter);
  }

  @Override
  public void initialize() {
    System.out.println("start recalibrate field");
    NetworkTableInstance.getDefault().getEntry("odom/field_calibration_start").setBoolean(true);
  }

  @Override
  public void end(boolean interrupted) {
    System.out.println("end recalibrate field");
  }

  @Override
  public boolean isFinished() {
    return NetworkTableInstance.getDefault().
        getEntry("odom/field_calibration_good").getBoolean(false);
  }
}
