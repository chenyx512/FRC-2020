#!/usr/bin/env python
# -*- coding: utf-8 -*-

# USAGE: You need to specify a filter and "only one" image source
#
# (python) range-detector --filter RGB --image /path/to/image.png
# or
# (python) range-detector --filter HSV --webcam

import cv2
import argparse
from operator import xor


def callback(value):
    pass


def setup_trackbars(range_filter):
    cv2.namedWindow("Trackbars", 0)

    for i in ["MIN", "MAX"]:
        v = 0 if i == "MIN" else 255

        for j in range_filter:
            cv2.createTrackbar("%s_%s" % (j, i), "Trackbars", v, 255, callback)


def get_trackbar_values(range_filter):
    values = []

    for i in ["MIN", "MAX"]:
        for j in range_filter:
            v = cv2.getTrackbarPos("%s_%s" % (j, i), "Trackbars")
            values.append(v)

    return values


def main():
    range_filter = 'HSV'
    setup_trackbars(range_filter)
    camera = cv2.VideoCapture(1) # TODO tune this to 0 if it is the only camera
    camera.set(cv2.CAP_PROP_EXPOSURE, -11)
    camera.set(cv2.CAP_PROP_FRAME_HEIGHT, 1280);
    camera.set(cv2.CAP_PROP_FRAME_WIDTH, 720);
    while True:
        ret, image = camera.read()

        if not ret:
            break

        frame_to_thresh = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
        v1_min, v2_min, v3_min, v1_max, v2_max, v3_max = get_trackbar_values(range_filter)

        thresh = cv2.inRange(frame_to_thresh, (v1_min, v2_min, v3_min), (v1_max, v2_max, v3_max))

        # preview = cv2.bitwise_and(image, image, mask=thresh)
        cv2.imshow("after", thresh)
        cv2.imshow("before", image)
        cv2.waitKey(1)


if __name__ == '__main__':
    main()

# H 90 - 97
# S 149 - 255
# V 46 - 255