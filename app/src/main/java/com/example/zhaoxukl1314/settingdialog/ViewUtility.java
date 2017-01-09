package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class ViewUtility {
    public static final String TAG = "ViewUtility";

// VIEW HIT TEST
    //MotionEvent point is contained in target view or not
    public static boolean hitTest(View targetView, MotionEvent motion) {
        boolean ret = false;

        //get view location
        int[] locationOfView = new int[2]; //x,y
        targetView.getLocationOnScreen(locationOfView);

        //hit test
        Rect rect = new Rect(locationOfView[0], locationOfView[1],
                locationOfView[0] + targetView.getWidth(),
                locationOfView[1] + targetView.getHeight());

        ret = rect.contains((int) motion.getRawX(), (int)motion.getRawY());

        return ret;
    }

    // Create screen crusters.
    private static enum ScreenSize {
        WUXGA(1920, 1200),
        FULL_HD(1920, 1080),
        HD(1280, 720),
        QHD(960, 540),
        FWVGA(854, 480),
        HVGA(640, 480),
        ;

        private ScreenSize(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        private final int mWidth;
        private final int mHeight;

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        public Rect getAsRect() {
            return new Rect(0, 0, mWidth, mHeight);
        }
    }

    /**
     * Get real size of the screen device.
     * If system bar is available, application can not get the real size of
     * the screen.
     * This function serves the algorithm to estimate the screen size from Context.
     */
    public static Rect getEstimatedRealScreenRect(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        // Original display size.
        final int appWidth;
        final int appHeight;
        if (size.y < size.x) {
            appWidth = size.x;
            appHeight = size.y;
        } else {
            appWidth = size.y;
            appHeight = size.x;
        }

        // Matching.
        ScreenSize estimatedSize = null;
        int smallestDiff = appWidth + appHeight;
        for (ScreenSize screen : ScreenSize.values()) {
            int diff = Math.abs(appWidth - screen.getWidth())
                    + Math.abs(appHeight - screen.getHeight());
            if (diff < smallestDiff) {
                // This size is more better than others.
                smallestDiff = diff;
                estimatedSize = screen;
            }
        }

        if (estimatedSize == null) {
            throw new RuntimeException(
                    "getEstimatedRealScreenRect():[Not supported screen size.]");
        }

        return estimatedSize.getAsRect();
    }

    /**
     * get pixel size from dimension resource.
     *
     * @param context
     *          Current running context
     * @param id
     *          dimension resource id
     *
     * @return pixel size
     */
    public static int getPixel(Context context, int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    private static final float ASPECT_TOLERANCE = 0.001f;

    /**
     * Return false if difference between two specified aspect ratios is larger than tolerance
     * or arguments have value less than 0.
     *
     * @param width1
     * @param height1
     * @param width2
     * @param height2
     * @return true when similar aspect.
     */
    public static boolean isSimilarAspect(int width1, int height1, int width2, int height2) {
        if (width1 < 1 || height1 < 1 || width2 < 1 || height2 < 1) {
            return false;
        } else {
            float aspect1 = (float) width1 / height1;
            float aspect2 = (float) width2 / height2;

            return Math.abs(aspect1 - aspect2) <= ASPECT_TOLERANCE;
        }
    }

    /**
     * Return false if difference between two specified aspect ratios is larger than tolerance.
     *
     * @param aspect1
     * @param aspect2
     * @return true when similar aspect.
     */
    public static boolean isSimilarAspect(float aspect1, float aspect2) {
        return Math.abs(aspect1 - aspect2) <= ASPECT_TOLERANCE;
    }

    /**
     * Return false if difference between two specified aspect ratios of rectangle
     * is larger than tolerance.
     *
     * @param rect1
     * @param rect2
     * @return true when similar aspect.
     */
    public static boolean isSimilarAspectRect(Rect rect1, Rect rect2) {
        return isSimilarAspect(rect1.width(), rect1.height(), rect2.width(), rect2.height());
    }

    /**
     * Return center of two point.
     * @param p1
     * @param p2
     * @return center of p1 and p2 points.
     */
    public static Point getCenter(Point p1, Point p2){
        return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) /2);
    }
}
