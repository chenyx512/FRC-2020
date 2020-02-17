package frc.robot.subsystems;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Coprocessor extends SubsystemBase {
  public NetworkTable odomTable = NetworkTableInstance.getDefault().getTable("odom");

  // flags coresponding to connection of Nano, tracking camera, RGB camera
  public boolean isConnected, isPoseGood, isTargetGood;
  public boolean isTargetFound;
  public double fieldX, fieldY, fieldTheta;
  public double targetFieldTheta, targetRelativeDirLeft;
  public double targetDis;

  private double lastClientTime;
  private int disconnectCnt;

  public Coprocessor() {
  }

  @Override
  public void periodic() {
    checkConnection();
    
    isTargetFound = odomTable.getEntry("target_found").getBoolean(false);
    fieldX = odomTable.getEntry("field_x").getDouble(0);
    fieldY = odomTable.getEntry("field_y").getDouble(0);
    fieldTheta = odomTable.getEntry("field_t").getDouble(0);
    targetFieldTheta = odomTable.getEntry("target_field_theta").getDouble(0);
    targetRelativeDirLeft = odomTable.getEntry("target_relative_dir_left").getDouble(0);
    targetDis = odomTable.getEntry("target_dis").getDouble(0);
  }

  /** This method updates if Nano is working as expected
   */
  private void checkConnection() {
    double clientTime = odomTable.getEntry("client_time").getDouble(0);
    if(clientTime == lastClientTime){
      disconnectCnt++;
      if(disconnectCnt > 10){
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

  public boolean isFieldCalibrated() {
    return odomTable.getEntry("field_calibration_good").getBoolean(false);
  }

  public void calibrate_field() {
    odomTable.getEntry("field_calibration_start").setBoolean(true);
  }
}
