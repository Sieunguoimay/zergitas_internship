package com.zergitas.demo.utils.helper

inline fun Long.formatTime(): String {
    val mms = this / 1000.0
    val hr: Int = (mms / 3600).toInt()
    val rem: Int = (mms % 3600).toInt()
    val mn = rem / 60
    val sec = rem % 60
    return String.format("%02d:%02d:%02d", hr, mn, sec)
}

inline fun Long.formatSize(): String {
    val size = this / (1024 * 1000f)
    return String.format("%.1f Mb", size).replace(",", ".")
}