
package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class SettingShortcut {

    public static final String TAG = "SettingShortcut";

    private final int mIconSize;

    private final ViewGroup mContainer;
    private final ListView mItems;
    private float mRotation;

    /**
     * @param items Sometimes ListView constructor in this function takes 100ms.
     * This is bad for the performance. If prevent this, create ListView object
     * in advance and set it as this parameter "items".
     */
    public SettingShortcut(Context context, ViewGroup parent, ListView items) {

        Resources res = context.getResources();
        mIconSize = res.getDimensionPixelSize(R.dimen.shortcut_icon_background_width);

        mContainer = new FrameLayout(context) {

            /**
             * A parent view may fix the layout when SettingShortcut instance is created.
             * This case, view of items must re-layout after a parent view fix the layout.
             */
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int height = MeasureSpec.getSize(heightMeasureSpec);
                updateItemHeight(height);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        parent.addView(mContainer);
        mContainer.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mContainer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        if (items != null) {
            mItems = (ListView) items;
        } else {
            // Sometimes ListView constructor takes 100ms.
            mItems = new ListView(context);
        }
        mItems.setEnabled(false);
        mItems.setClickable(false);
        mItems.setLongClickable(false);
        mItems.setFocusable(false);
        mItems.setFocusableInTouchMode(false);
        mItems.setScrollBarSize(0);
        mItems.setDividerHeight(0);
        mContainer.addView(mItems);
        mItems.getLayoutParams().width = mIconSize;
        mItems.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        ((FrameLayout.LayoutParams)mItems.getLayoutParams()).gravity = Gravity.CENTER_HORIZONTAL;
        // set initial orientation
        mItems.setOnHierarchyChangeListener(mHierarchyChangeListener);
    }

    public Adapter getAdapter() {
        return mItems.getAdapter();
    }

    /**
     * This method remains the selection if its possible.
     */
    public void setAdapter(SettingAdapter adapter) {

        updateItemHeight(mContainer.getMeasuredHeight());

        // Remain a selected status of items.
        SettingItem selectedItem = getSelected();
        mItems.setAdapter(adapter);
        if (selectedItem != null) {
            // Do not update selection if previous items contain a selected item.
            setSelected(selectedItem);
        }

        adapter.registerDataSetObserver(mAdapterObserver);
        registerOnItemSelectedListener();
    }

    /**
     * Update items without changing an adapter instance.
     * This operation doesn't reset view state. Use this method instead of {@link setAdapter()}
     * if it is needed that views remain state.
     *
     * This method remains the selection if its possible.
     */
    public void updateAdapter(SettingAdapter adapter) {

        if (getAdapter() == null) {
            setAdapter(adapter);

        } else {
            SettingItem selectedItem = getSelected();

            SettingAdapter currentAdapter = (SettingAdapter) getAdapter();
            currentAdapter.clear();
            for (int i = 0; i < adapter.getCount(); i++) {
                currentAdapter.add(adapter.getItem(i));
            }
            if (selectedItem != null) {
                // Do not update selection if previous items contain a selected item.
                setSelected(selectedItem);
            }

            updateItemHeight(mContainer.getMeasuredHeight());

            registerOnItemSelectedListener();
        }
    }

    public void show() {
        mItems.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mItems.setVisibility(View.INVISIBLE);
    }

    public boolean isShown() {
        return mItems.isShown();
    }

    public void clearSelected() {
        setSelected(null);
    }

    /**
     * Set a selected item with the specified item. Clear selection if an arguments is null.
     */
    public <T> void setSelected(T itemData) {
        setSelected(SettingItemBuilder.build(itemData).commit());
    }

    public boolean getSelectedItemIconVisibleRect(Rect rect) {
        SettingAdapter adapter = (SettingAdapter) getAdapter();
        if (adapter == null) {
            return false;
        }

        for (int i = 0; i < adapter.getCount(); i++) {
            SettingItem item = adapter.getItem(i);
            if (item.isSelected()) {
                return computeShortcutIconRect(i, adapter.getCount(), rect);
            }
        }
        return false;
    }

    /**
     * Return false if this method fails to compute rect.
     */
    private boolean computeShortcutIconRect(int iconIndex, int iconCount, Rect iconRect) {
        Rect shortcutTray = new Rect();
        if (!mItems.getGlobalVisibleRect(shortcutTray)) {
            return false;
        }
        Rect container = new Rect(0, 0, shortcutTray.width(), shortcutTray.height() / iconCount);
        container.offset(shortcutTray.left, shortcutTray.height() * iconIndex / iconCount);

        iconRect.set(
                container.centerX() - mIconSize / 2,
                container.centerY() - mIconSize / 2,
                container.centerX() + mIconSize / 2,
                container.centerY() + mIconSize / 2);

        return true;
    }

    /**
     * Return null if shortcut tray doesn't have a selected item.
     */
    private SettingItem getSelected() {
        SettingAdapter adapter = (SettingAdapter) getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                SettingItem item = adapter.getItem(i);
                if (item.isSelected()) {
                    return item;
                }
            }
        }

        return null;
    }

    private void updateItemHeight(int containerHeight) {
        if (containerHeight <= 0) {
            return;
        }
        SettingAdapter adapter = (SettingAdapter) getAdapter();
        if (adapter != null) {
            if (adapter.getCount() <= 0) {
                return;
            }
            adapter.setItemHeight(containerHeight / adapter.getCount());
        }
    }

    /**
     * Set a selected item with the specified item. Clear selection if an arguments is null.
     */
    private void setSelected(SettingItem selectedItem) {
        SettingAdapter adapter = (SettingAdapter) getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                SettingItem item = adapter.getItem(i);
                if (item.compareData(selectedItem)) {
                    item.setSelected(true);
                } else {
                    item.setSelected(false);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void registerOnItemSelectedListener() {
        SettingAdapter adapter = (SettingAdapter) getAdapter();
        if (adapter != null) {

            for (int i = 0; i < adapter.getCount(); i++) {
                adapter.getItem(i).setOnSelectedListener(mItemSelectedListener);
            }
        }
    }

    /**
     * Observe a change of items and reset the listener to notify selected item is changed.
     */
    private final DataSetObserver mAdapterObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            registerOnItemSelectedListener();
        }

        @Override
        public void onInvalidated() {
            registerOnItemSelectedListener();
        }
    };

    /**
     * Observe a change of items and reset the listener to notify selected item is changed.
     */
    private OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(SettingItem item) {
            setSelected(item);
        }
    };

    private OnHierarchyChangeListener mHierarchyChangeListener = new OnHierarchyChangeListener() {

        @Override
        public void onChildViewAdded(View parent, View child) {
            // set initial rotation.
            child.setRotation(mRotation);
        }

        @Override
        public void onChildViewRemoved(View arg0, View arg1) {
            // NOP
        }
    };
}
