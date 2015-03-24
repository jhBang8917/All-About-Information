package com.june.brandpic.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by jhbang on 2015. 2. 3..
 */
public class BrandResultActivity extends CommonActivity {

    private CustomAdapter<ArrayList> certification_Adapter;
    private ExpandableListView certification_ListView;
    private String companyName;
    private ArrayList<String> certification_FieldList;
    private ArrayList<String> certification_FieldNameList;
    private String certification_API = "";
    private EditText totalSearch_text;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private String totalElements;
    private TextView companyTotal;


    public String getCompanyName() {
        return companyName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandresult);
        setMenuTilte("인증 정보 검색");


        Intent intent = getIntent();
        companyName = intent.getStringExtra("companyName");
        Log.i("회사이름", companyName);
        if(companyName.contains("삼성")||companyName.toLowerCase().contains("samsung"))
            ((ImageView)findViewById(R.id.logo_imageview)).setImageResource(R.drawable.samsung_logo);
        TextView textView_companyName = (TextView) findViewById(R.id.companyname_textview);
        companyTotal = (TextView)findViewById(R.id.companytotal_textview);
        textView_companyName.setText(companyName);

        super.findViewById(R.id.bottomButton1).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(BrandResultActivity.this, HarmUpdateActivity.class);
                startActivity(intentActivity);
            }
        });
        super.findViewById(R.id.bottomButton2).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(BrandResultActivity.this, AgencySearchActivity.class);
                startActivity(intentActivity);
            }
        });

        certification_ListView = (ExpandableListView) findViewById(R.id.certification_expand);
        certification_ListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                v.findViewById(R.id.indicatior_imageview).setSelected(true);
                v.findViewById(R.id.indicatior_imageview).setBackgroundResource(R.drawable.common_indicator_minus);
                return false;
            }
        });

        totalSearch_text = (EditText) findViewById(R.id.totalsearch_editText);
        findViewById(R.id.totalsearch_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NameValuePair> queryList = new ArrayList<NameValuePair>();
                queryList.add(new BasicNameValuePair("model_query_pageable", "{enable:true,pageSize:10000}"));
                queryList.add(new BasicNameValuePair("model_query", "{$and:[" +
                        "{\"sb_makingCompany\":\"" + getCompanyName() + "\"}," +
                        "{$or:[{\"sp_model_str\":{\"$regex\":\"" + totalSearch_text.getText() + "\"}}," +
                        "{\"sp_goods\":{\"$regex\":\"" + totalSearch_text.getText() + "\"}}]}" +
                        "]}"));

                new JsonLoadingTask(queryList).execute();
            }
        });

        spinner = (Spinner) findViewById(R.id.division_spinner);
        spinner.setPrompt("인증구분");
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.division_spinner, R.layout.item_spinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<NameValuePair> queryList = new ArrayList<NameValuePair>();
                queryList.add(new BasicNameValuePair("model_query_pageable", "{enable:true,pageSize:10000}"));

                if (spinner.getSelectedItem().toString().equals("모두보기")) {
                    queryList.add(new BasicNameValuePair("model_query", "{$and:[{\"sb_makingCompany\":\"" + getCompanyName() + "\"}]}"));
                } else {

                    queryList.add(new BasicNameValuePair("model_query", "{$and:[" +
                            "{\"sb_makingCompany\":\"" + getCompanyName() + "\"}," +
                            "{\"sc_status\":\"" + spinner.getSelectedItem().toString() + "\"}" +
                            "]}"));
                }

                new JsonLoadingTask(queryList).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        certification_Adapter = new CustomAdapter<ArrayList>();

        certification_FieldList = new ArrayList<String>();
        certification_FieldList.add("sc_status");
        certification_FieldList.add("sp_model_str");
        certification_FieldList.add("sp_goods");
        certification_FieldList.add("sc_confirmNum");
        certification_FieldList.add("totalcodeName");
        certification_FieldList.add("sc_confirmDay");
        certification_FieldList.add("sp_producing");
        certification_FieldList.add("sb_makingCompany");
        certification_FieldList.add("sb_makingCountry");

        certification_FieldNameList = new ArrayList<String>();
        certification_FieldNameList.add("인증구분");
        certification_FieldNameList.add("제품명");
        certification_FieldNameList.add("품목");
        certification_FieldNameList.add("인증번호");
        certification_FieldNameList.add("인증기관");
        certification_FieldNameList.add("인증일자");
        certification_FieldNameList.add("수입/제조");
        certification_FieldNameList.add("제조사");
        certification_FieldNameList.add("제조국");

//        ArrayList<NameValuePair> queryList = new ArrayList<NameValuePair>();
//        queryList.add(new BasicNameValuePair("model_query_pageable", "{enable:true,pageSize:10000}"));
//        queryList.add(new BasicNameValuePair("model_query", "{$and:[{\"sb_makingCompany\":{\"$regex\":\"" + getCompanyName() + "\"}}]}"));
//
//        new JsonLoadingTask(queryList).execute();

    }




    public class JsonLoadingTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<NameValuePair> query;
        private ProgressDialog mProgress;

        public JsonLoadingTask(ArrayList<NameValuePair> list) {
            this.query = list;
        }


        @Override
        protected Void doInBackground(Void... Params) {
            HandleJson handleJson = new HandleJson();
            certification_API = handleJson.getStringFromUrl("http://www.ibtk.kr/api_confirm_detail/a334b557dcc627e0d61990cc14ea8c52?" + URLEncodedUtils.format(query, "utf-8"));
            certification_Adapter = handleJson.getJson("certification",certification_API, certification_FieldList, certification_FieldNameList);
            totalElements = handleJson.getTotalElements();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(BrandResultActivity.this);
            mProgress.setMessage("로딩중입니다.");
            mProgress.setCancelable(false);
            mProgress.show();

            //ProgressDialog dialog = ProgressDialog.show(mContext,"", "로딩중입니다 잠시만 기달려주세요.", true);

        }

        @Override
        protected void onPostExecute(Void result) {
            companyTotal.setText(totalElements);
            certification_ListView.setAdapter(certification_Adapter);

            if(certification_Adapter.getGroupCount()==0){
                Toast.makeText(getApplicationContext(),"정보가 없습니다.",Toast.LENGTH_SHORT).show();
            }
            mProgress.dismiss();
        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.


    } // JsonLoadingTask
}
