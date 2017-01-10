
package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * This class provides the function to calculate size of Menu dialog.
 */
class MenuDialogRectCalculator {

    private final Context mContext;

    private final int mPadding;
    private final int mDividerHeight;
    private final int mWidth;
    private final int mItemHeight;

    private final int mMaxHeightMargin;

    private final Rect mBounds;
    private int mMenuDialogRowCount;

    public MenuDialogRectCalculator(Context context, Rect containerBounds, int menuDialogRowCount) {
        mContext = context;
        Resources res = context.getResources();
        mPadding = res.getDimensionPixelSize(R.dimen.menu_dialog_padding);
        mDividerHeight = res.getDimensionPixelSize(R.dimen.divider_height);
        mWidth = res.getDimensionPixelSize(R.dimen.setting_dialog_menu_width);
        mItemHeight = res.getDimensionPixelSize(R.dimen.menu_dialog_item_height);
        mBounds = containerBounds;

        mMaxHeightMargin = res.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_phone);
        mMenuDialogRowCount = menuDialogRowCount;
    }

    public Point computePosition() {
        return computePositionForPhone();
    }

    private Point computePositionForPhone() {
        return new Point(
                mBounds.left,
                mBounds.top + (mBounds.height() - computeHeight()) / 2);
    }

    public int computeHeight() {
//        int height = isPortrait(orientation) ? mBounds.width() : mBounds.height();
        int height = mBounds.height();
        int numRows = getNumRows(height);
        return numRows * mItemHeight
                + mPadding * 2
                + numRows * mDividerHeight;

    }

    public int computeWidth() {
        return mWidth;
    }

    /**
     * Get number of rows for items in setting tab dialog.
     * The screen height is derived as below.
     * [screen height] = 2 * [dialog edge padding]
     * + [setting group tab height]
     * + [number of rows] * [setting item height]
     * + [max dialog vertical margin]
     * <p>
     * The number of rows is derived from This formula.
     *
     * @param screenHeight screen height size.
     * @return number of rows.
     */
    private int getNumRows(int screenHeight) {
        int numRows;
        numRows = (int) ((screenHeight
                        - mMaxHeightMargin
                        - mPadding * 2
                        + mDividerHeight) / (mItemHeight + mDividerHeight));
        if ((0 < mMenuDialogRowCount) && (mMenuDialogRowCount < numRows)) {
            numRows = mMenuDialogRowCount;
        }

        return numRows;
    }
}
