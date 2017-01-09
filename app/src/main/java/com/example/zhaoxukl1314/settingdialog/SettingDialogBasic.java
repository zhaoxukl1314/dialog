

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class SettingDialogBasic extends SettingDialog {

    public static final String TAG = "SettingDialogBasic";

    protected GridView mGridView;

    private View mSelectedView;

    private SettingDialogBasicParams mParams;

    private final int TITLE_HEIGHT = getPixel(R.dimen.title_text_height)
          + getPixel(R.dimen.divider_height);

    private final int ITEM_DIVIDER_HEIGHT = getPixel(R.dimen.divider_height);

    public SettingDialogBasic(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int getPixel(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mGridView = (GridView)findViewById(R.id.setting_gridview);
        mGridView.setFocusable(mGridView.isFocusable());
        mGridView.setFocusableInTouchMode(mGridView.isFocusableInTouchMode());

        int fadingEdgeLength = getPixel(R.dimen.setting_dialog_scroll_fading_edge_length);
        mGridView.setFadingEdgeLength(fadingEdgeLength);
        mGridView.setVerticalFadingEdgeEnabled(true);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        mGridView.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedView = view;
                SettingAdapter adapter = (SettingAdapter) mGridView.getAdapter();
                adapter.getItem(position).select();
            }
        });
    }

    public void setSettingDialogParams(SettingDialogBasicParams params) {
        Context ctx = getContext();
        mParams = params;
        findViewById(R.id.background).setBackgroundResource(
                mParams.getBackgroundId());
        findViewById(R.id.container).setPadding(
                mParams.getPadding(ctx),
                mParams.getPadding(ctx),
                mParams.getPadding(ctx),
                mParams.getPadding(ctx));
        if (mGridView.getAdapter() instanceof SettingAdapter) {
            ((SettingAdapter) mGridView.getAdapter()).setItemHeight(
                    mParams.getItemHeight(ctx));
        }
    }

    @Override
    public void setAdapter(SettingAdapter adapter) {
        if (mParams != null) {
            adapter.setItemHeight(mParams.getItemHeight(getContext()));
        }
        adapter.setRoundTopItemBackground(!isVisibleTitle());
        mGridView.setAdapter(adapter);
        if (adapter.getCount() > 0) {
            // scroll to top if all items is not selected.
            mGridView.setSelection(adapter.getSelectedPosition());
        }
    }

    /**
     * Get setting adapter.
     */
    public SettingAdapter getAdapter() {
        return (SettingAdapter)mGridView.getAdapter();
    }

    /**
     * Set a title of dialog
     *
     * @param textId Resource id of the text
     */
    public void setTitle(int textId) {
        TextView titleTextView = (TextView)findViewById(R.id.setting_title);
        titleTextView.setText(textId);

        // Set content description.
        titleTextView.setContentDescription(titleTextView.getText());

        View title = findViewById(R.id.setting_title_layout);
        title.setVisibility(View.VISIBLE);

        SettingAdapter adapter = (SettingAdapter) mGridView.getAdapter();
        if (adapter != null) {
            adapter.setRoundTopItemBackground(false);
        }
    }

    private boolean isVisibleTitle() {
        return (findViewById(R.id.setting_title_layout).getVisibility() == View.VISIBLE);
    }

    private int getItemNum() {
        return mGridView.getAdapter().getCount();
    }

    private int getTitleHeight() {
        View titleView = findViewById(R.id.setting_title_layout);
        if (titleView.getVisibility() == View.VISIBLE) {
            return TITLE_HEIGHT;
        } else {
            return 0;
        }
    }

    /**
     * Get height of dialog according to the number of column
     *
     * @param numColumn Number of column
     * @return Height of dialog
     */
    public int computeHeight(int numColumn) {
        int selectorPadding = getSelectorPadding();
        Context ctx = getContext();

        if (numColumn == 2) {
            return (int)(
                    getItemNum() / 2f * mParams.getItemHeight(getContext())
                    + (getItemNum() / 2f) * ITEM_DIVIDER_HEIGHT
                    + getTitleHeight()
                    + mParams.getPadding(ctx) * 2
                    + selectorPadding);
        } else {
            return getItemNum() * mParams.getItemHeight(ctx)
                    + getItemNum() * ITEM_DIVIDER_HEIGHT
                    + getTitleHeight()
                    + mParams.getPadding(ctx) * 2
                    + selectorPadding;
        }
    }

    public int computeMaxHeight(int numRows) {
        int selectorPadding = getSelectorPadding();

        return (
                numRows * mParams.getItemHeight(getContext())
                + mParams.getPadding(getContext()) * 2
                + getTitleHeight()
                + numRows * ITEM_DIVIDER_HEIGHT
                + selectorPadding);
    }

    public int computeWidth(int numColumn) {
        //int selectorPadding = getSelectorPadding();

        // Vertical line is 1dp,
        // but SETTING_ITEM_COLUMN_MARGIN is 0.5dp.
        // This is for gridlayout.
        if (numColumn == 2) {
            return getResources().getDimensionPixelSize(
                    R.dimen.setting_dialog_shortcut_2_column_width);
        } else {
            return getResources().getDimensionPixelSize(
                    R.dimen.setting_dialog_shortcut_width);
        }
    }

    /**
     * Get number of rows for items in shortcut dialog.
     * The screen height is derived as below.
     * [screen height] = 2 * [dialog edge padding]
     *                   + [title text height]
     *                   + [number of rows] * [setting item height]
     *                   + ([number of rows] - 1) * [divider height]
     *                   + [minimum dialog vertical margin]
     *
     * The number of rows is derived from This formula.
     *
     * @param screenHeight
     *          screen height size.
     * @return
     *      number of rows.
     */
    public int getNumRows(int screenHeight) {
        int contentBodyHeight = screenHeight
                - mParams.getPadding(getContext()) * 2
                - getTitleHeight();
        return (contentBodyHeight + ITEM_DIVIDER_HEIGHT)
                / (mParams.getItemHeight(getContext()) + ITEM_DIVIDER_HEIGHT);
    }

    private int getSelectorPadding() {
        Rect padding = new Rect();
        if (mGridView.getSelector() != null) {
            mGridView.getSelector().getPadding(padding);
            return padding.bottom + padding.top;
        }

        return 0;
    }

    /* (non-Javadoc)
     * @see com.sonyericsson.cameracommon.setting.dialog.SettingDialog
     */
    @Override
    public void show() {

        requestLayout();
        super.show();
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

        if (mSelectedView != null) {
            if (mSelectedView.getGlobalVisibleRect(rect)) {
                convertRectInLandscape(rect);
                return true;
            }
        }
        return false;
    }

    private void convertRectInLandscape(Rect rect) {
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            int x = (int)(rect.top + getX());
            int y = (int)(rect.left - getX());
            int width = rect.height();
            int height = rect.width();
            rect.set(x, y, x + width, y + height);
        }
    }
}
