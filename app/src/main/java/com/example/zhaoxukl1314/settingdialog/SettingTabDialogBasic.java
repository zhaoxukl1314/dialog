

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class SettingTabDialogBasic extends RelativeLayout implements SettingDialogInterface {

    public static final String TAG = "SettingTabDialogBasic";

    private GridView mGridView;
    private SettingAdapter mAdapter;

    private LayoutCoordinator mLayoutCoordinator;
    private ViewGroup mParentView;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mLayoutCoordinator != null) {
            mLayoutCoordinator.coordinatePosition();
        }
    }

    @Override
    public void show() {
        requestLayout();
        if (mLayoutCoordinator != null) {
            mLayoutCoordinator.coordinateSize();
        }
    }

    @Override
    public void setLayoutCoordinator(LayoutCoordinator coordinator) {
        mLayoutCoordinator = coordinator;
    }

    @Override
    public void open(ViewGroup parentView) {
        if (parentView == null) {
            throw new IllegalArgumentException("Parent view shouldn't be null");
        }

        mParentView = parentView;
        mParentView.addView(this);
    }

    @Override
    public void close() {
        cancelAnimation();

        Handler handler = getHandler();
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (mParentView != null) {
                        mParentView.removeView(SettingTabDialogBasic.this);
                    }
                }
            });
        }

    }

    protected void cancelAnimation() {
        if (mParentView == null) {
            return;
        }

        if (mParentView.getAnimation() != null) {
            mParentView.setAnimation(null);
        }
    }

    @Override
    public boolean hitTest(int x, int y) {
        Rect rect = new Rect();
        if (getGlobalVisibleRect(rect)) {
            return rect.contains(x, y);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnabled()) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return true;
        }
    }

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

    /**
     * Set number of columns
     *
     * @param numColumns Number of columns
     */
    public void setNumColumns(int numColumns) {
        mGridView.setNumColumns(numColumns);
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
}