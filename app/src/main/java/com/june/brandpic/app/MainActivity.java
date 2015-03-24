package com.june.brandpic.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by jhbang on 2015. 2. 10..
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        findViewById(R.id.button_certification).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(MainActivity.this, BrandSearchActivity.class);
                startActivity(intentActivity);
            }
        });
        findViewById(R.id.button_harmUpdate).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(MainActivity.this, HarmUpdateActivity.class);
                startActivity(intentActivity);
            }
        });
        findViewById(R.id.button_agency).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(MainActivity.this, AgencySearchActivity.class);
                startActivity(intentActivity);
            }
        });
    }
}
