
package com.example.zhaoxukl1314.settingdialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.List;


public class CommonUtility {
    public static final String TAG = "CommonUtility";

    /**
     * Returns a new List, which is subtracts excludeList from srcList.
     * The excludeList is list which shouldn't be in share list.
     * We may move this method to another (new?) class in the future.
     */
    public static List<ResolveInfo> removeExcludeItemsFromList(
            List<ResolveInfo> srcList, List<String> excludingItems) {

        // Copy src list.
        List<ResolveInfo> dst = new ArrayList<ResolveInfo>(srcList);

        // Remove applications which should not be added to share list.
        for (ResolveInfo resolveInfo: srcList) {
            for (String packagename: excludingItems) {
                if (packagename.equals(resolveInfo.activityInfo.packageName)) {
                    dst.remove(resolveInfo);
                    break;
                }
            }
        }

        return dst;
    }

    /**
     * Return whether the package is exist in MUT.
     * */
    public static boolean isPackageExist(String packageName, Context context) {
        boolean result = false;

        if (context == null) {
            return result;
        }

        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getApplicationInfo(packageName, 0);
            result = true;
        } catch (NameNotFoundException e) {
            result = false;
        }

        return result;
    }

    /**
     * Return whether language setting is arabic (right to left).
     * */
    public static boolean isMirroringRequired(Context context) {
        boolean result = false;

        if (context == null) {
            return result;
        }

        String checkString = context.getResources().getString(
                R.string.capturing_mode_selector_bidicheck_string);
        Bidi temp = new Bidi(checkString, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
        result = temp.isRightToLeft();

        return result;
    }

    /**
     * Return whether Activity is Available.
     * This method is for restriction profile feature, which is added in JB MR2.
     * If application call startActivity() and target app is disabled by Restricted Profile,
     * caller will clash. We need to avoid it.
     * If startup of the Activity is disabled by profile, this method return false.
     * */
    public static boolean isActivityAvailable(Context context, Intent intent) {
        boolean result = false;

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /** Application type. **/
    public enum ApplicationType {
        SYSTEM,
        UPDATED_SYSTEM_APP,
        OTHER
    }

    public static boolean isPreinstalledApp(Context context) {
        if (getApplicationType(context).equals(ApplicationType.SYSTEM)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSystemApp(Context context) {
        if (getApplicationType(context).equals(ApplicationType.OTHER)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check if context has the permission.
     *
     * @return If context has the permission, return true.
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        if (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(
                permission,
                context.getPackageName())) {
            return true;
        } else {
            return false;
        }
    }

    private static ApplicationType getApplicationType(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                final PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null && pi.applicationInfo != null) {
                    final int flags = pi.applicationInfo.flags;
                    if ((flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                        // Pre-installed and download-updated application.
                        return ApplicationType.UPDATED_SYSTEM_APP;
                    } else if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        // Pre-installed application.
                        return ApplicationType.SYSTEM;
                    } else {
                        // Post-installed application.
                        return ApplicationType.OTHER;
                    }
                } else {
                    return ApplicationType.OTHER;
                }
            } else {
                return ApplicationType.OTHER;
            }
        } catch (NameNotFoundException e) {
            return ApplicationType.OTHER;
        }
    }

    /**
     * Return whether the touch event is contained in the view.
     */
    public static boolean isEventContainedInView(View targetView, MotionEvent motion) {
        // Get view location.
        int[] locationOfView = new int[2]; // x,y.
        targetView.getLocationOnScreen(locationOfView);

        // Hit test.
        Rect rect = new Rect(locationOfView[0], locationOfView[1],
                locationOfView[0] + targetView.getWidth(),
                locationOfView[1] + targetView.getHeight());

        return rect.contains((int) motion.getRawX(), (int)motion.getRawY());
    }

    /**
     * Remove extension from file name.
     * @param filename filename.
     * @return file name without extension.
     */
    public static String removeFileExtension(final String filename) {
        int lastDotPos = filename.lastIndexOf('.');
        if (lastDotPos == -1) {
            return filename;
        } else if (lastDotPos == 0) {
            return filename;
        } else {
            return filename.substring(0, lastDotPos);
        }
    }

    /**
     * Dump file for debug.
     */
    public static void dumpFile(byte[] data, String filename) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("/sdcard/" + filename);
            fos.write(data);
        } catch (IOException e1) {
        }

        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e2) {
            }
        }
    }

    /**
     * Return two string are same or not.
     *
     * @param arg1: String1
     * @param arg2: String2
     * @return true: arg1 equals arg2 or both null. false: arg1 not equal arg2.
     */
    public static boolean sameStrings(String arg1, String arg2) {
        if (arg1 == null) {
            if (arg2 == null) {
                return true;
            } else {
                return false;
            }
        } else {
            return arg1.equals(arg2);
        }
    }
}
