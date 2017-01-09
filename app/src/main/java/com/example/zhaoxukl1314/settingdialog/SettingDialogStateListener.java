

package com.example.zhaoxukl1314.settingdialog;

public interface SettingDialogStateListener {
    /**
     * Called when a setting dialog is opened.
     */
    void onOpened();

    /**
     * Called when a setting dialog is closed.
     */
    void onClosed();

    /**
     * Called when a setting dialog is expanded.
     */
    void onExpanded();
}
