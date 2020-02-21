from networktables import NetworkTables
import cv2
import numpy as np
import math as m


FIELD_LENGTH = 15.98
FIELD_WIDTH = 8.21
TOP_LEFT_X = 96
TOP_LEFT_Y = 26
BUT_RIGHT_X = 1040
BUT_RIGHT_Y = 514
CENTER_X = 97
CENTER_Y = 169

def getImagePoint(fieldX, fieldY):
    return (
        (int)(CENTER_X - fieldX * (BUT_RIGHT_X - TOP_LEFT_X) / FIELD_LENGTH + 0.5),
        (int)(CENTER_Y + fieldY * (BUT_RIGHT_Y - TOP_LEFT_Y) / FIELD_WIDTH + 0.5),
    )

LENGTH = 50
def draw_robot(image, x, y, theta):
    robot_center = getImagePoint(x, y)
    theta = m.radians(180 - theta)
    dx = (robot_center[0] + int(LENGTH * m.cos(theta)),
          robot_center[1] + int(LENGTH * m.sin(theta)))
    cv2.line(image, robot_center, dx, (0, 255, 0), 5)
    cv2.circle(image, getImagePoint(x, y), 20, (0, 255, 0), -1)


def draw_trajectory(image, trajectory):
    for i in range(100):
        x = trajectory[i * 2]
        y = trajectory[i * 2 + 1]
        cv2.circle(image, getImagePoint(x, y), 2, (255, 0, 0), -1)

NetworkTables.startClientTeam(3566)
NetworkTables.startDSClient()
odom_table = NetworkTables.getTable('odom')
original_image = cv2.imread("2020-Field.png")

while True:
    frame = original_image.copy()
    field_x = NetworkTables.getEntry("/odom/field_x").getDouble(1)
    field_y = NetworkTables.getEntry("/odom/field_y").getDouble(0)
    field_theta = NetworkTables.getEntry("/odom/field_t").getDouble(180)
    draw_robot(frame, field_x, field_y, field_theta)
    if NetworkTables.getEntry("/robot/drivetrain/state").getString("nothing")\
            == "TRAJECTORY_FOLLOWING":
        trajectory = NetworkTables.getEntry("/robot/drivetrain/trajectory").\
            getDoubleArray([])
        if len(trajectory) == 200:
            draw_trajectory(frame, trajectory)

    cv2.imshow("visual", frame)
    cv2.waitKey(10)