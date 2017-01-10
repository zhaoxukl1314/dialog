

package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SettingIconList extends SettingDialogItem {

    public static final String TAG = "SettingIconList";

    private final ViewHolder mHolder;

    private static class ViewHolder {
        ViewGroup mContainer;
        LinearLayout mList;
    }

    public SettingIconList(Context context, SettingItem item) {
        super(item);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        mHolder = new ViewHolder();
        mHolder.mContainer = (ViewGroup) inflater.inflate(R.layout.setting_item_icon_list, null);
        mHolder.mList = (LinearLayout) mHolder.mContainer.findViewById(R.id.list);
    }

    @Override
    public void update(ViewGroup parent, SettingAdapter.ItemLayoutParams params) {

        int itemWidth = mHolder.mList.getLayoutParams().height
                / Math.max(1, getItem().getChildren().size());

        mHolder.mList.removeAllViews();
        for (SettingItem item : getItem().getChildren()) {
            ImageView view = createIcon(item);
            mHolder.mList.addView(view);

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams)view.getLayoutParams();
            layoutParams.width = itemWidth;
            layoutParams.height = 0;
            layoutParams.weight = 1;
        }
    }

    @Override
    public View getView() {
        return mHolder.mContainer;
    }

    private void updateSelected(SettingItem selectedItem) {
        // clear all item selection
        for (SettingItem item : getItem().getChildren()) {
            if (item != selectedItem) {
                item.setSelected(false);
            }
        }
        // select new item
        selectedItem.select();

        // update view selection
        for (int i = 0; i < mHolder.mList.getChildCount(); i++) {
            View v = mHolder.mList.getChildAt(i);
            if (v.getTag() == selectedItem) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
    }

    private ImageView createIcon(final SettingItem item) {
        Context context = mHolder.mContainer.getContext();
        ImageView image = new ImageView(context);
        image.setTag(item);
        image.setSelected(item.isSelected());
        image.setImageResource(item.getIconId());
        image.setBackgroundResource(R.drawable.setting_item_icon_selector);
        image.setClickable(true);

        image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               if (getView().isShown()) {
                    updateSelected(item);
                }
            }
        });

        return image;
    }
}