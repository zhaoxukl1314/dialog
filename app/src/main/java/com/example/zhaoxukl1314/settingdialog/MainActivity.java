package com.example.zhaoxukl1314.settingdialog;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private SettingDialogStack mSettingDialogStack;
    private TimeShiftSettingDialogListener mSettingDialogListener = new TimeShiftSettingDialogListener();
    private SettingDialogItemFactory mDialogItemFactory;
    private SettingChangeExecutor mSettingChangeExecutor;

    private static final TimeShiftViewerMenuItem[] COMMON_ITEMS = {
            TimeShiftViewerMenuItem.SAVE_SEPERATELY,
            TimeShiftViewerMenuItem.SHARE,
            TimeShiftViewerMenuItem.SELECT_PHOTOS,
    };

    private static final SettingTabs.Tab[] TABS = {
            SettingTabs.Tab.Common
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDialogItemFactory = new SettingDialogItemFactory();
        mSettingDialogStack = new SettingDialogStack(
                this,
                mSettingDialogListener,
                (FrameLayout) this.findViewById(R.id.viewer_right_container),
                (RelativeLayout) this.findViewById(R.id.viewer_interaction_view_container));

    }

    public void openMenu(View v) {
        mSettingDialogStack.openMenuDialog(
                generateItemAdapter(),
                TABS,
                null,
                null,
                3);
    }

    public SettingAdapter generateItemAdapter() {
        SettingAdapter adapter = new SettingAdapter(this, mDialogItemFactory);
        mSettingChangeExecutor =
                new SettingChangeExecutor(this, mSettingDialogStack);
        ArrayList<TimeShiftViewerMenuItem> visibleItems =
                new ArrayList<TimeShiftViewerMenuItem>();
        for (TimeShiftViewerMenuItem key : COMMON_ITEMS) {
            visibleItems.add(key);
        }
        for (TimeShiftViewerMenuItem key : visibleItems) {
            SettingItemBuilder<TimeShiftViewerMenuItem> builder = SettingItemBuilder.build(key)
                    .iconId(key.getIconId())
                    .textId(key.getTitleId())
                    .executor(mSettingChangeExecutor.getExecutor(key))
                    .dialogItemType(SettingDialogItemFactory.VALUE_BUTTON);

            adapter.add(builder.commit());
        }
        adapter.setItemHeight(SettingDialogBasicParams
                .SECOND_LAYER_DIALOG_SINGLE_ITEM_PARAMS
                .getItemHeight(this));
        return adapter;
    }

    private static class TimeShiftSettingDialogListener implements SettingDialogListener {
        @Override
        public void onOpenSettingDialog(
                SettingDialogStack sender,
                boolean isAlreadyOpened,
                boolean isAnimation) {
            //NOP.
        }

        @Override
        public void onCloseSettingDialog(SettingDialogStack sender, boolean isAllClosed) {
            //NOP.
        }
    }
}
