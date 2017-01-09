

package com.example.zhaoxukl1314.settingdialog;

import android.content.res.Resources;
import android.graphics.Rect;
import android.widget.LinearLayout.LayoutParams;

public class ControlLayoutCoordinator implements LayoutCoordinator {
    public static final String TAG = "ControlLayoutCoordinator";

    private final int mDialogHeight;

    private final SettingControlDialog mView;
    private final Rect mContainerRect;
    private final Rect mAnchorRect;

    public Rect mDialogRect;

    public ControlLayoutCoordinator(
            SettingControlDialog view,
            Rect containerRect,
            Rect anchorRect) {
        mView = view;
        mContainerRect = containerRect;
        mAnchorRect = anchorRect;

        Resources res = view.getResources();
        mDialogHeight = res.getDimensionPixelSize(R.dimen.control_setting_dialog_height);
    }

    @Override
    public void coordinatePosition(int orientation) {
            coordinatePositionPhone(orientation);
    }

    private void coordinatePositionPhone(int orientation) {
        mView.setX(mContainerRect.left);
        mView.setY(mContainerRect.top +
                (mContainerRect.height() - mView.getLayoutParams().height) / 2.0f);

        mDialogRect = new Rect(
                (int) mView.getX(),
                (int) mView.getY(),
                (int) mView.getX() + mView.getLayoutParams().width,
                (int) mView.getY() + mView.getLayoutParams().height);
    }

    @Override
    public void coordinateSize(int orientation) {
        mView.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
        mView.getLayoutParams().height = mDialogHeight
                + mView.getPaddingTop()
                + mView.getPaddingBottom();
    }

    @Override
    public Rect getDialogRect() {
        return mDialogRect;
    }
}
