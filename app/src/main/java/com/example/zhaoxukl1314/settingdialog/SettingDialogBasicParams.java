
package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;

public enum SettingDialogBasicParams {

    SHORTCUT_DIALOG_PARAMS(
            R.dimen.shortcut_dialog_item_height,
            R.dimen.shortcut_dialog_padding,
            R.drawable.cam_shortcut_dialog_background_icn),
    SECOND_LAYER_DIALOG_SINGLE_ITEM_PARAMS(
            R.dimen.second_layer_dialog_item_double_line_height,
            R.dimen.second_layer_dialog_padding,
            R.drawable.cam_setting_sub_dialog_background_icn),
    SECOND_LAYER_DIALOG_DOUBLE_ITEM_PARAMS(
            R.dimen.second_layer_dialog_item_double_line_height,
            R.dimen.second_layer_dialog_padding,
            R.drawable.cam_setting_sub_dialog_background_icn);

    SettingDialogBasicParams(
            int itemHeight,
            int padding,
            int backgroundId) {
        mItemHeightResId = itemHeight;
        mPaddingResId = padding;
        mBackgroundId = backgroundId;
    }

    public static final String TAG = "SettingDialogBasicParams";

    private final int mItemHeightResId;
    private final int mPaddingResId;
    private final int mBackgroundId;

    public int getItemHeight(Context context) {
        return context.getResources().getDimensionPixelSize(mItemHeightResId);
    }

    public int getPadding(Context context) {
        return context.getResources().getDimensionPixelSize(mPaddingResId);
    }

    public int getBackgroundId() {
        return mBackgroundId;
    }
}
