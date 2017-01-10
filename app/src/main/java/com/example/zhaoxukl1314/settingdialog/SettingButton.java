package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingButton extends SettingDialogItem {
    public static final String TAG = "SettingButton";

    private final ViewHolder mHolder;

    private final OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (getView().isShown()) {
                select(getItem());
            }
        }
    };

    private static class ViewHolder {
        View mContainer;
        View mBackground;
        View mDivider;
        View mCenterDividerRight;
        View mCenterDividerLeft;
        ImageView mImage;
        TextView mText;
    }

    public SettingButton(Context context, SettingItem item) {
        super(item);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mHolder = new ViewHolder();
        mHolder.mContainer = inflater.inflate(R.layout.setting_item_button, null);
        mHolder.mBackground = mHolder.mContainer.findViewById(R.id.background);
        mHolder.mDivider = mHolder.mContainer.findViewById(R.id.setting_divider);
        mHolder.mCenterDividerRight = mHolder.mContainer
                .findViewById(R.id.setting_center_divider_right);
        mHolder.mCenterDividerLeft = mHolder.mContainer
                .findViewById(R.id.setting_center_divider_left);
        mHolder.mImage = (ImageView) mHolder.mContainer.findViewById(R.id.icon);
        mHolder.mText = (TextView) mHolder.mContainer.findViewById(R.id.text);

        mHolder.mBackground.setClickable(true);
    }

    @Override
    public void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params) {
        final Resources res = mHolder.mContainer.getContext().getResources();

        mHolder.mText.setText(getItem().getText(res));
        mHolder.mImage.setImageResource(getItem().getIconId());

        mHolder.mBackground.setClickable(true);
        mHolder.mBackground.setOnClickListener(mOnClickListener);
        mHolder.mBackground.setSelected(getItem().isSelected());
        mHolder.mBackground.getLayoutParams().width = LayoutParams.MATCH_PARENT;
        mHolder.mBackground.getLayoutParams().height = params.height;

        changeDrawableState(params)
                .background(mHolder.mBackground)
                .dividerHorizontal(mHolder.mDivider)
                .dividerVertical(mHolder.mCenterDividerLeft, mHolder.mCenterDividerRight)
                .apply();
    }

    @Override
    public View getView() {
        return mHolder.mContainer;
    }
}

