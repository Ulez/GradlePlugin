package com.lcy.gradleplugin;

import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.util.HashMap;

public class Recorder {
    private static final String TAG = "MethodRecorder";
    private static HashMap<String, Long> upTimeMap = new HashMap<>();
    private static long sMainThreadId = Looper.getMainLooper().getThread().getId();

    public static void i(String method) {
        if (Thread.currentThread().getId() == sMainThreadId) {
            upTimeMap.put(method, SystemClock.uptimeMillis());
        }
    }

    public static void o(String method) {
        if (Thread.currentThread().getId() == sMainThreadId) {
            long cost = SystemClock.uptimeMillis() - upTimeMap.getOrDefault(method, 0L);
            Log.e(TAG, method + ",cost = " + cost);
        }
    }
}
