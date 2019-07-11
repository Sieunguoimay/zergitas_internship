package com.sieunguoimay.vuduydu.yamp.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi

object Utils {
    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(context: Context, permissions: Array<String>): Array<String>? {
        var builder = StringBuilder()
        for (permission in permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                builder.append(permission).append(";")
            }
        }
        val s = builder.toString()
        return if (s.equals(""))  null
        else s.split(";").toTypedArray()
    }
}