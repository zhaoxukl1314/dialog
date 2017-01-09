/*
 * Copyright (C) 2012 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.example.zhaoxukl1314.settingdialog;

import android.view.ViewGroup;

/**
 * Interface definition for a setting dialog.
 *
 */
public interface SettingDialogInterface2 {

    void open(ViewGroup parentView);

    void close();

    void setSensorOrientation(int orientation);

    void setStateListener(SettingDialogStateListener listener);

    boolean hitTest(int x, int y);

    void setEnabled(boolean enable);
}
