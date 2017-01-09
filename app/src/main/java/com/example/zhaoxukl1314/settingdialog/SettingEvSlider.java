

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class SettingEvSlider extends SettingDialogItem {

    public static final String TAG = "SettingEvSlider";

    private final ViewHolder mHolder;

    private final int mTopPadding;
    private final int mBottomPadding;

    private int mSelectedPosition;

    private static class ViewHolder {
        ViewGroup mContainer;
        View mBackground;
        ImageView mScaleNumber;
        ImageView mIndicator;
    }

    public SettingEvSlider(Context context, SettingItem item) {
        super(item);

        mTopPadding = context.getResources().
                getDimensionPixelSize(R.dimen.ev_slider_memory_top_padding);
        mBottomPadding = context.getResources().
                getDimensionPixelSize(R.dimen.ev_slider_memory_bottom_padding);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mHolder = new ViewHolder();
        mHolder.mContainer = new FrameLayout(context) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);

                updateIndicator(getSelectedItemDisplyPosition());
            }
        };
        mHolder.mContainer.addView(inflater.inflate(R.layout.setting_item_ev_slider, null));
        mHolder.mBackground = mHolder.mContainer.findViewById(R.id.background);
        mHolder.mIndicator = (ImageView) mHolder.mContainer.findViewById(R.id.indicator);
        mHolder.mScaleNumber = (ImageView) mHolder.mContainer.findViewById(R.id.scale_number);

        mHolder.mBackground.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setPressed(true);
                        updateIndicator(event.getY(), event.getAction());
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (view.isPressed()) {
                            updateIndicator(event.getY(), event.getAction());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (view.isPressed()) {
                            updateIndicator(event.getY(), event.getAction());
                        }
                        view.setPressed(false);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params) {
        updateIndicator(getSelectedItemDisplyPosition());
    }

    @Override
    public View getView() {
        return mHolder.mContainer;
    }

    private int getSelectedItemDisplyPosition() {
        for (int position = 0; position < getValueItemCount(); position++) {
            SettingItem item = getValueItem(position);
            if (item.isSelected()) {
                mSelectedPosition = position;
                return mSelectedPosition;
            }
        }

        return 0;
    }

    private SettingItem getValueItem(int position) {
        return getItem().getChildren().get(getValueItemCount() - position - 1);
    }

    private int getValueItemCount() {
        return getItem().getChildren().size();
    }

    private float getMemoryStepSize() {
        return (float)(mHolder.mBackground.getMeasuredHeight()
                - mTopPadding
                - mBottomPadding) / (float)(getItem().getChildren().size() - 1);
    }

    private void updateIndicator(float y, int action) {

        int position = (int)((y - mTopPadding ) / getMemoryStepSize());
        position = Math.max(position, 0);
        position = Math.min(position, getValueItemCount() - 1);
        updateIndicator(position);
    }

    private void updateIndicator(int position) {
        float currentposition = getMemoryStepSize() * (float)position + (float)mTopPadding;

        float indicatorY = currentposition - (mHolder.mIndicator.getMeasuredHeight() / (float) 2);
        mHolder.mIndicator.setY(indicatorY);

        if (position != mSelectedPosition) {
            // reset item selection
            for (SettingItem item : getItem().getChildren()) {
                item.setSelected(false);
            }
            // select new item
            getValueItem(position).select();
        }

        // Update current selected
        mSelectedPosition = position;
    }

    @Override
    public void setUiOrientation(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mHolder.mBackground.setBackgroundResource(
                    R.drawable.cam_ev_level_dialog_scale_land_icn);
            mHolder.mScaleNumber.setImageResource(
                    R.drawable.cam_ev_level_dialog_scale_number_land_icn);
            mHolder.mIndicator.setImageResource(
                    R.drawable.setting_ev_indicator_selector_land);
        } else {
            mHolder.mBackground.setBackgroundResource(
                    R.drawable.cam_ev_level_dialog_scale_port_icn);
            mHolder.mScaleNumber.setImageResource(
                    R.drawable.cam_ev_level_dialog_scale_number_port_icn);
            mHolder.mIndicator.setImageResource(
                    R.drawable.setting_ev_indicator_selector_port);
        }
    }
}
