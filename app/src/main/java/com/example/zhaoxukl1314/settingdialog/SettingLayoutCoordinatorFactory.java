
package com.example.zhaoxukl1314.settingdialog;

import android.graphics.Rect;

class SettingLayoutCoordinatorFactory {

    /**
     * Represents layout coordinate data
     *
     */
    public static class LayoutCoordinateData {
        public final Rect containerRect;

        public LayoutCoordinateData(Rect container) {
            this.containerRect = container;
        }
    }

    public static LayoutCoordinator createMenuLayoutCoordinator(
            SettingTabDialogBasic dialog,
            LayoutCoordinateData data,
            int menuDialogRowCount) {

        return new MenuLayoutCoordinator(dialog, data.containerRect, menuDialogRowCount);
    }

}
