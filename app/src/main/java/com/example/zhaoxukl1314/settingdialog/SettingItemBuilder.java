package com.example.zhaoxukl1314.settingdialog;

import java.util.ArrayList;
import java.util.List;

public class SettingItemBuilder<T> {
    public static final String TAG = "SettingItemBuilder";
    private static final int INVALID_RESOURCE_ID = -1;

    private final T mData;

    private int mIconId = INVALID_RESOURCE_ID;
    private int mTextId = INVALID_RESOURCE_ID;
    private String mText = "";
    private int mDialogItemType = INVALID_RESOURCE_ID;
    private SettingExecutorInterface<T> mExecutor = null;
    private List<SettingItem> mItems = null;

    private boolean mIsSelectable = true;
    private boolean mSelected = false;

    private SettingItemBuilder(T data) {
        mData = data;
    }

    public static <T> SettingItemBuilder<T> build(T data) {
        return new SettingItemBuilder<T>(data);
    }

    public SettingItem commit() {
        final SettingItem item;

        if (mTextId == INVALID_RESOURCE_ID) {
            item = new TypedSettingItem<T>(
                    mData,
                    mIconId,
                    mText,
                    mDialogItemType,
                    mExecutor);

        } else {
            item = new TypedSettingItem<T>(
                    mData,
                    mIconId,
                    mTextId,
                    mDialogItemType,
                    mExecutor);
        }

        if (mItems != null) {
            for (SettingItem child : mItems) {
                item.getChildren().add(child);
            }
        }

        item.setSelectable(mIsSelectable);
        item.setSelected(mSelected);

        return item;
    }

    public SettingItemBuilder<T> iconId(int id) {
        mIconId = id;
        return this;
    }

    public SettingItemBuilder<T> textId(int id) {
        mTextId = id;
        return this;
    }

    public SettingItemBuilder<T> text(String text) {
        mText = text;
        return this;
    }

    public SettingItemBuilder<T> dialogItemType(int type) {
        mDialogItemType = type;
        return this;
    }

    public SettingItemBuilder<T> executor(SettingExecutorInterface<T> executor) {
        mExecutor = executor;
        return this;
    }

    public SettingItemBuilder<T> item(SettingItem item) {
        if (mItems == null) {
            mItems = new ArrayList<SettingItem>();
        }
        mItems.add(item);
        return this;
    }

    public SettingItemBuilder<T> selected(boolean value) {
        mSelected = value;
        return this;
    }

    public SettingItemBuilder<T> selectable(boolean value) {
        mIsSelectable = value;
        return this;
    }
}

