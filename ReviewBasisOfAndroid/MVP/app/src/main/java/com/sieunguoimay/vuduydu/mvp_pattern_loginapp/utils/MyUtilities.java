package com.sieunguoimay.vuduydu.mvp_pattern_loginapp.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.content.res.ResourcesCompat;

import com.sieunguoimay.vuduydu.mvp_pattern_loginapp.R;

public class MyUtilities {

    public static void vibrate(Activity activity, int miliseconds){
        Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(miliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(miliseconds);
        }
    }
}
