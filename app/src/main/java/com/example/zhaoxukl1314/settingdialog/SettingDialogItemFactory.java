package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.view.ViewGroup;

/**
 * This class provides the function to create a class extended SettingDialogItem.
 */
public class SettingDialogItemFactory {

    public static final int BUTTON = 1;
    public static final int VALUE_BUTTON = 2;

    public SettingDialogItem create(SettingItem item, ViewGroup parent) {
        Context context = parent.getContext();

        if (item.getDialogItemType() == BUTTON) {
            return new SettingButton(context, item);

        } else if (item.getDialogItemType() == VALUE_BUTTON) {
            return new SettingValueButton(context, item);

        } else {
            throw new IllegalArgumentException(
                    "The specified type is unknown. type:" + item.getDialogItemType());
        }
    }
}

