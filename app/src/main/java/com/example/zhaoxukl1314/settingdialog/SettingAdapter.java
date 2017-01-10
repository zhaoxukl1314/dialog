package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * An Adapter class as a bridge between an Setting dialog view and the underlying data
 * for that view.
 *
 */
public class SettingAdapter extends ArrayAdapter<SettingItem> {

    public static final String TAG = "SettingAdapter";

    public static final int INVALID_VALUE = -1;

    private final SettingDialogItemFactory mDialogItemFactory;

    private int mItemHeight;
    private boolean mSetRoundBackgroundTop;

    public SettingAdapter(
            Context context, List<SettingItem> objects,
            SettingDialogItemFactory dialogItemFactory) {
        super(context, 0, objects);
        mDialogItemFactory = dialogItemFactory;
        mSetRoundBackgroundTop = false;
        mItemHeight = INVALID_VALUE;
    }

    public SettingAdapter(Context context, SettingDialogItemFactory dialogItemFactory) {
        this(context, new ArrayList<SettingItem>(), dialogItemFactory);
    }

    public SettingAdapter(Context context) {
        this(context, new ArrayList<SettingItem>(), new SettingDialogItemFactory());
    }

    /**
     * Specify height of item in Setting dialog. This value is applied to all item views
     * in SettingDialog.
     */
    public void setItemHeight(int height) {
        if (mItemHeight != height) {
            mItemHeight = height;
            notifyDataSetChanged();
        }
    }

    /**
     * Return 0 if this object has selected item.
     */
    public int getSelectedPosition() {
        for (int i = 0; i < getCount(); i++) {
            SettingItem item = getItem(i);
            if (item.isSelected()) {
                return i;
            }
        }
        return 0;
    }

    public static class ItemLayoutParams {
        // size of item
        public final int height;
        // position of item
        public final boolean top;
        public final boolean bottom;
        public final boolean left;
        public final boolean right;

        public ItemLayoutParams(
                int height,
                boolean top,
                boolean bottom,
                boolean left,
                boolean right) {
            this.height = height;
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SettingItem item = (SettingItem) getItem(position);

        ItemLayoutParams params = generateItemLayoutParams(parent, position);

        // update a view if it is mine.
        if (convertView != null) {
            if (convertView.getTag() instanceof SettingDialogItem) {
                SettingDialogItem itemView = (SettingDialogItem) convertView.getTag();

                if (itemView.getItem() != item) {
                    // Update SettingItem instance and redraw with new SettingItem.
                    itemView.setItem(item);
                }
                itemView.update(parent, params);
                return itemView.getView();
            }
        }

        // Create new SettingDialogItem and Views.
        SettingDialogItem itemView = mDialogItemFactory.create(item, parent);
        itemView.update(parent, params);
        itemView.getView().setTag(itemView);

        return itemView.getView();
    }

    @Override
    public int getItemViewType(int position) {
        Object obj = getItem(position);
        if (obj instanceof SettingItem) {
            return ((SettingItem) obj).getDialogItemType();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getViewTypeCount() {
        return mDialogItemFactory.getDialogItemTypeCount();
    }

    private ItemLayoutParams generateItemLayoutParams(ViewGroup parent, int itemPosition) {
        int columnCount = 1;
        if (parent instanceof GridView) {
            columnCount = ((GridView)parent).getNumColumns();
        }

        int lastRow = getCount() / columnCount - 1;
        int thisRow = itemPosition / columnCount;
        int lastColumn = columnCount - 1;
        int thisColumn = itemPosition % columnCount;

        return new ItemLayoutParams(
                mItemHeight,
                mSetRoundBackgroundTop && (thisRow == 0),
                (thisRow == lastRow),
                (thisColumn == 0),
                (thisColumn == lastColumn));
    }
}

