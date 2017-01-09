
package com.example.zhaoxukl1314.settingdialog;

import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;


/**
 * This is utility class to coordinate SettingDialog.
 */
class LayoutCoordinateUtil {

    /**
     * This method sets left, top, right and bottom of target view.
     * The position are set value based on 'rotationSourceArea'.
     *
     * <pre>
     * Landscape, Portrait before rotation
     * -------------------------------------
     * |    (a)                            |
     * |     *-----------                  |
     * |     |(b)       |                  |
     * |     | *####### |                  |
     * |     | ######## |                  |
     * |     |          |                  |
     * |     |          |                  |
     * |     |          |                  |
     * |     ------------                  |
     * -------------------------------------
     * Portrait after rotation
     * -------------------------------------
     * |                                   |
     * |    (c)                            |
     * |     *----------------             |
     * |     |   ####        |             |
     * |     |   ####        |             |
     * |     |   ####        |             |
     * |     |   ####        |             |
     * |     -----------------             |
     * |                                   |
     * -------------------------------------
     * ### target view
     * (a) rotationSourceArea
     * (b) targetRect
     * (c) rotationDestPosition
     * </pre>
     *
     * @param orientation
     *       Specify Configuration.ORIENTATION_PORTRAIT or Configuration.ORIENTATION_LANDSCAPE.
     *
     * @param target
     *
     * @param targetRect
     *       Specify area of target view based on container view of target.
     *
     * @param rotationSourceArea
     *       Specify area based on container view of target.
     *
     * @param rotationDestPosition
     *       Specify position after view is rotated.
     * @return
     *       Area of target view based on container view of target.
     */
    public static Rect coodinatePosition(
            View target,
            Rect targetRect,
            Rect rotationSourceArea,
            Point rotationDestPosition) {

        // position based on 'rotationSourceArea'
        int xOnRotationSourceArea = targetRect.left - rotationSourceArea.left;
        int yOnRotationSourceArea = targetRect.top - rotationSourceArea.top;

        // Move target view based on 'rotationSourceArea'
        target.setLeft(xOnRotationSourceArea);
        target.setRight(xOnRotationSourceArea + targetRect.width());
        target.setTop(yOnRotationSourceArea);
        target.setBottom(yOnRotationSourceArea + targetRect.height());

        // Rotate target view based on 'rotationSourceArea'
        target.setPivotX(-target.getLeft());
        target.setPivotY(-target.getTop());

//        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // The dialog is rotated by -90 around the (0,0) and the left edge is aligned
//            // the top edge of screen. Therefore y must be moved +rotationSourceArea.width().
//            int offsetX = rotationDestPosition.x;
//            int offsetY = rotationDestPosition.y + rotationSourceArea.width();
//
//            // Should not set offset by setTranslation here. disappeared abnormally problem.
//            target.setTranslationX(0);
//            target.setTranslationY(0);
//            target.offsetLeftAndRight(offsetX);
//            target.offsetTopAndBottom(offsetY);
//
//            // Set absolute position of this dialog
//            Rect rect = new Rect(0, 0, targetRect.height(), targetRect.width());
//            rect.offset(offsetX, offsetY - rotationSourceArea.width());
//            return rect;
//
//        } else {
            int offsetX = rotationDestPosition.x;
            int offsetY = rotationDestPosition.y;
            target.setTranslationX(offsetX);
            target.setTranslationY(offsetY);

            // Set absolute position of this dialog
            Rect rect = new Rect(0, 0, targetRect.width(), targetRect.height());
            rect.offset(target.getLeft() + offsetX, target.getTop() + offsetY);
            return rect;
//        }
    }
}
