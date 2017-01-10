

package com.example.zhaoxukl1314.settingdialog;

import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.animation.Animation;

public interface SettingDialogInterface{

    /**
     * Open dialog
     *
     * @param parentView ViewGroup to add setting dialog view
     */
    void open(ViewGroup parentView);

    /**
     * Close dialog
     */
    void close();

    /**
     * Set the adapter that provides the data and the views to represent the data in this widget.
     *
     * @param adapter The adapter to use to create this view's content
     */
    void setAdapter(SettingAdapter adapter);

    void show();

    /**
     * Set the layout coordinator to coordinate layout of this setting dialog.
     * @param coordinator layout coordinator
     */
    void setLayoutCoordinator(LayoutCoordinator coordinator);

    boolean hitTest(int x, int y);
}
