package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.commands.DriveForOneSec;
import frc.robot.commands.DriveWithJoystick;


public class Robot extends TimedRobot {
    public static Joystick joystick = new Joystick(0);
    public static DriveSubsystem driveSubsystem = new DriveSubsystem();
    public static DriveWithJoystick driveWithJoystick =
            new DriveWithJoystick(driveSubsystem, joystick);

    public static DriveForOneSec driveForOneSecCommand = new DriveForOneSec(driveSubsystem);
    JoystickButton driveForOneSecButton = new JoystickButton(joystick, 2);

    @Override
    public void robotInit() {
        driveSubsystem.setDefaultCommand(driveWithJoystick);
        driveForOneSecButton.whenPressed(driveForOneSecCommand);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
