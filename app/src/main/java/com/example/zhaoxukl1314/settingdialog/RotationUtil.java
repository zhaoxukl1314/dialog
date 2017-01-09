/*
 * Copyright (C) 2014 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */
package com.example.zhaoxukl1314.settingdialog;

import android.content.res.Configuration;

/**
 * This class is utility class about rotation.
 *
 */
public class RotationUtil {

    public static final String TAG = "RotationUtil";

    /**
     * Get rotation angle according to the orientation.
     *
     * @param orientation Orientation
     * @return angle
     */
    public static float getAngle(int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return -90;
        } else {
            return 0;
        }
    }

    /**
     * get normalized degree for EXIF.
     * EXIF supports only 0 / 90 / 180 / 270 degree.
     * This method normalized the degree acquired from sensor.
     *
     * @param degrees acquired from sensor
     * @return normalized degree
     */
    public static int getNormalizedRotation(int degrees) {
        int normalizedOrientation = 0;

        degrees %= 360;
        if (((0 + 45) < degrees) && (degrees <= (90 + 45))) {
            normalizedOrientation = 90;
        } else if (((90 + 45) <= degrees) && (degrees <= (180 + 45))) {
            normalizedOrientation = 180;
        } else if (((180 + 45) <= degrees) && (degrees <= (270 + 45))) {
            normalizedOrientation = 270;
        } else {
            normalizedOrientation = 0;
        }
        return normalizedOrientation;
    }
}
