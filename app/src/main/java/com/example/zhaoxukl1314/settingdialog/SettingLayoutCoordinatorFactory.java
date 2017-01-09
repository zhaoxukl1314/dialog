
package com.example.zhaoxukl1314.settingdialog;

import android.graphics.Rect;

class SettingLayoutCoordinatorFactory {

    /**
     * Represents layout coordinate data
     *
     */
    public static class LayoutCoordinateData {
        public final Rect containerRect;
        public final Rect anchorRect;

        public LayoutCoordinateData(Rect container, Rect anchor) {
            this.containerRect = container;
            this.anchorRect = anchor;
        }
    }

    public static LayoutCoordinator createShortcutLayoutCoordinator(
            SettingDialogBasic view,
            LayoutCoordinateData data) {

        return new ShortcutLayoutCoordinator(view, data.containerRect, data.anchorRect);
    }

    public static LayoutCoordinator createSecondLayerLayoutCoordinator(
            SettingDialogBasic view,
            LayoutCoordinateData data,
            int menuDialogRowCount,
            int numberOfTabs) {

        return new SecondLayerLayoutCoordinator(
                view,
                data.containerRect,
                data.anchorRect,
                menuDialogRowCount,
                numberOfTabs);
    }

    public static LayoutCoordinator createMenuLayoutCoordinator(
            SettingTabDialogBasic dialog,
            LayoutCoordinateData data,
            int menuDialogRowCount) {

        return new MenuLayoutCoordinator(dialog, data.containerRect, menuDialogRowCount);
    }

    public static LayoutCoordinator createControlLayoutCoordinator(
            SettingControlDialog dialog,
            LayoutCoordinateData data) {

        return new ControlLayoutCoordinator(dialog, data.containerRect, data.anchorRect);
    }
}
