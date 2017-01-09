

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.GridView;

public class SettingTabDialogBasic extends SettingDialog {

    public static final String TAG = "SettingTabDialogBasic";

    private SettingTabs mTabs;
    private GridView mGridView;
    private SettingAdapter mAdapter;
    private int mNumberOfTabs = 0;

    public SettingTabDialogBasic(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getPixel(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mGridView = (GridView)findViewById(R.id.setting_gridview);
        int fadingEdgeLength = getPixel(R.dimen.setting_dialog_scroll_fading_edge_length);
        mGridView.setFadingEdgeLength(fadingEdgeLength);
        mGridView.setVerticalFadingEdgeEnabled(true);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        mTabs = (SettingTabs) findViewById(R.id.tabs);
    }

    public void setTabs(SettingTabs.Tab ... tabs) {
        mTabs.setTabs(tabs);
    }

    public void setSelectedTab(SettingTabs.Tab tab) {
        mTabs.setSelected(tab);
    }

    public SettingTabs.Tab getSelectedTab() {
        return mTabs.getSelected();
    }

    public void setOnSelectedTabListener(SettingTabs.OnTabSelectedListener listener) {
        mTabs.setOnSelectedListener(listener);
    }

    @Override
    public void setAdapter(SettingAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mTabBodyAdapterObserver);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mTabBodyAdapterObserver);
        registerOnItemSelectedListener();
        mGridView.setAdapter(mAdapter);
        if (adapter.getCount() > 0) {
            // scroll to top if all items is not selected.
            mGridView.setSelection(adapter.getSelectedPosition());
        }
        invalidate();
    }

    // Observe a change of items and reset the listener to notify selected item is changed.
    private final DataSetObserver mTabBodyAdapterObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            registerOnItemSelectedListener();
        }

        @Override
        public void onInvalidated() {
            registerOnItemSelectedListener();
        }
    };

    private void registerOnItemSelectedListener() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            mAdapter.getItem(i).setOnSelectedListener(mItemSelectedListener);
        }
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialog
     */
    @Override
    public void setSensorOrientation(int orientation) {
        requestLayout();
        super.setSensorOrientation(orientation);

        // When orientation is changed, a number of columns of GridView is changed.
        // At that time, views of GridView are re-attached and a last re-attached view is focused.
        // So the view speaks.
        mGridView.performAccessibilityAction(
                AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS,
                null);
    }

    /**
     * Set number of columns
     *
     * @param numColumns Number of columns
     */
    public void setNumColumns(int numColumns) {
        mGridView.setNumColumns(numColumns);
    }

    @Override
    public boolean getSelectedItemRect(Rect rect) {

        // find a selected item.
        for (int i = 0; i < mGridView.getChildCount(); i++) {
            View v = mGridView.getChildAt(i);
            if (v.getTag() instanceof SettingDialogItem) {
                SettingDialogItem item = (SettingDialogItem)v.getTag();
                if (item.getItem().isSelected()) {
                    rect.set(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    rect.offset(mGridView.getLeft(), mGridView.getTop());
                    return true;
                }
            }
        }

        return false;
    }

    private OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(SettingItem item) {
            for (int i = 0; i < mAdapter.getCount(); i++) {
                SettingItem testItem = mAdapter.getItem(i);
                if (testItem != item) {
                    testItem.setSelected(false);
                }
            }
        }
    };

    public SettingAdapter getAdapter() {
        return mAdapter;
    }

    public int numberOfTabs() {
        return mNumberOfTabs;
    }

    public void setNumberOfTabs(int numberOfTabs) {
        mNumberOfTabs = numberOfTabs;
    }
}