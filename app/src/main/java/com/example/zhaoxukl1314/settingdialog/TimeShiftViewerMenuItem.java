

package com.example.zhaoxukl1314.settingdialog;

public enum TimeShiftViewerMenuItem{
    SAVE_SEPERATELY(
            R.string.cam_strings_timeshift_save_selection_txt),
    SHARE(
            R.string.cam_strings_timeshift_share_txt),
    SELECT_PHOTOS(
            R.string.cam_strings_timeshift_select_images_txt),
    ;

    /** Other applications may refer to the title id. */
    private int mTitleTextId;

    private TimeShiftViewerMenuItem(int titleTextId) {
        mTitleTextId = titleTextId;
    }

    /**
     * Get Title Id for the value.
     *
     * @return Title Id.
     */
    public int getTitleId() {
        return mTitleTextId;
    }

    /**
     * Get Icon Id for the value.
     */
    public int getIconId() {
        // This function is never used.
        return 0;
    }

    /**
     * Get Text Id for the value.
     */
    public int getTextId() {
        // This function is never used.
        return 0;
    }
}
