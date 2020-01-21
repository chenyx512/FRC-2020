import cv2
from Constants import Constants

def show(img):
    cv2.imshow('img', img)
    cv2.waitKey(1)

vc = cv2.VideoCapture(1)
vc.set(cv2.CAP_PROP_EXPOSURE,-9)
vc.set(cv2.CAP_PROP_FRAME_HEIGHT,1280);
vc.set(cv2.CAP_PROP_FRAME_WIDTH,720);

while True:
    ret, frame = vc.read()
    if not ret:
        raise Exception("no frame")

    frame_HSV = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    thresh = cv2.inRange(frame_HSV, Constants.HSV_LOW, Constants.HSV_HIGH)

    contours, hierarchy = cv2.findContours(thresh, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    contour = max(contours, key=cv2.contourArea)
    cv2.drawContours(frame, [contour], -1, (0, 0, 255))

    # M = cv2.moments(contour)
    # center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))
    #
    # pts.appendleft(center)
    show(frame)