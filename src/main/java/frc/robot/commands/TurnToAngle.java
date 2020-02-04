package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.*;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Control;
import frc.robot.Robot;


public class TurnToAngle extends InstantCommand {
  private final Control control = Control.getInstance();
  boolean target_found;
  double target_to_the_left_degree;
  public TurnToAngle() {
    addRequirements(Robot.driveSubsystem);
  }

  @Override
  public void initialize() {
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("odom_table");
    table.getEntry("target_to_the_left_degree");
    target_found = table.getEntry("target_found").getBoolean(false);
    target_to_the_left_degree = table.getEntry("targetToTheLeftDegree").getDouble(0.0);
    
    if(!target_found) {
      System.out.println("target not found");
      return;
    }
    double p_value = 1.0 / 60;
    double power = p_value * target_to_the_left_degree;
    System.out.printf("output %f\n", power);
    Robot.driveSubsystem.outputMetersPerSecond(-power, power);

  }

  @Override
  public void end(boolean interrupted) {
    
  }
}