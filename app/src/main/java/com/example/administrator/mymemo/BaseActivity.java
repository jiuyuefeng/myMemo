package com.example.administrator.mymemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class BaseActivity extends Activity {
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
    }

    public void startActivity(Class<?> paramClass) {
        startActivity(new Intent(this, paramClass));
    }
}
