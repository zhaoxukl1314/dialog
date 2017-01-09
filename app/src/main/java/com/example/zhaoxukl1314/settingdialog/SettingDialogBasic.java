

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class SettingDialogBasic extends SettingDialog {

    public static final String TAG = "SettingDialogBasic";

    protected GridView mGridView;

    private View mSelectedView;

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

    @Override
    public void setAdapter(SettingAdapter adapter) {
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


    @Override
    public void show() {

        requestLayout();
        super.show();
    }

    @Override
    public boolean getSelectedItemRect(Rect rect) {

        if (mSelectedView != null) {
            if (mSelectedView.getGlobalVisibleRect(rect)) {
                return true;
            }
        }
        return false;
    }
}
