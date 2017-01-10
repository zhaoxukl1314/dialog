

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class SettingIcon extends SettingDialogItem {

    public static final String TAG = "SettingIcon";

    private static final int ICON_FADE_SWITCH_ANIMATION_DURATION = 250;
    private static final int ICON_FADE_SWITCH_ANIMATION_OFFSET = 50;

    /** Un-enable icon color filter color. */
    private static final int DISABLED_FILTER = R.color.disabled_filter;

    private final ViewHolder mHolder;

    private final OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (getView().isShown()) {
                if (getItem().isSelectable()) {
                    select(getItem());
                }
            }
        }
    };

    static class ViewHolder {
        FrameLayout mContainer;
        FrameLayout mBackground;
        ImageView mIcon;
        int mIconRes;
    }

    public SettingIcon(Context context, SettingItem item) {
        super(item);

        mHolder = new ViewHolder();
        mHolder.mContainer = new FrameLayout(context);

        mHolder.mBackground = new FrameLayout(context);
        mHolder.mContainer.addView(mHolder.mBackground);
        mHolder.mBackground.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
        mHolder.mBackground.getLayoutParams().height = LayoutParams.WRAP_CONTENT;

        mHolder.mIcon = new ImageView(context);
        mHolder.mBackground.addView(mHolder.mIcon);
        mHolder.mIcon.getLayoutParams().width = context.getResources().getDimensionPixelSize(
                R.dimen.shortcut_icon_background_width);
        mHolder.mIcon.getLayoutParams().height = context.getResources().getDimensionPixelSize(
                R.dimen.shortcut_icon_background_height);
        mHolder.mIcon.setScaleType(ScaleType.CENTER);
        ((FrameLayout.LayoutParams)mHolder.mIcon.getLayoutParams()).gravity = Gravity.CENTER;

        mHolder.mIconRes = ResourceUtil.INVALID_RESOURCE_ID;
    }

    @Override
    public void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params) {

        final Context context = mHolder.mContainer.getContext();

        mHolder.mBackground.getLayoutParams().width = LayoutParams.MATCH_PARENT;
        mHolder.mBackground.getLayoutParams().height = params.height;

        if (getItem().getIconId() != ResourceUtil.INVALID_RESOURCE_ID) {
            mHolder.mContainer.setVisibility(View.VISIBLE);

            mHolder.mIcon.setClickable(true);
            mHolder.mIcon.setOnClickListener(mOnClickListener);

            if (mHolder.mIconRes != getItem().getIconId() &&
                    mHolder.mIconRes != ResourceUtil.INVALID_RESOURCE_ID &&
                    !getItem().isSelected()) {
                mHolder.mIcon.startAnimation(createIconAnimation(mHolder.mContainer.getContext()));
            }
            mHolder.mIconRes = getItem().getIconId();
            mHolder.mIcon.setImageResource(getItem().getIconId());
            mHolder.mIcon.setBackgroundResource(R.drawable.setting_shortcut_selector);

            mHolder.mIcon.setSelected(getItem().isSelected());

            // grayout if not selectable
            if (getItem().isSelectable()) {
                mHolder.mIcon.clearColorFilter();
            } else {
                mHolder.mIcon.setColorFilter(DISABLED_FILTER);
            }

        } else {
            mHolder.mContainer.setVisibility(View.INVISIBLE);

            mHolder.mIcon.setClickable(false);
            mHolder.mIcon.setOnClickListener(null);

            mHolder.mIconRes = ResourceUtil.INVALID_RESOURCE_ID;
            mHolder.mIcon.setImageDrawable(null);
            mHolder.mIcon.setBackground(null);
            mHolder.mIcon.setSelected(false);
        }
    }

    @Override
    public View getView() {
        return mHolder.mContainer;
    }

    private Animation createIconAnimation(Context context) {
        ScaleAnimation scale = new ScaleAnimation(
                0.0f, 1.0f, 0.0f, 1.0f,
                Animation.ABSOLUTE, mHolder.mIcon.getLayoutParams().width / 2.0f,
                Animation.ABSOLUTE, mHolder.mIcon.getLayoutParams().height / 2.0f);

        scale.setInterpolator(new OvershootInterpolator());
        scale.setDuration(ICON_FADE_SWITCH_ANIMATION_DURATION);
        scale.setStartOffset(ICON_FADE_SWITCH_ANIMATION_OFFSET);

        return scale;
    }

    public ImageView getIcon() {
        return mHolder.mIcon;
    }
}
