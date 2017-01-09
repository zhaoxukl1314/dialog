

package com.example.zhaoxukl1314.settingdialog;

import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * This class coordinates a layout of second layer setting dialog.
 */
public class SecondLayerLayoutCoordinator implements LayoutCoordinator {
    public static final String TAG = "SecondLayerLayoutCoordinator";

    private final int mTopMargin;

    private final SettingDialogBasic mView;
    private final MenuDialogRectCalculator mMenuDialogRectCalculator;
    private final Rect mBounds;
    private final Rect mAnchor;

    private int mDialogWidth;
    private int mDialogHeight;
    public Rect mDialogRect;

    /**
     * Constructor.
     *
     * @param view
     *      Shortcut dialog view
     * @param containerRect
     *      surface view rectangle.
     * @param anchorRect
     *      pressed shortcut icon rectangle.
     */
    public SecondLayerLayoutCoordinator(
            SettingDialogBasic view,
            Rect containerRect,
            Rect anchorRect,
            int menuDialogRowCount,
            int numberOfTabs) {
        mView = view;
        mBounds = containerRect;
        mAnchor = anchorRect;
        mMenuDialogRectCalculator = new MenuDialogRectCalculator(
                view.getContext(), containerRect, menuDialogRowCount, numberOfTabs);
        mTopMargin = view.getContext().getResources().getDimensionPixelSize(
                R.dimen.second_layer_dialog_margin_top);
    }

    @Override
    public void coordinatePosition(int orientation) {
        int parentDialogWidth = mMenuDialogRectCalculator.computeWidth(orientation);
        int parentDialogHeight = mMenuDialogRectCalculator.computeHeight(orientation);

        // This view is aligned left of a parent dialog if language is RtoL.
        int left = CommonUtility.isMirroringRequired(mView.getContext())
                ? mAnchor.left
                : mAnchor.right - mDialogWidth;
        int top = mAnchor.bottom + mTopMargin;

        Rect targetRect = new Rect(left, top, left + mDialogWidth, top + mDialogHeight);
        Rect rotationSourceArea = new Rect(0, 0, parentDialogWidth, parentDialogHeight);

        Point rotationDestPosition = mMenuDialogRectCalculator.computePosition(orientation);
        if (isPortrait(orientation)) {
            // Adjust bottom edge to the containerRect if target dialog is beyond the containerRect.
            if (targetRect.bottom + rotationDestPosition.x > mBounds.right) {
                rotationDestPosition.x = mBounds.right - targetRect.bottom;
            }
        } else {
            // Adjust bottom edge to the containerRect if target dialog is beyond the containerRect.
            if (targetRect.bottom + rotationDestPosition.y > mBounds.bottom) {
                rotationDestPosition.y = mBounds.bottom - targetRect.bottom;
            }
        }

        mDialogRect = LayoutCoordinateUtil.coodinatePosition(
                orientation,
                mView,
                targetRect,
                rotationSourceArea,
                rotationDestPosition);
    }

    @Override
    public void coordinateSize(int orientation) {
        int heightLimit = isPortrait(orientation) ? mBounds.width() : mBounds.height();

        mView.setNumColumns(1);
        int numRows = mView.getNumRows(heightLimit);
        mDialogWidth = mView.computeWidth(1);
        mDialogHeight = Math.min(mView.computeMaxHeight(numRows), mView.computeHeight(1));

        mView.getLayoutParams().width = mDialogWidth;
        mView.getLayoutParams().height = mDialogHeight;
    }

    @Override
    public Rect getDialogRect() {
        return mDialogRect;
    }

    private boolean isPortrait(int orientation) {
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
