package com.jasonzhong.nasapictureviewer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.app.ActivityCompat;
import android.view.WindowManager;

/**
 * Created by junzhong on 2017-07-28.
 */

public class Util {

    public static int getScreenHeight(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    public static Bitmap scaleBitmap(Bitmap bm, int desiredHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float ratio = 0;

        if (width != height) {
            if (height > desiredHeight) {
                ratio = (float) desiredHeight / height;
            } else {
                ratio = (float) height / desiredHeight;
            }
            height = desiredHeight;
            double temp = Math.floor(ratio * width * 100.0) / 100.0;
            width = (int) temp;
        } else {
            // square
            height = desiredHeight;
            width = height;
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public static void openAlertDialog(final Activity context, String tile, String message) {
        try {
            new AlertDialog.Builder(context).setTitle(tile)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.finishAffinity(context);
                        }
                    }).show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }
}
