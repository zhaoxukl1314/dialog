

package com.example.zhaoxukl1314.settingdialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
public class SettingCategoryButton extends SettingDialogItem {
    public static final String TAG = "SettingCategoryButton";

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
        TextView mCategory;
        TextView mValue;
    }

    /**
     * Constructor
     *
     * @param context
     *      current running context
     * @param item
     *      setting item
     * @param valueId
     *      setting item value
     */
    public SettingCategoryButton(Context context, SettingItem item) {
        super(item);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mHolder = new ViewHolder();
        mHolder.mContainer = inflater.inflate(R.layout.setting_item_category_button, null);
        mHolder.mBackground = mHolder.mContainer.findViewById(R.id.background);
        mHolder.mDivider = mHolder.mContainer.findViewById(R.id.setting_divider);
        mHolder.mCategory = (TextView) mHolder.mContainer.findViewById(R.id.category);
        mHolder.mValue = (TextView) mHolder.mContainer.findViewById(R.id.value);
    }

    /**
     * Update view.
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params) {
        final Resources res = mHolder.mContainer.getContext().getResources();
        mHolder.mCategory.setText(getItem().getText(res));

        SettingItem selected = getSelectedItem();
        if (selected != null) {
            // This process is case "Resolution" or "Video resolution".
            if ((selected.getLongText(res) != null) &&
                    (selected.getLongText(res).length() > 0)) {
                mHolder.mValue.setText(selected.getLongText(res));
            } else {
                // if item has a current value.
                mHolder.mValue.setText(selected.getText(res));
            }
            mHolder.mValue.setVisibility(View.VISIBLE);

        } else {
            SettingItem item = getItem();
            if (item instanceof TypedSettingItem) {
                String value = null;
                // This process is case "Resolution" or "Video resolution".
                if ((item.getLongText(res) != null) &&
                        (item.getLongText(res).length() > 0)) {
                    value = item.getLongText(res);
                } else {
                    value = ((TypedSettingItem)item).getValueText();
                }
                if (value != null && 0 < value.length()) {
                    mHolder.mValue.setText(value);
                    mHolder.mValue.setVisibility(View.VISIBLE);
                } else {
                    mHolder.mValue.setVisibility(View.GONE);
                }
            } else {
                // if item doesn't have a current value.
                mHolder.mValue.setVisibility(View.GONE);
            }
        }

        mHolder.mBackground.setClickable(true);
        mHolder.mBackground.setOnClickListener(mOnClickListener);



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
        mHolder.mCategory.setTextColor(textColor);
        mHolder.mValue.setTextColor(textColor);

        // Set content description.
        mHolder.mBackground.setContentDescription(getItem().getContentDescription(res));

        changeDrawableState(params)
                .background(mHolder.mBackground)
                .dividerHorizontal(mHolder.mDivider)
                .apply();
    }

    /**
     * Get view.
     */
    @Override
    public View getView() {
        return mHolder.mContainer;
    }

    private SettingItem getSelectedItem() {
        for (SettingItem item : getItem().getChildren()) {
            if (item.isSelectable() && item.isSelected()) {
                return item;
            }
        }

        return null;
    }
}
