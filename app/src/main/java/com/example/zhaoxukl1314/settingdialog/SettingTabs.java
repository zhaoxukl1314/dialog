
package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Arrays;

public class SettingTabs extends LinearLayout {
    public static final String TAG = "SettingTabs";

    private TabView mTabLeft;
    private TabView mTabMiddle;
    private TabView mTabRight;
    private OnTabSelectedListener mListener;

    public enum Tab {
        Photo(R.drawable.setting_tab_photo),
        Video(R.drawable.setting_tab_video),
        Common(R.drawable.setting_tab_common);

        Tab(int iconId) {
            this.iconId = iconId;
        }

        private final int iconId;

        public int getIconId() {
            if (this.equals(Common)) {
                return R.drawable.setting_tab_common_vzw;
            } else {
                return iconId;
            }
        }
    }

    public interface OnTabSelectedListener {
        void onTabSelected(Tab tab);
    }

    public SettingTabs(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTabLeft = new TabView(R.id.tab_left);
        mTabMiddle = new TabView(R.id.tab_middle);
        mTabRight = new TabView(R.id.tab_right);
        mListener = null;
        setVisibility(View.GONE);
    }

    public Tab getSelected() {
        for (TabView i : Arrays.asList(mTabLeft, mTabMiddle, mTabRight)) {
            if (i.mFrame.isSelected()) {
                return i.mTab;
            }
        }
        return null;
    }

    public void setSelected(Tab tab) {
        for (TabView i : Arrays.asList(mTabLeft, mTabMiddle, mTabRight)) {
            i.mFrame.setSelected(i.mTab == tab);
        }
    }

    private void clearSelected() {
        for (TabView i : Arrays.asList(mTabLeft, mTabMiddle, mTabRight)) {
            i.mFrame.setSelected(false);
        }
    }

    public void setOnSelectedListener(OnTabSelectedListener listener) {
        mListener = listener;
    }

    /**
     * This class supports tabs less than 3.
     */
    public void setTabs(Tab... tabs) {
        switch (tabs.length) {
            case 0:
                mTabLeft.clear();
                mTabMiddle.clear();
                mTabRight.clear();
                setVisibility(View.GONE);
                clearSelected();
                break;

            case 1:
                mTabLeft.clear();
                mTabMiddle.clear();
                mTabRight.clear();
                setVisibility(View.GONE);
                clearSelected();
                break;

            case 2:
                mTabLeft.set(tabs[0]);
                mTabMiddle.clear();
                mTabRight.set(tabs[1]);
                setVisibility(View.VISIBLE);
                setSelected(tabs[0]);
                break;

            case 3:
                mTabLeft.set(tabs[0]);
                mTabMiddle.set(tabs[1]);
                mTabRight.set(tabs[2]);
                setVisibility(View.VISIBLE);
                setSelected(tabs[0]);
                break;

            default:
                throw new IllegalArgumentException("this argument is not supported.");
        }
    }

    private class TabView implements OnClickListener {
        final FrameLayout mFrame;
        final ImageView mIcon;

        Tab mTab;

        TabView(int layoutId) {
            mFrame = (FrameLayout) findViewById(layoutId);
            mIcon = (ImageView) mFrame.findViewById(R.id.icon);
            mFrame.setOnClickListener(this);
            clear();
        }

        public void set(Tab tab) {
            mTab = tab;
            mFrame.setVisibility(View.VISIBLE);
            mIcon.setImageResource(mTab.getIconId());
        }

        public void clear() {
            mTab = null;
            mFrame.setVisibility(View.GONE);
            mIcon.setImageDrawable(null);
        }

        @Override
        public void onClick(View v) {
            if (mTab == null) {
                return;
            }
            if (mFrame.isSelected()) {
                return;
            }
            setSelected(mTab);
            if (mListener != null) {
                mListener.onTabSelected(mTab);
            } else {
            }
        }
    }
}
