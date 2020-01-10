package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveSubsystem;

import frc.robot.commands.DriveForOneSec;
import frc.robot.commands.TurnRight;

public class GoTurnGo extends SequentialCommandGroup{

    public GoTurnGo(DriveSubsystem drive) {
        addCommands(
          new DriveForOneSec(drive),
        
          new TurnRight(drive),

          new DriveForOneSec(drive)
        );
    }
}