

package com.example.zhaoxukl1314.settingdialog;

import android.graphics.Rect;

/**
 * This class coordinates a layout of setting menu dialog.
 */
public class MenuLayoutCoordinator implements LayoutCoordinator {

    private final SettingTabDialogBasic mView;
    private final MenuDialogRectCalculator mMenuDialogRectCalculator;

    public Rect mDialogRect;

    public MenuLayoutCoordinator(
            SettingTabDialogBasic view,
            Rect containerRect,
            int menuDialogRowCount) {
        mView = view;
        mMenuDialogRectCalculator =
                new MenuDialogRectCalculator(view.getContext(), containerRect, menuDialogRowCount);
    }

    /**
     * Set Position of Setting tab dialog.
     * Setting tab dialog contains setting tab(icons) and
     * setting tab body(setting items).
     */
    @Override
    public void coordinatePosition() {
        Rect targetRect = new Rect(
                0,
                0,
                mView.getLayoutParams().width,
                mView.getLayoutParams().height);

        mDialogRect = LayoutCoordinateUtil.coodinatePosition(
                mView,
                targetRect,
                targetRect,
                mMenuDialogRectCalculator.computePosition());
    }

    @Override
    public void coordinateSize() {
        mView.setNumColumns(1);
        mView.getLayoutParams().width = mMenuDialogRectCalculator.computeWidth();
        mView.getLayoutParams().height = mMenuDialogRectCalculator.computeHeight();
    }
}
