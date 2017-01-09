

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
     * Open dialog
     *
     * @param parentView ViewGroup to add setting dialog view
     * @param isNested A flag to indicate the dialog is nested
     */
    void open(ViewGroup parentView, boolean isNested);

    /**
     * Close dialog
     */
    void close();

    /**
     * Returns true if the dialog is nested
     *
     * @return true if the dialog is nested
     */
    boolean isNested();

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

    /**
     * Set the animation when the setting dialog is closed.
     *
     * @param animation Animation when the setting dialog is closed
     */
    void setCloseAnimation(Animation animation);

    /**
     * Set a callback to be invoked when a setting dialog state is changed.
     * @param listener setting dialog state listener
     */
    void setStateListener(SettingDialogStateListener listener);

    boolean getSelectedItemRect(Rect rect);

    boolean hitTest(int x, int y);

    void setEnabled(boolean enable);

    /**
     * Get layout coordinator.
     */
    LayoutCoordinator getLayoutCoordinator();
}
