package com.example.zhaoxukl1314.settingdialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Switch extends LinearLayout {
    /**
     * Tag for log.
     */
    public static final String TAG = "Switch";

    /**
     * Un-enable icon color filter color.
     */
    private static final int DISABLED_FILTER = R.color.disabled_filter;

    /**
     * State.(ON or OFF)
     */
    private boolean mIsChecked;

    private ImageView mSwitchTrack;
    private ImageView mSwitchKnob;
    protected TextView mText;
    protected View mSwitchBundle;

    private static final int OFF_POSITION = 0;

    private int mOnPosition = 0;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mSwitchTrack = (ImageView) findViewById(R.id.switch_track);
        mSwitchKnob = (ImageView) findViewById(R.id.switch_knob);
        mText = (TextView) findViewById(R.id.switch_category);
        mSwitchBundle = (View) findViewById(R.id.switch_bundle);

        setOnClickListener(new SwitchOnClickListener());
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }

    public void setChecked(boolean isChecked) {
        mIsChecked = isChecked;
        updateIcon();
        updatePosition();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (isEnabled()) {
            if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
                mText.setTextColor(getResources().getColor(R.color.default_text_col, null));
            } else {
                mText.setTextColor(getResources().getColor(R.color.default_text_col));
            }
            mSwitchTrack.clearColorFilter();
        } else {
            mText.setTextColor(getResources().getColor(R.color.grayout_text_col, null));
            if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
                mText.setTextColor(getResources().getColor(R.color.grayout_text_col, null));
            } else {
                mText.setTextColor(getResources().getColor(R.color.grayout_text_col));
            }
            mSwitchTrack.setColorFilter(DISABLED_FILTER);
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    private void changeState() {
        mIsChecked = !mIsChecked;
        setChecked(mIsChecked);
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(null, mIsChecked);
        }
    }

    private void updatePosition() {
        float x = (mIsChecked ? mOnPosition : OFF_POSITION);
        mSwitchKnob.setTranslationX(x);
    }

    private void updateIcon() {
        if (mIsChecked) {
            mSwitchTrack.setImageResource(R.drawable.cam_setting_switch_on_bg_icn);
            mSwitchKnob.setImageResource(R.drawable.cam_setting_switch_on_icn);
        } else {
            mSwitchTrack.setImageResource(R.drawable.cam_setting_switch_off_bg_icn);
            mSwitchKnob.setImageResource(R.drawable.cam_setting_switch_off_icn);
        }

        // update thumb move width.
        mOnPosition = (mSwitchTrack.getDrawable().getIntrinsicWidth() -
                mSwitchKnob.getDrawable().getIntrinsicWidth());

    }

    private final class SwitchOnClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            changeState();
        }
    }
}

