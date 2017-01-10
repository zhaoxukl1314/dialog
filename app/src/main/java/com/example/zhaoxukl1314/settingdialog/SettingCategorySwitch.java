
package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingCategorySwitch extends SettingDialogItem {
    public static final String TAG = "SettingCategorySwitch";

    private final ViewHolder mHolder;

    private final OnCheckedChangeListener mOnCheckedChangeListener =
            new SwitchOnCheckedChangeListener();

    private final class SwitchOnCheckedChangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (getView().isShown()) {
                if (isChecked) {
                    select(getOnItem());
                } else {
                    select(getOffItem());
                }
            }
        }
    }

    private static class ViewHolder {
        View mContainer;
        View mDivider;
        CategorySwitch mSwitch;
    }

    /**
     * Constructor
     *
     * @param context
     *      current running context
     * @param item
     *      setting item
     * @param on
     *      switch of on
     * @param off
     *      switch of off
     */
    public SettingCategorySwitch(Context context, SettingItem item) {
        super(item);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mHolder = new ViewHolder();
        mHolder.mContainer = inflater.inflate(R.layout.setting_item_category_switch, null);
        mHolder.mDivider = mHolder.mContainer.findViewById(R.id.setting_divider);
        mHolder.mSwitch = (CategorySwitch) mHolder.mContainer.findViewById(R.id.switch_layout);
    }

    /**
     * Update switch view.
     */
    @Override
    public void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params) {
        final Resources res= mHolder.mContainer.getContext().getResources();

        mHolder.mSwitch.setText(getItem().getText(res));
        mHolder.mContainer.setClickable(true);
        mHolder.mSwitch.setEnabled(getItem().isSelectable());

        // Should not notify a setting changed event unless user activities.
        // So release onCheckedChangeListener temporary.
        mHolder.mSwitch.setOnCheckedChangeListener(null);
        mHolder.mSwitch.setChecked(getOnItem().isSelected());
        mHolder.mSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);

        changeDrawableState(params)
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

    private SettingItem getOnItem() {
        return getItem().getChildren().get(0);
    }

    private SettingItem getOffItem() {
        return getItem().getChildren().get(1);
    }
}
