package com.comp3717.vu_gilpin.services;

import android.content.Context;

import com.comp3717.vu_gilpin.R;

public class ConditionService {

    public static String getCondition(Context context, float systolicReading, float diastolicReading) {
        if (diastolicReading < 80) {
            if (systolicReading < 120) {
                return context.getString(R.string.condition_normal);
            }

            if (systolicReading < 130) {
                return context.getString(R.string.condition_elevated);
            }
        }

        if (systolicReading > 180 || diastolicReading > 120) {
            return context.getString(R.string.condition_crisis);
        }

        if (systolicReading >= 140 || diastolicReading >= 90) {
            return context.getString(R.string.condition_high_2);
        }

        return context.getString(R.string.condition_high_1);
    }
}
