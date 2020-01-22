from collections import deque
import argparse
from imutils.video import VideoStream
import cv2
import imutils
import numpy as np

hMin = 19
hMax = 30
sMin = 83
sMax = 193
vMin = 151
vMax = 255


ap = argparse.ArgumentParser()
# ap.add_argument("-b", "--buffer", type=int, default=32,
# 	help="max buffer size")
args = vars(ap.parse_args())

colorLower = (hMin, sMin, vMin)
colorUpper = (hMax, sMax, vMax)

vs = VideoStream(src=0).start()

while True:
    frame = vs.read()

    frame = imutils.resize(frame, width=600)
    blurred = cv2.GaussianBlur(frame, (11, 11), 0)
    hsv = cv2.cvtColor(blurred, cv2.COLOR_BGR2HSV)

    mask = cv2.inRange(hsv, colorLower, colorUpper)
    mask = cv2.erode(mask, None, iterations=2)
    mask = cv2.dilate(mask, None, iterations=1)

    cnts = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,
                            cv2.CHAIN_APPROX_SIMPLE)
    cnts = imutils.grab_contours(cnts)
    center = None

    for contour in cnts:
        if cv2.contourArea(contour) <= 100:
            continue
        ((x, y), radius) = cv2.minEnclosingCircle(contour)
        # M = cv2.moments(c)
        # center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))
        if radius > 20:
            cv2.circle(frame, (int(x), int(y)), int(radius),
            (255, 0, 0), 2)
    cv2.imshow("Frame", frame)
    cv2.waitKey(1)



