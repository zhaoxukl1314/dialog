

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public abstract class SettingDialog extends RelativeLayout implements SettingDialogInterface {
    public static final String TAG = "SettingDialog";

    private LayoutCoordinator mLayoutCoordinator;
    protected int mOrientation = Configuration.ORIENTATION_LANDSCAPE;
    private Animation mCloseAnimation;
    private SettingDialogStateListener mStateListener;
    private ViewGroup mParentView;
    private boolean mIsNested = false;

    public SettingDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mLayoutCoordinator != null) {
            mLayoutCoordinator.coordinatePosition(mOrientation);
        }
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface
     */
    @Override
    public void setSensorOrientation(int orientation) {
        mOrientation = orientation;

        if (mLayoutCoordinator != null) {
            mLayoutCoordinator.coordinateSize(orientation);
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

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.
     * SettingDialogInterface#open(android.view.ViewGroup)
     */
    @Override
    public void open(ViewGroup parentView) {
        open(parentView, false);
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface
     */
    @Override
    public void open(ViewGroup parentView, boolean isNested) {
        if (parentView == null) {
            throw new IllegalArgumentException("Parent view shouldn't be null");
        }

        mIsNested = isNested;
        mParentView = parentView;
        mParentView.addView(this);

        if (mStateListener != null) {
            mStateListener.onOpened();
        }
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface
     */
    @Override
    public boolean isNested() {
        return mIsNested ;
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface
     */
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

        if (mStateListener != null) {
            mStateListener.onClosed();
        }
    }

    protected void startAnimation() {
        if (mCloseAnimation == null) {
            return;
        }

        cancelAnimation();

        mCloseAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //NOP
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //NOP
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                close();
            }
        });

        mParentView.startAnimation(mCloseAnimation);
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

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialogInterface
     */
    @Override
    public void setStateListener(SettingDialogStateListener listener) {
        mStateListener = listener;
    }

    protected void notifyItemSelected(SettingItem item) {
        if (item.isSelectable() == false) {
            return;
        }

        item.select();
        close();
    }

    protected void notifyItemUpdated(SettingItem item) {
        item.select();
    }

    protected void updateSelectItem(SettingAdapter adapter, SettingItem selectedItem) {
        for (int i = 0; i < adapter.getCount(); i++) {
            adapter.getItem(i).setSelected(false);
        }

        selectedItem.setSelected(true);
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
