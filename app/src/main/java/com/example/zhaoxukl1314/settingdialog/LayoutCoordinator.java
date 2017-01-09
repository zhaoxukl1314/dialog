

package com.example.zhaoxukl1314.settingdialog;

import android.graphics.Rect;

public interface LayoutCoordinator {

    /**
     * Coordinate view position according to the orientation
     * This method is called when the layout of SettingDialog is changed.
     * So width and height have measured for SettingDialog.
     *
     * @param orientation Orientation
     */
    void coordinatePosition(int orientation);

    /**
     * Coordinate view size according to the orientation
     *
     * @param orientation Orientation
     */
    void coordinateSize(int orientation);

    /**
     * Get Coordinate view Rectangle.
     *
     * @return view rectangle
     */
    Rect getDialogRect();
}
