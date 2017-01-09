package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;


/**
 * This class provides the function to manage a location of dialog and stacking.
 */
public class SettingDialogStack {

    public static final String TAG = "SettingDialogStack";

    private final Context mContext;
    private final ViewGroup mDialogBackground;

    private int mOrientation;

    private SettingTabDialogBasic mMenuDialog;

    private SettingLayoutCoordinatorFactory.LayoutCoordinateData mMenuDialogCoordinateData;

    private SettingDialogAnimation mSettingAnimation;

    private boolean mIsMenuDialogOpened;

    private OnKeyListener mOnInterceptKeyListener;

    /**
     * settingShortcutItems Sometimes SettingShortcut constructor in this function
     * takes 100ms. This is bad for the performance. If prevent this, create ListView object
     * in advance and set it as this parameter "settingShortcutItems".
     */
    public SettingDialogStack(
            Context context,
            ViewGroup dialogContainer) {
        mContext = context;

        setOnInterceptKeyListener(null);
        mDialogBackground = new Background(mContext);
        dialogContainer.addView(mDialogBackground);
        mDialogBackground.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        mDialogBackground.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        mMenuDialog = null;

        mDialogBackground.setClickable(true);
        mDialogBackground.setFocusable(false);

        mIsMenuDialogOpened = false;

        mSettingAnimation = new SettingDialogAnimation(context);
    }

    public boolean isDialogOpened() {
        return mMenuDialog != null;
    }

    /**
     * Allow a client to customize the handling of key events using this listener.
     */
    public void setOnInterceptKeyListener(OnKeyListener listener) {
        if (listener == null) {
            mOnInterceptKeyListener = DUMMY_ON_INTERCEPT_KEY_LISTENER;
        }  else {
            mOnInterceptKeyListener = listener;
        }
    }

    public boolean openMenuDialog(
            SettingAdapter adapter,
            SettingTabs.Tab[] tabs,
            int menuDialogRowCount) {

        if (mMenuDialog != null) {
            return false;
        }

        boolean alreadyOpened = isDialogOpened();
        boolean isAnimation = false;

        if (!mIsMenuDialogOpened) {
            isAnimation = true;
        }

        // close all dialogs
        closeMenuDialog(false);

        mMenuDialogCoordinateData = generateMenuDialogLayoutCoordinateData();
        if (mMenuDialogCoordinateData != null) {
            mMenuDialog = SettingDialogFactory.createMenu(
                    mContext,
                    mMenuDialogCoordinateData,
                    menuDialogRowCount,
                    tabs.length);
            mMenuDialog.setTabs(tabs);
            mMenuDialog.setAdapter(adapter);

            // set animation
            if ((!mIsMenuDialogOpened) && (isAnimation)) {
                mSettingAnimation.setOpenDialogAnimation(mDialogBackground, mOrientation);
            }

            mMenuDialog.open(mDialogBackground);
            mMenuDialog.setSensorOrientation(mOrientation);

            mIsMenuDialogOpened = true;
        }

        resetEnabledOfDialogs();
        mDialogBackground.requestFocus();

        return true;
    }

    public boolean closeCurrentDialog() {

        // close the top layered dialog.
        boolean handled = false;
        if (!handled) {
            handled = closeMenuDialog(true);
        }
        resetEnabledOfDialogs();

        if (handled) {
            if (!isDialogOpened()) {
                mDialogBackground.clearFocus();
            } else {
            }
        }

        return handled;
    }

    public void closeDialogs() {
        closeDialogs(false);
    }

    public void closeDialogs(boolean withAnimation) {
        // close the top layered dialog.

        boolean handled = false;
        handled |= closeMenuDialog(withAnimation);
        resetEnabledOfDialogs();

        if (handled) {
            // notify onCloseSettingDialog event if all dialogs are closed.
            if (!isDialogOpened()) {
                mDialogBackground.clearFocus();
            }
        }
    }

    private SettingDialogInterface getCurrentDialog() {

        for (SettingDialogInterface dialog : getDialogList()) {
            if (dialog != null) {
                return dialog;
            }
        }
        return null;
    }

    private void resetEnabledOfDialogs() {

        final SettingDialogInterface[] dialogs = {
                mMenuDialog
        };

        SettingDialogInterface current = getCurrentDialog();

        for (SettingDialogInterface dialog : dialogs) {
            if (dialog != null) {
                dialog.setEnabled(dialog == current);
            }
        }
    }

    private boolean closeMenuDialog(boolean isAnimation) {
        if (mMenuDialog != null) {
            // set animation.
            if (isAnimation) {
                mSettingAnimation.setCloseDialogAnimation(mMenuDialog, mOrientation);
            }

            mMenuDialog.close();
            mMenuDialog = null;
            mIsMenuDialogOpened = false;

            return true;
        }
        return false;
    }


    /**
     * This dialogs are sorted in z-order.
     * index:0 front
     * ...
     * index:n
     */
    private SettingDialogInterface[] getDialogList() {
        return new SettingDialogInterface[] {
                mMenuDialog
        };
    }

    private SettingLayoutCoordinatorFactory.LayoutCoordinateData generateMenuDialogLayoutCoordinateData() {
        Rect container = getContainerRect();
        if (container == null) {
            return null;
        }
        return new SettingLayoutCoordinatorFactory.LayoutCoordinateData(container, null);
    }

    private Rect getContainerRect() {
        Rect container = new Rect();
        if (!mDialogBackground.getGlobalVisibleRect(container)) {
            return null;
        }

        Resources res = mContext.getResources();
        int marginLeft = res.getDimensionPixelSize(R.dimen.left_container_width);
        int marginRight = res.getDimensionPixelSize(R.dimen.right_container_width);
        container.set(
                marginLeft,
                0,
                container.width() - marginLeft - marginRight,
                container.height());

        return container;
    }

    /**
     * This view handles a touch event and key event to close dialog.
     */
    private class Background extends FrameLayout {

        public Background(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            SettingDialogInterface current = getCurrentDialog();

            if (current != null) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (!current.hitTest(
                                (int) event.getRawX(), (int) event.getRawY())) {
                            closeDialogs(true);
                        }
                        return true;
                }
            }

            return false;
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            // Allow a client to customize the handling of key events.
            // @see {@link SettingDialogStack#setOnInterceptKeyListener}
            final boolean intercepted = mOnInterceptKeyListener.onKey(this, keyCode, event);

            if (!intercepted) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_CAMERA:
                    case KeyEvent.KEYCODE_FOCUS:
                        closeDialogs();
                        break;

                    case KeyEvent.KEYCODE_BACK:
                        return isDialogOpened();

                    default:
                        break;
                }
            }

            return false;
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return closeCurrentDialog();

                default:
                    break;
            }

            return false;
        }
    }

    private static final OnKeyListener DUMMY_ON_INTERCEPT_KEY_LISTENER = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            return false;
        }
    };
}

