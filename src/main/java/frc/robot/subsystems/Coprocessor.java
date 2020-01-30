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
  public double targetAbsAzm, poseAzm;
  public boolean isTargetFound;

  private double lastClientTime;
  private int disconnectCnt;

  public Coprocessor() {
  }

  @Override
  public void periodic() {
    checkConnection();
    
    isTargetFound = odomTable.getEntry("target_found").getBoolean(false);
    targetAbsAzm = odomTable.getEntry("target_abs_azm").getDouble(0);
    poseAzm = odomTable.getEntry("pose_t").getDouble(0);
  }

  /** This method updates if Nano is working as expected
   */
  private void checkConnection() {
    double clientTime = odomTable.getEntry("client_time").getDouble(0);
    if(clientTime == lastClientTime){
      disconnectCnt++;
      if(disconnectCnt > 5)
        isConnected=false;
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
}
