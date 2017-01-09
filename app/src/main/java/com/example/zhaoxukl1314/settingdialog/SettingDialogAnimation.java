

package com.example.zhaoxukl1314.settingdialog;

import android.R.anim;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

/**
 * This class provides the function to create animation for setting dialog.
 */
public class SettingDialogAnimation {
    public static final String TAG = "SettingDialogAnimation";

    private Context mContext;
    private final float mTranslateDistance;

    public SettingDialogAnimation(Context context) {
        mContext = context;
        mTranslateDistance = ViewUtility.getPixel(mContext,
                R.dimen.setting_dialog_column_height) / 3f;
    }

    private TranslateAnimation getTranslateForDecelerate(
            float fromX,float toX, float fromY, float toY) {
        TranslateAnimation translate = new TranslateAnimation(fromX, toX, fromY, toY);
        translate.setInterpolator(mContext, anim.decelerate_interpolator);

        return translate;
    }

    private TranslateAnimation getTranslateForAccelerate(
            float fromX,float toX, float fromY, float toY) {
        TranslateAnimation translate = new TranslateAnimation(fromX, toX, fromY, toY);
        translate.setInterpolator(mContext, anim.accelerate_interpolator);

        return translate;
    }

    /**
     * Set animation of opening dialog.
     *
     * @param view
     * @param orientation
     * @return animation
     */
    public Animation setOpenDialogAnimation(View view, int orientation) {

        AnimationSet animation = new AnimationSet(false);
        Animation alpha = AnimationUtils.loadAnimation(
                mContext, R.anim.setting_dialog_fade_in);

        animation.addAnimation(alpha);
        animation.setDuration(animation.getDuration());

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            animation.addAnimation(getTranslateForDecelerate(mTranslateDistance, 0, 0, 0));
        } else {
            animation.addAnimation(getTranslateForDecelerate(0, 0, mTranslateDistance, 0));
        }
        view.setAnimation(animation);

        return animation;
    }

    /**
     * Set animation of closing dialog.
     *
     * @param view
     * @param orientation
     * @return animation
     */
    public Animation setCloseDialogAnimation(View view, int orientation) {

        AnimationSet animation = new AnimationSet(false);
        Animation alpha = AnimationUtils.loadAnimation(
                mContext, R.anim.setting_dialog_fade_out);

        animation.addAnimation(alpha);
        animation.setDuration(animation.getDuration());

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            animation.addAnimation(getTranslateForAccelerate(0, mTranslateDistance, 0, 0));
        } else {
            animation.addAnimation(getTranslateForAccelerate(0, 0, 0, mTranslateDistance));
        }
        view.setAnimation(animation);

        return animation;
    }
}
