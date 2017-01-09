

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class SettingControlDialog extends SettingDialog {

    private static final String TAG = "SettingControlDialog";

    private final int CONTROL_DIALOG_LEFT_PADDING =
            getPixel(R.dimen.control_dialog_left_padding);

    private Drawable mBackground;

    private LinearLayout mItems;
    private SettingAdapter mAdapter;

    public SettingControlDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getPixel(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setWillNotDraw(false);
        mItems = (LinearLayout) findViewById(R.id.dialog_items);
    }

    @Override
    protected void onAttachedToWindow() {
//        mBackground = getResources().getDrawable(
//                R.drawable.cam_shortcut_dialog_background_icn, null);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mBackground = null;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        // Draw a background image. This image must be rotated if the device is portrait.
        drawBackground(canvas);
        canvas.restore();

        super.onDraw(canvas);
    }

    @Override
    public void setAdapter(SettingAdapter adapter) {
        mAdapter = adapter;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        for (int i = 0; i < mAdapter.getCount(); i++) {

            if (i < mItems.getChildCount()) {
                View org = mItems.getChildAt(i);
                View mod = mAdapter.getView(i, org, mItems);

                if (org != mod) {
                    mItems.removeViewAt(i);
                    mItems.removeView(mod);
                    mItems.addView(mod, i, params);
                }
            } else {
                mItems.addView(mAdapter.getView(i, null, mItems), params);
            }

        }

        while (mItems.getChildCount() > mAdapter.getCount()) {
            mItems.removeViewAt(mItems.getChildCount() - 1);
        }

        updateItems();
    }

    private void drawBackground(Canvas canvas) {
        if (mBackground == null) {
            return;
        }

        // Rotate background by center of dialog according orientation.
        canvas.rotate(
                RotationUtil.getAngle(mOrientation),
                getMeasuredWidth() / 2f,
                getMeasuredHeight() / 2f);

        mBackground.setBounds(computeBackgroundRectBeforeRotation());
        mBackground.draw(canvas);
    }

    private Rect computeBackgroundRectBeforeRotation() {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            Rect rect = new Rect(0, 0, getMeasuredHeight(), getMeasuredWidth());
            // Move background to center of dialog.
            rect.offset(
                    -(rect.width() - getMeasuredWidth()) / 2,
                    -(rect.height() - getMeasuredHeight()) / 2);
            return rect;

        } else {
            return new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
    }

    private void updateItems() {

        for (int i = 0; i < mItems.getChildCount(); i++) {
            View v = mItems.getChildAt(i);
            if (v.getTag() instanceof SettingItem) {;
                if (((SettingItem)v.getTag()).isSelectable()) {
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.setVisibility(View.GONE);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialog
     */
    @Override
    public void setSensorOrientation(int orientation) {
        requestLayout();

        // rotate item of dialog
        for (int i = 0; i < mItems.getChildCount(); i++) {
            if (mItems.getChildAt(i).getTag() instanceof SettingDialogItem) {
                SettingDialogItem item = (SettingDialogItem) mItems.getChildAt(i).getTag();
                item.setUiOrientation(orientation);
            }
        }
        super.setSensorOrientation(orientation);
    }

    @Override
    public boolean getSelectedItemRect(Rect rect) {
        return false;
    }

    public int getControlDialogLeftPadding() {
        return CONTROL_DIALOG_LEFT_PADDING;
    }
}