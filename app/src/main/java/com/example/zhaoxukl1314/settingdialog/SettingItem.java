package com.example.zhaoxukl1314.settingdialog;

import android.content.res.Resources;

import java.util.List;

/**
 * This class represents setting item.
 *
 */
public interface SettingItem {


    /**
     *  Reader of resource id.
     */
    int getIconId();
    String getText(Resources resources);
    String getSubText(Resources resources);
    String getLongText(Resources resources);
    String getContentDescription(Resources resources);

    /**
     * Select this setting item and run the specified SettingExecutor.
     */
    void select();

    /**
     * Return true and this item is highlighted in setting dialog if this item is selected.
     */
    boolean isSelected();

    /**
     * Return true and this item is visible in setting dialog if this item is selected.
     */
    boolean isSelectable();

    void setSelected(boolean value);

    void setSelectable(boolean value);

    int getDialogItemType();

    List<SettingItem> getChildren();

    void setOnSelectedListener(OnItemSelectedListener listener);

    boolean compareData(SettingItem item);

    boolean compareData(Object data);

    boolean isSoundEnabled();
}

