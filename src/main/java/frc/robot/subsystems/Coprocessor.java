package frc.robot.subsystems;


import com.team254.lib.util.MinTimeBoolean;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Coprocessor extends SubsystemBase {
  public NetworkTable odomTable = NetworkTableInstance.getDefault().getTable("odom");

  // flags coresponding to connection of Nano, tracking camera, RGB camera
  public boolean isConnected;
  // target
  public double targetFieldTheta, targetRelativeDirLeft, targetDis;
  public boolean isTargetFound, isTargetGood;
  // pose
  public double fieldX, fieldY, fieldTheta;
  public boolean isPoseGood;
  // ball
  public double ballDis, ballFieldTheta, ballRelativeDirLeft;
  public boolean isBallGood, isBallFound;
  public double innerDis, innerAngleDelta;

  private double lastClientTime;
  private int disconnectCnt;

  public Coprocessor() {
  }

  @Override
  public void periodic() {
    checkConnection();
    // pose
    fieldX = odomTable.getEntry("field_x").getDouble(0);
    fieldY = odomTable.getEntry("field_y").getDouble(0);
    fieldTheta = odomTable.getEntry("field_t").getDouble(0);
    // target
    isTargetFound = odomTable.getEntry("target_found").getBoolean(false);
    targetFieldTheta = odomTable.getEntry("target_field_theta").getDouble(0);
    targetRelativeDirLeft = odomTable.getEntry("target_relative_dir_left").getDouble(0);
    targetDis = odomTable.getEntry("target_dis").getDouble(0);
    // ball
    isBallFound = odomTable.getEntry("ball_found").getBoolean(false);
    ballDis = odomTable.getEntry("ball_dis").getDouble(0);
    ballRelativeDirLeft = odomTable.getEntry("ball_to_left").getDouble(0);
    ballFieldTheta = odomTable.getEntry("ball_field_theta").getDouble(0);
    // solve the triangle between robot, outer target, and inner target
    if (isTargetFound) {
      // law of cos
      innerDis = Math.sqrt(Math.max(0, 
          0.74 * 0.74 + targetDis * targetDis - 
          1.48 * targetDis * Math.cos(Math.toRadians(-targetFieldTheta))));
      // law of sine
      innerAngleDelta = Math.toDegrees(Math.asin(0.74 * 
          Math.sin(Math.toRadians(-targetFieldTheta)) / innerDis));
      // actual_target = target_theta - delta
      // System.out.printf("%.2f %.2f\n", innerDis, innerAngleDelta); 
      odomTable.getEntry("inner_target_dis").setDouble(innerDis);
    }
  }

  /** This method updates if Nano is working as expected
   */
  private void checkConnection() {
    double clientTime = odomTable.getEntry("client_time").getDouble(0);
    if(clientTime == lastClientTime){
      disconnectCnt++;
      if(disconnectCnt > 15){
        isConnected=false;
        // odomTable.getEntry("field_calibration_good").setBoolean(false);
      }
    }
    else{
      disconnectCnt=0;
      isConnected=true;
    }
    lastClientTime = clientTime;

    isTargetGood = odomTable.getEntry("target_good").getBoolean(false);
    isPoseGood = odomTable.getEntry("pose_good").getBoolean(false);
    isBallGood = odomTable.getEntry("ball_good").getBoolean(false);
    NetworkTableInstance.getDefault().getEntry("/coprocessor/working").
        setBoolean(isConnected && isPoseGood && isTargetGood && isBallGood);
    String error = "";
    if (!isConnected)
      error = "COMM";
    else {
      if (!isTargetGood)
        error += "CV|";
      if (!isPoseGood)
        error += "POSE|";
      if (!isBallGood)
        error += "BALL";
    } 
    NetworkTableInstance.getDefault().getEntry("/coprocessor/error").
        setString(error);
  }

  public boolean isFieldCalibrated() {
    return odomTable.getEntry("field_calibration_good").getBoolean(false);
  }

  public void calibrate_field() {
    odomTable.getEntry("field_calibration_start").setBoolean(true);
  }

  public Pose2d getPose() {
    return new Pose2d(fieldX, fieldY, new Rotation2d(Math.toRadians(fieldTheta)));
  }
}
