

package com.example.zhaoxukl1314.settingdialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingValueButton extends SettingDialogItem {
    public static final String TAG = "SettingValueButton";

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

    private static class ViewHolder {
        View mContainer;
        View mBackground;
        View mDivider;
        TextView mText;
    }

    public SettingValueButton(Context context, SettingItem item) {
        super(item);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mHolder = new ViewHolder();
        mHolder.mContainer = inflater.inflate(R.layout.setting_item_value_button, null);
        mHolder.mBackground = mHolder.mContainer.findViewById(R.id.background);
        mHolder.mDivider = mHolder.mContainer.findViewById(R.id.setting_divider);
        mHolder.mText = (TextView) mHolder.mContainer.findViewById(R.id.title);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params) {
        final Resources res = mHolder.mContainer.getContext().getResources();

        mHolder.mText.setText(getItem().getText(res));
        int textColor = 0;
        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            textColor = getItem().isSelectable()
                    ? parent.getResources().getColor(R.color.default_text_col, null)
                    : parent.getResources().getColor(R.color.grayout_text_col, null);
        } else {
            textColor = getItem().isSelectable()
                    ? parent.getResources().getColor(R.color.default_text_col)
                    : parent.getResources().getColor(R.color.grayout_text_col);
        }
        mHolder.mText.setTextColor(textColor);

        mHolder.mBackground.setClickable(true);
        mHolder.mBackground.setOnClickListener(mOnClickListener);
        mHolder.mBackground.setSelected(getItem().isSelected());

        // Set content description.
        mHolder.mBackground.setContentDescription(getItem().getContentDescription(res));

        mHolder.mBackground.getLayoutParams().width = LayoutParams.MATCH_PARENT;
        mHolder.mBackground.getLayoutParams().height = params.height;

        mHolder.mText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        changeDrawableState(params)
            .background(mHolder.mBackground)
            .dividerHorizontal(mHolder.mDivider)
            .apply();
    }

    @Override
    public View getView() {
        return mHolder.mContainer;
    }
}
