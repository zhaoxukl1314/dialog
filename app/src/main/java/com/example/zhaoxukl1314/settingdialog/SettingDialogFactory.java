
package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

class SettingDialogFactory {

    public static SettingTabDialogBasic createMenu(
            Context context,
            SettingLayoutCoordinatorFactory.LayoutCoordinateData coordinateData,
            int menuDialogRowCount) {

        SettingTabDialogBasic dialog = (SettingTabDialogBasic) inflate(
                context,
                R.layout.setting_dialog_menu);
        LayoutCoordinator coordinator
            = SettingLayoutCoordinatorFactory.createMenuLayoutCoordinator(
                    dialog,
                    coordinateData,
                    menuDialogRowCount);
        dialog.setLayoutCoordinator(coordinator);

        return dialog;
    }

    private static View inflate(Context context, int id) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(id, null);
    }
}
