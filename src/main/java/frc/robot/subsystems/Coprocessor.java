package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Coprocessor extends SubsystemBase {
  NetworkTable odomTable = NetworkTableInstance.getDefault().getTable("odom");

  // flags coresponding to connection of Nano, tracking camera, RGB camera
  public boolean isConnected, isPoseGood, isTargetGood;
  // values for maintaining angle
  public double targetT265Azm, targetFieldAzm, t265Azm, fieldAzm;
  public double targetRelativeDirRight;
  public boolean isTargetFound;

  private double lastClientTime;
  private int disconnectCnt;

  public Coprocessor() {
  }

  @Override
  public void periodic() {
    checkConnection();
    
    isTargetFound = odomTable.getEntry("target_found").getBoolean(false);
    targetT265Azm = odomTable.getEntry("target_t265_azm").getDouble(0);
    t265Azm = odomTable.getEntry("t265_pose_t").getDouble(0);
    targetFieldAzm = odomTable.getEntry("target_field_azm").getDouble(0);
    fieldAzm = odomTable.getEntry("field_pose_t").getDouble(0);

    targetRelativeDirRight = odomTable.getEntry("target_relative_dir_right").getDouble(0);
  }

  /** This method updates if Nano is working as expected
   */
  private void checkConnection() {
    double clientTime = odomTable.getEntry("client_time").getDouble(0);
    if(clientTime == lastClientTime){
      disconnectCnt++;
      if(disconnectCnt > 5){
        isConnected=false;
        odomTable.getEntry("field_calibration_good").setBoolean(false);
      }
    }
    else{
      disconnectCnt=0;
      isConnected=true;
    }
    lastClientTime = clientTime;

    SmartDashboard.putBoolean("coprocessor_connected", isConnected);
    isTargetGood = odomTable.getEntry("target_good").getBoolean(false);
    isPoseGood = odomTable.getEntry("pose_good").getBoolean(false);
  }

  /** This method checks if Nano is working as expected
   */
  public boolean isWorking() {
    return isConnected && isTargetGood && isPoseGood;
  }

  public boolean isFieldCalibrated() {
    return odomTable.getEntry("field_calibration_good").getBoolean(false);
  }
}
