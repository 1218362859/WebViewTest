package com.example.a.webviewtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by A on 2018/4/26.
 */

public class SplashActivity extends Activity {
    private static final long SPLASH_DELAY_MILLIS = 3000;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goHome();
            }
        }, SPLASH_DELAY_MILLIS);
    }

    private void goHome()
    {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
}
