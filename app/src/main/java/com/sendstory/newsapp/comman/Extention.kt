package com.sendstory.newsapp.comman

import android.content.Context
import android.util.Log
import org.apache.commons.lang3.time.DurationFormatUtils


fun Context.getAge(value: Long): String {
    val currentTime = System.currentTimeMillis()
    val age = currentTime - (value * 1000)

    try {
        var ageString = DurationFormatUtils.formatDuration(age, "d") + "d"
        if ("0d" == ageString) {
            ageString = DurationFormatUtils.formatDuration(age, "H") + "h"
            if ("0h" == ageString) {
                ageString = DurationFormatUtils.formatDuration(age, "m") + "m"
                if ("0m" == ageString) {
                    ageString = DurationFormatUtils.formatDuration(age, "s") + "s"
                    if ("0s" == ageString) {
                        ageString = age.toString() + "ms"
                    }
                }
            }
        }
        return ageString
    }catch (e: Exception){
        Log.e("TAG", "Time Convert Error ${e.message}", )
        return ""
    }


}