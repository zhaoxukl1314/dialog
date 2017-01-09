package com.example.zhaoxukl1314.settingdialog;

import android.view.View;
import android.view.ViewGroup;

import com.example.zhaoxukl1314.settingdialog.SettingAdapter;

/**
 * This is base class which provides the function to show a item in Setting Dialog.
 */
public abstract class SettingDialogItem {

    private SettingItem mItem;

    public SettingDialogItem(SettingItem item) {
        mItem = item;
    }

    public void setItem(SettingItem item) {
        mItem = item;
    }

    public abstract View getView();

    public abstract void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params);

    public SettingItem getItem() {
        return mItem;
    }

    public void select(SettingItem item) {
        item.select();
    }

    public void setUiOrientation(int orientation) {
        //NOP in default implements
    }

    /**
     * Start changing a drawable state according to the specified ItemLayoutParams.
     * The changes are applied after {@link DrawableStateChanger#apply()} is called.
     */
    protected DrawableStateChanger changeDrawableState(SettingAdapter.ItemLayoutParams params) {
        return new DrawableStateChanger(params);
    }
}

/**
 * This class provides the function to change a divider and background image
 * according to ItemLayoutParams.
 */
class DrawableStateChanger {
    /**
     * Change a background image according a position of item.
     * The levels are defined in setting_item_selector.xml
     */
    private static final int DRAWABLE_LEVEL_NORMAL = 0;
    private static final int DRAWABLE_LEVEL_TOP = 1;
    private static final int DRAWABLE_LEVEL_BOTTOM = 2;

    private final SettingAdapter.ItemLayoutParams mParams;
    private View mDividerBottom;
    private View mDividerLeft;
    private View mDividerRight;
    private View mBackground;

    public DrawableStateChanger(SettingAdapter.ItemLayoutParams params) {
        mParams = params;
        mDividerBottom = null;
        mDividerLeft = null;
        mDividerRight = null;
        mBackground = null;
    }

    public DrawableStateChanger dividerHorizontal(View bottom) {
        mDividerBottom = bottom;
        return this;
    }

    public DrawableStateChanger dividerVertical(View left, View right) {
        mDividerLeft = left;
        mDividerRight = right;
        return this;
    }

    public DrawableStateChanger background(View background) {
        mBackground = background;
        return this;
    }

    public void apply() {
        if (mParams != null) {
            // Show a divider if this item is to the last.
            if (mDividerBottom != null) {
                mDividerBottom.setVisibility(View.VISIBLE);
            }

            // Show a vertical divider
            // gridlayout require same of items width .
            // So, right items show vertical line in 0.5dp in left
            // and left items show vertical line in 0.5dp in right.
            if (mDividerLeft != null && mDividerRight != null) {
                if (mParams.left && mParams.right) {
                    mDividerRight.setVisibility(View.GONE);
                    mDividerLeft.setVisibility(View.GONE);
                } else if (mParams.left) {
                    mDividerRight.setVisibility(View.VISIBLE);
                    mDividerLeft.setVisibility(View.GONE);
                } else if (mParams.right) {
                    mDividerRight.setVisibility(View.GONE);
                    mDividerLeft.setVisibility(View.VISIBLE);
                } else {
                    mDividerRight.setVisibility(View.VISIBLE);
                    mDividerLeft.setVisibility(View.VISIBLE);
                }
            }

            // Change a background image according to the position of item.
            if (mBackground != null && mBackground.getBackground() != null) {
                mBackground.getBackground().setLevel(getDrawbleLevel(mParams));
            }
        }
    }

    private int getDrawbleLevel(SettingAdapter.ItemLayoutParams params) {
        if (mParams.top) {
            return DRAWABLE_LEVEL_TOP;
        } else if (params.bottom) {
            return DRAWABLE_LEVEL_BOTTOM;
        } else {
            return DRAWABLE_LEVEL_NORMAL;
        }
    }
}
