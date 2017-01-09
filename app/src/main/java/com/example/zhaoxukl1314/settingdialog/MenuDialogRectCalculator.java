
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
    private final int mTabHeight;
    private final int mWidth;
    private final int mItemHeight;

    private final int mMaxHeightMargin;

    private final boolean mIsTablet;
    private final Rect mBounds;
    private int mMenuDialogRowCount;
    // Number of Tabs in MenuDialog
    private int mNumberOfTabs;
    public MenuDialogRectCalculator(Context context, Rect containerBounds, int menuDialogRowCount,
            int numberOfTabs) {
        mContext = context;
        Resources res = context.getResources();
        mPadding = res.getDimensionPixelSize(R.dimen.menu_dialog_padding);
        mDividerHeight = res.getDimensionPixelSize(R.dimen.divider_height);
        mTabHeight = res.getDimensionPixelSize(R.dimen.setting_group_tab_height);
        mWidth = res.getDimensionPixelSize(R.dimen.setting_dialog_menu_width);
        mItemHeight = res.getDimensionPixelSize(R.dimen.menu_dialog_item_height);
        mBounds = containerBounds;
        mIsTablet = LayoutDependencyResolver.isTablet(mContext);

        mMaxHeightMargin = mIsTablet
                ? res.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_tablet)
                : res.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_phone);
        mMenuDialogRowCount = menuDialogRowCount;
        mNumberOfTabs = numberOfTabs;
    }

    public Point computePosition(int orientation) {
       if (mIsTablet) {
           return computePositionForTablet(orientation);
       } else {
           return computePositionForPhone(orientation);
       }
    }

    private Point computePositionForPhone(int orientation) {
        if (isPortrait(orientation)) {
            return new Point(
                    mBounds.left,
                    mBounds.top + (mBounds.height() - computeWidth(orientation)) / 2);
        } else {
            return new Point(
                    mBounds.left,
                    mBounds.top + (mBounds.height() - computeHeight(orientation)) / 2);
        }
    }

    private Point computePositionForTablet(int orientation) {

        // Menu dialog is arranged according to icon of Setting shortcut on Tablet.
        int shortcutCount = LayoutDependencyResolver.getLeftItemCount(mContext);
        int shortcutSize = mContext.getResources().getDimensionPixelSize(
                R.dimen.shortcut_dialog_item_height);
        int marginBottom = (mBounds.height() / shortcutCount - shortcutSize) / 2;

        if (isPortrait(orientation)) {
            return new Point(
                    mBounds.left,
                    mBounds.bottom - marginBottom - computeWidth(orientation));
        } else {
            return new Point(
                    mBounds.left,
                    mBounds.bottom - marginBottom - computeHeight(orientation));
        }
    }

    public int computeHeight(int orientation) {
        int height = isPortrait(orientation) ? mBounds.width() : mBounds.height();
        int numRows = getNumRows(height);

        if (mNumberOfTabs < 2) {
            return numRows * mItemHeight
                    + mPadding * 2
                    + numRows  * mDividerHeight;
        } else {
            return numRows * mItemHeight
                    + mPadding * 2
                    + mTabHeight
                    + numRows  * mDividerHeight;
        }
    }

    public int computeWidth(int orientation) {
        return mWidth;
    }

    /**
     * Get number of rows for items in setting tab dialog.
     * The screen height is derived as below.
     * [screen height] = 2 * [dialog edge padding]
     *                   + [setting group tab height]
     *                   + [number of rows] * [setting item height]
     *                   + [max dialog vertical margin]
     *
     * The number of rows is derived from This formula.
     *
     * @param screenHeight
     *          screen height size.
     * @return
     *      number of rows.
     */
    private int getNumRows(int screenHeight) {
        int numRows;
        if (mNumberOfTabs < 2) {
            numRows =
                    (int) ((screenHeight
                           - mMaxHeightMargin
                           - mPadding * 2
                           + mDividerHeight) / (mItemHeight + mDividerHeight));
        } else {
            numRows =
                    (int) ((screenHeight
                           - mMaxHeightMargin
                           - mPadding * 2
                           - mTabHeight
                           + mDividerHeight) / (mItemHeight + mDividerHeight));
        }
        if ((0 < mMenuDialogRowCount) && (mMenuDialogRowCount < numRows)) {
            numRows = mMenuDialogRowCount;
        }

        return numRows;
    }

    private boolean isPortrait(int orientation) {
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
