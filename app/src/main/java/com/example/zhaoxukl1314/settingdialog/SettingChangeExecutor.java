package com.example.zhaoxukl1314.settingdialog;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class executes touch event of menu item.
 */
public class SettingChangeExecutor {

    private static final String TAG = SettingChangeExecutor.class.getSimpleName();
    private SettingDialogStack mSettingDialogStack;
    private ArrayList<Uri> mUriList;

    public SettingChangeExecutor(
            Activity activity, SettingDialogStack settingDialogStack) {
        mSettingDialogStack = settingDialogStack;
    }

    private class SaveSeperatelySettingExecutor implements
            SettingExecutorInterface<TimeShiftViewerMenuItem> {
        @Override
        public void onExecute(TypedSettingItem<TimeShiftViewerMenuItem> item) {
            mSettingDialogStack.closeDialogs(false);
        }
    }

    private class ShareSettingExecutor implements
            SettingExecutorInterface<TimeShiftViewerMenuItem> {
        @Override
        public void onExecute(TypedSettingItem<TimeShiftViewerMenuItem> item) {
            mSettingDialogStack.closeDialogs(false);
        }
    }

    private class SelectPhotoSettingExecutor implements
            SettingExecutorInterface<TimeShiftViewerMenuItem> {
        @Override
        public void onExecute(TypedSettingItem<TimeShiftViewerMenuItem> item) {
            mSettingDialogStack.closeDialogs(false);
        }
    }

    public SettingExecutorInterface<TimeShiftViewerMenuItem> getExecutor(
            final TimeShiftViewerMenuItem key) {
        switch (key) {
            case SAVE_SEPERATELY:
                return new SaveSeperatelySettingExecutor();

            case SHARE:
                return new ShareSettingExecutor();

            case SELECT_PHOTOS:
                return new SelectPhotoSettingExecutor();

            default:
                return new SettingExecutorInterface<TimeShiftViewerMenuItem>() {
                    @Override
                    public void onExecute(TypedSettingItem<TimeShiftViewerMenuItem> item) {
                        // NOP
                    }
                };
        }
    }
}

