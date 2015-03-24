package com.june.brandpic.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class AgencySearchActivity extends Activity {


    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencysearch);
        ImageButton backButon = (ImageButton)findViewById(R.id.backButton);
        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(AgencySearchActivity.this, MainActivity.class);
                intentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentActivity);
            }
        });
        super.findViewById(R.id.bottomButton1).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(AgencySearchActivity.this, HarmUpdateActivity.class);
                startActivity(intentActivity);
            }
        });
        super.findViewById(R.id.bottomButton2).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(AgencySearchActivity.this, BrandSearchActivity.class);
                startActivity(intentActivity);
            }
        });

        searchText = (EditText)findViewById(R.id.agencysearch_edittext);
        findViewById(R.id.agencysearch_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentActivity = new Intent(AgencySearchActivity.this, AgencyResultActivity.class);
//                startActivity(intentActivity);

                if(searchText.length()<2) {
                    Toast.makeText(getApplicationContext(), R.string.word_toast, Toast.LENGTH_SHORT).show();
                }else{
                    DialogFragment dialog = new CustomDialog().newInstance(R.layout.popup_listview,searchText.getText().toString(),"agency");
                    dialog.show(getFragmentManager(), "test");

                }
            }
        });


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


}
