

package com.example.zhaoxukl1314.settingdialog;


import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.ViewGroup.LayoutParams;

public class ShortcutLayoutCoordinator implements LayoutCoordinator {
    public static final String TAG = "ShortcutLayoutCoordinator";

    private final SettingDialogBasic mView;
    private final Rect mContainerRect;
    private final Rect mAnchorRect;

    private final boolean mIsTablet;
    private final int mMaxHeightMargin;

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
     * @param enableTwoColumnDialog
     *      if true, shown dialog may be two column dialog.
     */
    public ShortcutLayoutCoordinator(SettingDialogBasic view, Rect containerRect, Rect anchorRect) {
        mView = view;
        mContainerRect = containerRect;
        mAnchorRect = anchorRect;

        mIsTablet = LayoutDependencyResolver.isTablet(mView.getContext());

        Resources res = mView.getContext().getResources();
        mMaxHeightMargin = mIsTablet
                ? res.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_tablet)
                : res.getDimensionPixelSize(R.dimen.setting_dialog_menu_max_height_margin_phone);
    }

    /**
     * Set dialog position.
     */
    @Override
    public void coordinatePosition(int orientation) {
        if (mIsTablet) {
            coordinatePositionTablet(orientation);
        } else {
            coordinatePositionPhone(orientation);
        }
    }

    private void coordinatePositionPhone(int orientation) {
        mView.setPivotX(0);
        mView.setPivotY(0);
        mView.setRotation(RotationUtil.getAngle(orientation));

        if (isPortrait(orientation)) {
            int left = mContainerRect.left;
            int top = (int) (mContainerRect.top + mDialogWidth
                    + (mContainerRect.height() - mDialogWidth) / 2f);
            mView.setLeft(left);
            mView.setRight(left + mDialogWidth);
            mView.setTop(top);
            mView.setBottom(top + mDialogHeight);
            mDialogRect = new Rect(
                    (int) mView.getX(),
                    (int) mView.getY() - mDialogWidth,
                    (int) mView.getX() + mDialogHeight,
                    (int) mView.getY());
        } else {
            int left = mContainerRect.left;
            int top = (int) (mContainerRect.top + (mContainerRect.height() - mDialogHeight) / 2f);
            mView.setLeft(left);
            mView.setRight(left + mDialogWidth);
            mView.setTop(top);
            mView.setBottom(top + mDialogHeight);
            mDialogRect = new Rect(
                    (int) mView.getX(),
                    (int) mView.getY(),
                    (int) mView.getX() + mDialogWidth,
                    (int) mView.getY() + mDialogHeight);
        }
    }

    private void coordinatePositionTablet(int orientation) {
        // Shortcut dialog is arranged according to icon of Setting shortcut on Tablet.
        int shortcutCount = LayoutDependencyResolver.getLeftItemCount(mView.getContext());
        int shortcutSize = mView.getContext().getResources().getDimensionPixelSize(
                R.dimen.shortcut_dialog_item_height);
        int marginVertical = (mContainerRect.height() / shortcutCount - shortcutSize) / 2;

        mView.setPivotX(0);
        mView.setPivotY(0);

        if (isPortrait(orientation)) {
            int left = mContainerRect.left;
            int top = (int) (mContainerRect.top + mAnchorRect.centerY()
                    + mDialogWidth / 2f);
            if (top < mContainerRect.top + marginVertical + mDialogWidth) {
                top = mContainerRect.top + marginVertical + mDialogWidth;
            } else if (top + mDialogWidth > mContainerRect.bottom - marginVertical + mDialogWidth) {
                top = mContainerRect.bottom - marginVertical - mDialogWidth + mDialogWidth;
            }

            mView.setLeft(left);
            mView.setRight(left + mDialogWidth);
            mView.setTop(top);
            mView.setBottom(top + mDialogHeight);
            mDialogRect = new Rect(
                    (int) mView.getX(),
                    (int) mView.getY() - mDialogWidth,
                    (int) mView.getX() + mDialogHeight,
                    (int) mView.getY());
        } else {
            int left = mContainerRect.left;
            int top = (int) (mContainerRect.top + mAnchorRect.centerY() - mDialogHeight / 2f);
            if (top < mContainerRect.top + marginVertical) {
                top = mContainerRect.top + marginVertical;
            } else if (top + mDialogHeight > mContainerRect.bottom - marginVertical) {
                top = mContainerRect.bottom - marginVertical - mDialogHeight;
            }

            mView.setLeft(left);
            mView.setRight(left + mDialogWidth);
            mView.setTop(top);
            mView.setBottom(top + mDialogHeight);
            mDialogRect = new Rect(
                    (int) mView.getX(),
                    (int) mView.getY(),
                    (int) mView.getX() + mDialogWidth,
                    (int) mView.getY() + mDialogHeight);
        }
        mView.setRotation(RotationUtil.getAngle(orientation));
    }

    /**
     * Set dialog size.
     */
    @Override
    public void coordinateSize(int orientation) {
        LayoutParams params = mView.getLayoutParams();

        int containerHeight = isPortrait(orientation)
                ? mContainerRect.width()
                : mContainerRect.height();
        int maxHeight = containerHeight - mMaxHeightMargin;

        if (mView.computeHeight(1) > maxHeight && !isPortrait(orientation)) {
            mView.setNumColumns(2);

            int numRows = mView.getNumRows(maxHeight);
            params.height = Math.min(mView.computeMaxHeight(numRows), mView.computeHeight(2));
            params.width = mView.computeWidth(2);

        } else {
            mView.setNumColumns(1);

            int numRows = mView.getNumRows(maxHeight);
            params.height = Math.min(mView.computeMaxHeight(numRows), mView.computeHeight(1));
            params.width = mView.computeWidth(1);
        }

        mDialogWidth = params.width;
        mDialogHeight = params.height;
    }

    @Override
    public Rect getDialogRect() {
        return mDialogRect;
    }

    private boolean isPortrait(int orientation) {
        return (orientation == Configuration.ORIENTATION_PORTRAIT);
    }
}
