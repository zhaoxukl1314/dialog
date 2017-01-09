package com.example.zhaoxukl1314.settingdialog;

/**
 * This event send when the all dialogs in SettingDialogStack are closed.
 */
public interface SettingDialogListener {
    void onOpenSettingDialog(SettingDialogStack sender,
                             boolean isAlreadyOpened, boolean isAnimation);
    void onCloseSettingDialog(SettingDialogStack sender, boolean isAllClosed);
}

