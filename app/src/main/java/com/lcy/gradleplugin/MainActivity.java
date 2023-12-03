package com.lcy.gradleplugin;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.e("lcyy","1111");
//        MethodRecordItem methodRecordItem = new MethodRecordItem(1, new ArrayList<>());
//        Log.e("lcyy","22222");
//        MethodRecorder.INSTANCE.onMethodEnter("com.lcy.gradleplugin.MainActivity", "onCreate", new ArrayList<>());
        Log.e("lcyy", "01010101010");
        test(1, 2);
//        MethodRecorder.INSTANCE.onMethodEnter("com.lcy.gradleplugin.MainActivity", "onCreate", new ArrayList<>());
    }

    public int test(int a, int b) {
        int c = a + b;
        SystemClock.sleep(1000);
        return c;
    }
}