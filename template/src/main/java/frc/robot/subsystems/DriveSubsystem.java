package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class DriveSubsystem extends SubsystemBase {
    public static WPI_TalonSRX leftMaster, rightMaster;
    private static WPI_TalonSRX leftSlave, rightSlave;
    public static DifferentialDrive drive;

    public DriveSubsystem() {
        leftSlave = new WPI_TalonSRX(1);
        leftMaster = new WPI_TalonSRX(3);
        rightSlave = new WPI_TalonSRX(2);
        rightMaster = new WPI_TalonSRX(4);

        rightMaster.setInverted(false);
        rightSlave.setInverted(false);

        leftSlave.follow(leftMaster);
        rightSlave.follow(rightMaster);

        drive = new DifferentialDrive(leftMaster, rightMaster);
        drive.setSafetyEnabled(true);
    }
}
