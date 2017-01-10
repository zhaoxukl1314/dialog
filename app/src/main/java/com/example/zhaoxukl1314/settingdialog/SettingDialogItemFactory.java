package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.view.ViewGroup;

/**
 * This class provides the function to create a class extended SettingDialogItem.
 */
public class SettingDialogItemFactory {

    public int getDialogItemTypeCount() {
        return END_OF_TYPE_LIST;
    }

    public static final int ICON = 0;
    public static final int BUTTON = 1;
    public static final int VALUE_BUTTON = 2;
    public static final int CATEGORY_BUTTON = 3;
    public static final int CATEGORY_SWITCH = 4;
    public static final int EV = 5;
    public static final int MENU = 6;
    public static final int VERTICAL_ICON_LIST = 7;
    public static final int CONTROL_SWITCH = 8;
    public static final int END_OF_TYPE_LIST = 9;

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

