package com.example.zhaoxukl1314.settingdialog;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class TypedSettingItem<T> implements SettingItem {
    public static final String TAG = "TypedSettingItem";

    private final T mData;

    //Only one of mTxtId or mTxtStr is set in constructor.
    private final int mTextId;
    private final String mText;

    private final int mSubTextId;
    private final int mLongTextId;


    private final int mIconId;
    private final int mDialogItemType;
    private final String mAdditionalTextForAccessibility;

    private final SettingExecutorInterface<T> mExecutor;
    private final List<SettingItem> mChildren;


    private boolean mIsSelected;
    private boolean mIsSelectable;
    private final boolean mIsSoundEnabled;

    private OnItemSelectedListener mOnSettingItemSelectedListener;


    public TypedSettingItem(
            T data,
            int iconId,
            int labelId,
            int subTextId,
            int longTextId,
            String additionalTextForAccessibility,
            int dialogItemType,
            SettingExecutorInterface<T> executor,
            boolean isSoundEnable) {
        mData = data;
        mTextId = labelId;
        mText = "";
        mIconId = iconId;
        mDialogItemType = dialogItemType;
        mExecutor = executor;
        mIsSoundEnabled = isSoundEnable;

        mChildren = new ArrayList<SettingItem>();

        mSubTextId = subTextId;
        mLongTextId = longTextId;
        mAdditionalTextForAccessibility = additionalTextForAccessibility;
        mIsSelected = false;
        mIsSelectable = false;

        mOnSettingItemSelectedListener = null;
    }

    public TypedSettingItem(
            T data,
            int iconId,
            String text,
            int subTextId,
            int longTextId,
            String additionalTextForAccessibility,
            int dialogItemType,
            SettingExecutorInterface<T> executor,
            boolean isSoundEnable) {
        mData = data;
        mTextId = ResourceUtil.INVALID_RESOURCE_ID;
        mText = text;
        mSubTextId = subTextId;
        mLongTextId = longTextId;
        mIconId = iconId;
        mDialogItemType = dialogItemType;
        mAdditionalTextForAccessibility = additionalTextForAccessibility;
        mExecutor = executor;
        mIsSoundEnabled = isSoundEnable;

        mChildren = new ArrayList<SettingItem>();

        mIsSelected = false;
        mIsSelectable = false;

        mOnSettingItemSelectedListener = null;
    }

    @Override
    public String getText(Resources resources) {
        if (mTextId == ResourceUtil.INVALID_RESOURCE_ID) {
            return mText;
        } else {
            return resources.getString(mTextId);
        }
    }

    /**
     * Return null if the sub text is not set.
     */
    @Override
    public String getSubText(Resources resources) {
        if (mSubTextId == ResourceUtil.INVALID_RESOURCE_ID) {
            return null;
        } else {
            return resources.getString(mSubTextId);
        }
    }

    /**
     * Return null if the long text is not set.
     */
    @Override
    public String getLongText(Resources resources) {
        if (mLongTextId == ResourceUtil.INVALID_RESOURCE_ID) {
            return null;
        } else {
            return resources.getString(mLongTextId);
        }
    }

    @Override
    public String getContentDescription(Resources resources) {

        StringBuilder description = new StringBuilder();

        if (mTextId != ResourceUtil.INVALID_RESOURCE_ID) {
            description.append(resources.getString(mTextId));
        } else {
            description.append(mText);
        }

        if ((mAdditionalTextForAccessibility != null) &&
                (!mAdditionalTextForAccessibility.isEmpty())) {
            description.append(' ');
            description.append(mAdditionalTextForAccessibility);
        }

        return description.toString();
    }

    @Override
    public int getIconId() {
        return mIconId;
    }

    @Override
    public boolean isSelected() {
        return mIsSelected;
    }

    @Override
    public boolean isSelectable() {
        return mIsSelectable;
    }

    @Override
    public void setSelected(boolean value) {
        mIsSelected = value;
    }

    @Override
    public void setSelectable(boolean value) {
        mIsSelectable = value;
    }

    @Override
    public int getDialogItemType() {
        return mDialogItemType;
    }

    @Override
    public List<SettingItem> getChildren() {
        return mChildren;
    }

    public T getData() {
        return mData;
    }

    /**
     * Select the item
     *
     * SettingExecutorInterface.onExecute() is called.
     */
    @Override
    public void select() {
        mIsSelected = true;

        if (mOnSettingItemSelectedListener != null) {
            mOnSettingItemSelectedListener.onItemSelected(this);
        }

        if (mExecutor == null) {
            return;
        }

        mExecutor.onExecute(this);
    }

    @Override
    public void setOnSelectedListener(OnItemSelectedListener listener) {
        mOnSettingItemSelectedListener = listener;
    }

    @Override
    public boolean compareData(SettingItem item) {
        if (item instanceof TypedSettingItem) {
            TypedSettingItem<?> other = (TypedSettingItem<?>)item;
            return (mData == other.mData);
        }
        return false;
    }

    @Override
    public boolean compareData(Object data) {
        return (mData == data);
    }

    public String getValueText() {
        return mAdditionalTextForAccessibility;
    }

    @Override
    public boolean isSoundEnabled() {
        return mIsSoundEnabled;
    }
}
