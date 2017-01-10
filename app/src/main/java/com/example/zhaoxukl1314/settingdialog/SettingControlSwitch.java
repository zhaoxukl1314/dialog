

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingControlSwitch extends SettingDialogItem {
    public static final String TAG = "SettingControlSwitch";

    private final ViewHolder mHolder;

    private final OnCheckedChangeListener mOnCheckedChangeListener =
            new SwitchOnCheckedChangeListener();

    private final Resources mResources;

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
        ControlSwitch mSwitch;
    }

    public SettingControlSwitch(Context context, SettingItem item) {
        super(item);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mHolder = new ViewHolder();
        mHolder.mContainer = inflater.inflate(R.layout.setting_item_control_switch, null);
        mHolder.mSwitch = (ControlSwitch) mHolder.mContainer.findViewById(R.id.switch_layout);
        mResources = mHolder.mContainer.getContext().getResources();
    }

    /**
     * Update switch view.
     */
    @Override
    public void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params) {


        mHolder.mSwitch.setText(getItem().getText(mResources));
        mHolder.mContainer.setClickable(true);
        mHolder.mSwitch.setEnabled(getItem().isSelectable());

        // Should not notify a setting changed event unless user activities.
        // So release onCheckedChangeListener temporary.
        mHolder.mSwitch.setOnCheckedChangeListener(null);
        mHolder.mSwitch.setChecked(getOnItem().isSelected());
        mHolder.mSwitch.setOnCheckedChangeListener(mOnCheckedChangeListener);
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
