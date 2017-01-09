

package com.example.zhaoxukl1314.settingdialog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class LayoutDependencyResolver {
    public static final String TAG = "LayoutDependencyResolver";

    /**
     * Aspect ratio of View finder is defined 16:9 (width / height).
     * View finder at this corresponds to view, not preview user sees.
     * The area has preview and all UI components.
     */
    private static float VIEWFINDER_ASPECT_RATIO = 16f / 9f;

    // System bar status.
    public enum SystemBarStatus {
        ALWAYS_CANCELED, // Not control Navigation bar.
        REGION_ASSIGNED, // KeyLess products on vanilla Android framework.
        REGION_OVERLAID, // KeyLess products on extended Android framework.
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.is_tablet);
    }

    public static boolean isTenInch(Context context) {
        return context.getResources().getBoolean(R.bool.is_ten_inch);
    }

    public static SystemBarStatus getCurrentSystemBarStatus(Context context) {
        if (isTablet(context)) {
            return SystemBarStatus.ALWAYS_CANCELED;
        } else {
            if (isAvailableSystemUiVisibility(context)) {
                return SystemBarStatus.REGION_OVERLAID;
            } else {
                // Extended SystemUi Visibility API is not supported.
                return SystemBarStatus.REGION_ASSIGNED;
            }
        }
    }

    public static int getSystemBarMargin(Context context) {
        switch (getCurrentSystemBarStatus(context)) {
            case ALWAYS_CANCELED:
                return 0;

            case REGION_ASSIGNED:
                // fall-through.
            case REGION_OVERLAID:
                return context.getResources().getDimensionPixelSize(R.dimen.navigationbar_width);

            default:
                // UnExpectedState.
                throw new IllegalStateException(
                        "setupDummySystemBar():[Unexpected system bar status.]");
        }
    }

    /**
     * Boundary that this function returns contains all UI components for camera application.
     * Surface of camera preview should be arranged inside this area.
     */
    public static Rect getViewFinderSize(Context context) {
        // This area has a status bar and a navigation bar area. These overlaies on View finder.
        Point appDisplaySize = new Point(
                context.getResources().getDisplayMetrics().widthPixels,
                context.getResources().getDisplayMetrics().heightPixels);

        // This area doesn't have a status bar and a navigation bar area.
        Point realDisplaySize = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealSize(realDisplaySize);

        switch (getCurrentSystemBarStatus(context)) {
            case ALWAYS_CANCELED:
                if (isTablet(context)) {
                    // For tablet, camera has a navigation bar at long side of display.
                    // But lock-screen may have at short side. Therefore, camera doesn't get
                    // a width of application area correctly.
                    // The following code computes size from real display size.
                    // Aspect ratio of display of tablet is smaller than expected view finder,
                    // so this works correctly for tablet.
                    return cropWithAspectRatio(realDisplaySize, VIEWFINDER_ASPECT_RATIO);
                } else {
                    return cropWithAspectRatio(appDisplaySize, VIEWFINDER_ASPECT_RATIO);
                }

            case REGION_ASSIGNED:
                // fall-through.
            case REGION_OVERLAID:
                // This screen size has system bar area.
                return cropWithAspectRatio(realDisplaySize, VIEWFINDER_ASPECT_RATIO);

            default:
                throw new IllegalStateException(
                        "setupDummySystemBar():[Unexpected system bar status.]");
        }
    }

    private static Rect cropWithAspectRatio(Point size, float aspectWidthPerHeight) {
        float longSideLength = Math.max(size.x, size.y);
        float shortSideLength = Math.min(size.x, size.y);
        if (longSideLength / shortSideLength < aspectWidthPerHeight) {
            return new Rect(0, 0,
                    (int) Math.ceil(longSideLength),
                    (int) Math.ceil(longSideLength / aspectWidthPerHeight));
        } else {
            return new Rect(0, 0,
                    (int) Math.ceil(shortSideLength * aspectWidthPerHeight),
                    (int) Math.ceil(shortSideLength));
        }
    }

    public static int getLeftItemCount(Context context) {
        return context.getResources().getInteger(R.integer.shortcut_icon_count);
    }

    /**
     * Specify the aspectRatio (width / height). Compute a rect in ViewFinder area.
     * ViewFinder area means the root view of application.
     */
    public static Rect getSurfaceRect(Context context, float previewAspectRatio) {
        Rect viewFinderSize = getViewFinderSize(context);
        float viewFinderAspectRatio = (float) viewFinderSize.width()
                / (float) viewFinderSize.height();

        if (previewAspectRatio > viewFinderAspectRatio) {
            // Preview is wider than ViewFinder, so the width is same as ViewFinder
            // and the height is less than ViewFinder.
            return new Rect(
                    0,
                    0,
                    viewFinderSize.width(),
                    (int) (viewFinderSize.width() / previewAspectRatio));
        } else {
            // Preview is taller than ViewFinder, so the height is same as ViewFinder
            // and the width is less than ViewFinder.
            return new Rect(
                    0,
                    0,
                    (int) (viewFinderSize.height() * previewAspectRatio),
                    viewFinderSize.height());
        }
    }

    // Resolve layout.
    // displayRect must be based on Landscape coordinates.
    public static void resolveLayoutDependencyOnDevice(Activity act, View root) {
        // Arrange ViewFinder. This view fills the screen for application.
        Rect viewfinderRect = getViewFinderSize(act);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) root.getLayoutParams();
        params.width = viewfinderRect.width();
        params.height = viewfinderRect.height();
        params.gravity = Gravity.BOTTOM;

        setupRightContainer(act);
        setupCaptureMethodIndicatorContainer(act);
        setupSystemBarMargin(act);
        setupRotatableToast(act);
    }

    // Remove the system navigation bar.
    public static void requestToRemoveSystemUi(View view) {
        if (view == null) {
            // NOP. View is not available.
            return;
        }

        switch (getCurrentSystemBarStatus(view.getContext())) {
            case ALWAYS_CANCELED:
                // NOP. Device has hardware key.
                break;

            case REGION_ASSIGNED:
                // Same behavior as dim.
                requestToDimSystemUi(view);
                return;

            case REGION_OVERLAID:

                break;
        }

        // Refresh.
        view.requestLayout();
    }

    // Dim the system navigation bar.
    public static void requestToDimSystemUi(View view) {
        if (view == null) {
            // NOP. View is not available.
            return;
        }

        switch (getCurrentSystemBarStatus(view.getContext())) {
            case ALWAYS_CANCELED:
                // NOP. Device has hardware key.
                break;

            case REGION_ASSIGNED:
                // Dim.
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                break;

            case REGION_OVERLAID:
                break;
        }

        // Refresh.
        view.requestLayout();
    }

    // Recover the system navigation bar.
    public static void requestToRecoverSystemUi(View view) {
        if (view == null) {
            // NOP. View is not available.
            return;
        }

        switch (getCurrentSystemBarStatus(view.getContext())) {
            case ALWAYS_CANCELED:
                // NOP. Device has hardware key.
                break;

            case REGION_ASSIGNED:
                // NOP.
                break;

            case REGION_OVERLAID:
                // Solid background.
                break;
        }

        // Refresh.
        view.requestLayout();
    }

    private static void setupRightContainer(Activity act) {
        // TODO: NOT SUPPORT HVGA DIAPLAY.
        // TODO: NOT SUPPORT DEVICE WHICH HAS HW KEY.

        Rect viewfinderRect = getViewFinderSize(act);
        int shortcutIconHeight = act.getResources().getDimensionPixelSize(
                R.dimen.shortcut_dialog_item_height);
        int shortcutIconHeightPadding = act.getResources().getDimensionPixelSize(
                R.dimen.shortcut_dialog_padding);

        int topMargin = (viewfinderRect.height() / getLeftItemCount(act) - shortcutIconHeight
                + shortcutIconHeightPadding) / 2;
        int bottomMargin = topMargin;
        act.findViewById(R.id.right_container).setPadding(0, topMargin, 0, bottomMargin);

        setupModeIndicatorContainer(act);
    }

    public static void setupRotatableToast(Activity act) {
        DisplayMetrics metrix = act.getResources().getDisplayMetrics();
        int displayWidth = Math.max(metrix.widthPixels, metrix.heightPixels);
        int displayHeight = Math.min(metrix.widthPixels, metrix.heightPixels);

        int leftMargin = act.getResources().getDimensionPixelSize(
                R.dimen.left_container_width);
        int rightMargin = act.getResources().getDimensionPixelSize(
                R.dimen.right_container_width) + getSystemBarMargin(act);

        Rect finderRect = getViewFinderSize(act);
        finderRect.offset(0, displayHeight - finderRect.height());
        int toastHeight = finderRect.height() / getLeftItemCount(act);

        // Compute container of toast for landscape
        Rect landscapeTop = new Rect(
                finderRect.left + leftMargin,
                finderRect.top,
                finderRect.right - rightMargin,
                finderRect.top + toastHeight);
        Rect landscapeBottom = new Rect(
                finderRect.left + leftMargin,
                finderRect.bottom - toastHeight,
                finderRect.right - rightMargin,
                finderRect.bottom);

        // Compute container of toast for portrait
        Rect portraitTop = new Rect(
                finderRect.left + leftMargin,
                finderRect.top,
                finderRect.left + leftMargin + toastHeight,
                finderRect.bottom);
        Rect portraitBottom = new Rect(
                finderRect.right - rightMargin - toastHeight,
                finderRect.top,
                finderRect.right - rightMargin,
                finderRect.bottom);

    }

    private static void setupSystemBarMargin(Activity act) {
        View iconContainer = act.findViewById(R.id.icons);
        View uiComponentsContainer = act.findViewById(R.id.lazy_inflated_ui_component_container);

        int navigationBarWidth = getSystemBarMargin(act);
        ((ViewGroup.MarginLayoutParams) iconContainer.getLayoutParams())
                .setMargins(0, 0, navigationBarWidth, 0);
        iconContainer.requestLayout();

        ((ViewGroup.MarginLayoutParams) uiComponentsContainer.getLayoutParams())
                .setMargins(0, 0, navigationBarWidth, 0);
        uiComponentsContainer.requestLayout();
    }

    private static void setupCaptureMethodIndicatorContainer(Activity act) {
        act.findViewById(R.id.capture_method_indicator_container).getLayoutParams().height =
                getViewFinderSize(act).height() / getLeftItemCount(act);
    }

    private static void setupModeIndicatorContainer(Activity act) {
        int modeIconSize = act.getResources().getDimensionPixelSize(
                R.dimen.capturing_mode_selector_button_item_width);
        int shortcutIconHeight = act.getResources().getDimensionPixelSize(
                R.dimen.shortcut_dialog_item_height);
        int containerWidth = act.getResources().getDimensionPixelSize(
                R.dimen.right_container_width);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) act.findViewById(
                R.id.mode_indicator_container).getLayoutParams();
        params.height = modeIconSize;
        params.rightMargin = containerWidth - (containerWidth - modeIconSize) / 2
                + getSystemBarMargin(act);
        params.bottomMargin = (getViewFinderSize(act).height() / getLeftItemCount(act)
                - shortcutIconHeight) / 2;
    }

    private static boolean isAvailableSystemUiVisibility(Context context) {
        if (context.checkCallingOrSelfPermission(SYSTEM_UI_VISIBILITY_EXTENSIONS)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private static final String SYSTEM_UI_VISIBILITY_EXTENSIONS =
            "com.sonymobile.permission.SYSTEM_UI_VISIBILITY_EXTENSIONS";
    // TODO: android.Manifest.permission.SYSTEM_UI_VISIBILITY_EXTENSIONS permission
}
