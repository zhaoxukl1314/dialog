

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public abstract class SettingDialog extends RelativeLayout implements SettingDialogInterface {
    public static final String TAG = "SettingDialog";

    private LayoutCoordinator mLayoutCoordinator;
    private Animation mCloseAnimation;
    private ViewGroup mParentView;

    public SettingDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mLayoutCoordinator != null) {
            mLayoutCoordinator.coordinatePosition();
        }
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface
     */
    @Override
    public void show() {
        if (mLayoutCoordinator != null) {
            mLayoutCoordinator.coordinateSize();
        }
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface
     */
    @Override
    public void setLayoutCoordinator(LayoutCoordinator coordinator) {
        mLayoutCoordinator = coordinator;
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface
     */
    @Override
    public void setCloseAnimation(Animation animation) {
        mCloseAnimation = animation;
    }

    @Override
    public void open(ViewGroup parentView) {
        if (parentView == null) {
            throw new IllegalArgumentException("Parent view shouldn't be null");
        }

        mParentView = parentView;
        mParentView.addView(this);
    }

    @Override
    public void close() {
        cancelAnimation();

        if (mCloseAnimation != null) {
            startAnimation(mCloseAnimation);
        }

        Handler handler = getHandler();
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mParentView != null) {
                        mParentView.removeView(SettingDialog.this);
                    }
                }
            });
        }

    }

    protected void cancelAnimation() {
        if (mParentView == null) {
            return;
        }

        if (mParentView.getAnimation() != null) {
            mParentView.setAnimation(null);
            if (mCloseAnimation != null) {
                mCloseAnimation.setAnimationListener(null);
            }
        }
    }

    @Override
    abstract public void setAdapter(SettingAdapter adapter);

    @Override
    public boolean hitTest(int x, int y) {
        Rect rect = new Rect();
        if (getGlobalVisibleRect(rect)) {
            return rect.contains(x, y);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnabled()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public LayoutCoordinator getLayoutCoordinator() {
        return mLayoutCoordinator;
    }
}
