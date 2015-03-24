package com.june.brandpic.app;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nhn.android.maps.NMapActivity;

/**
 * Created by jhbang on 2015. 2. 14..
 */
public abstract class CommonNMapActivity extends NMapActivity {
    protected LinearLayout fullLayout;
    protected FrameLayout subActivityContent;

    @Override
    public void setContentView(int layoutResID) {
        fullLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_common, null);  // The base layout
        subActivityContent = (FrameLayout) fullLayout.findViewById(R.id.main_frame);            // The frame layout where the activity content is placed.
        getLayoutInflater().inflate(layoutResID, subActivityContent, true);            // Places the activity layout inside the activity content frame.
        super.setContentView(fullLayout);                                                       // Sets the content view as the merged layouts.
        ImageButton backButon = (ImageButton)fullLayout.findViewById(R.id.backButton);
        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setMenuTilte(String title){
        TextView menuTitle = (TextView)fullLayout.findViewById(R.id.menutitle_textview);
        menuTitle.setText(title);
    }
}
