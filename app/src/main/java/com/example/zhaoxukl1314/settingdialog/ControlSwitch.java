/*
 * Copyright (C) 2015 Sony Mobile Communications Inc.
 * All rights, including trade secret rights, reserved.
 */

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * This class shows Control dialog Switch.
 */
public class ControlSwitch extends Switch {
    /** Tag for log. */
    public static final String TAG = "ControlSwitch";

    private boolean mIsUpsideDown;

    public ControlSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public synchronized void setUiOrientation(int orientation) {
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                if (mIsUpsideDown) {
                    // Switch to "non upside down" layout
                    reverseChildrenViews(this);
                    mText.setRotation(0f);
                    synchronized (mSwitchBundle) {
                        mSwitchBundle.setRotation(0f);
                    }
                    mIsUpsideDown = false;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                if (!mIsUpsideDown) {
                    // Switch to "upside down" layout
                    reverseChildrenViews(this);
                    mText.setRotation(-90f);
                    synchronized (mSwitchBundle) {
                        mSwitchBundle.setRotation(-90f);
                    }
                    mIsUpsideDown = true;
                }
                break;
            default:
                // NOP.
                break;
        }
    }

    private void reverseChildrenViews(ViewGroup viewGroup) {
        List<View> views = new ArrayList<View>();
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            views.add(viewGroup.getChildAt(i));
        }
        viewGroup.removeAllViews();
        for(int i = views.size() - 1; i >= 0; i--) {
            viewGroup.addView(views.get(i));
        }
    }

}
