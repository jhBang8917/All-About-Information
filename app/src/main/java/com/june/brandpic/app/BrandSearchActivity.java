package com.june.brandpic.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;


public class BrandSearchActivity extends Activity {


    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandsearch);
        ImageButton backButon = (ImageButton)findViewById(R.id.backButton);
        backButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(BrandSearchActivity.this, MainActivity.class);
                intentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentActivity);
            }
        });
        super.findViewById(R.id.bottomButton1).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(BrandSearchActivity.this, HarmUpdateActivity.class);
                startActivity(intentActivity);
            }
        });
        super.findViewById(R.id.bottomButton2).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(BrandSearchActivity.this, AgencySearchActivity.class);
                startActivity(intentActivity);
            }
        });


        searchText = (EditText)findViewById(R.id.searchText);
//        searchText = (AutoCompleteTextView)findViewById(R.id.searchText);
//        searchText.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
        findViewById(R.id.main_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intentActivity = new Intent(BrandSearchActivity.this, BrandResultActivity.class);
//                intentActivity.putExtra("searchText",searchText.getText().toString());
//                startActivity(intentActivity);
                if(searchText.length()<2){
                    Toast.makeText(getApplicationContext(), R.string.word_toast, Toast.LENGTH_SHORT).show();
//                    AlertDialog.Builder alert = new AlertDialog.Builder(BrandSearchActivity.this);
//                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();     //닫기
//                        }
//                    });
//                    alert.setMessage("한 글자이상 입력해주세요");
//                    alert.show();
                }else{

                    DialogFragment dialog = new CustomDialog().newInstance(R.layout.popup_listview,searchText.getText().toString(),"brand");

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
