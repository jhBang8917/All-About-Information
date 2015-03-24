package com.june.brandpic.app;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.TabHost.TabSpec;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jhbang on 2015. 2. 10..
 */
public class HarmUpdateActivity extends CommonActivity implements OnClickListener {

    private TabHost tabHost;
    private ExpandableListView harm_ListView;
    private ExpandableListView recall_ListView;
    private CustomAdapter<ArrayList> harm_Adapter;
    private CustomAdapter<ArrayList> recall_Adapter;
    private ArrayList<String> harm_FieldList;
    private ArrayList<String> recall_FieldList;
    private String harm_API;
    private String recall_API;
    private String harm_date;
    private String recall_date;
    private ArrayList<String> harm_FieldNameList;
    private ArrayList<String> recall_FieldNameList;
    private ToggleButton day_toggle;
    private ToggleButton week_toggle;
    private ToggleButton month_toggle;
    private ToggleButton sixmonth_toggle;
    private ToggleButton year_toggle;
    private ArrayList<ToggleButton> toogleList;
    private ArrayList<NameValuePair> harmQuery;
    private ArrayList<NameValuePair> recallQuery;
    private TextView totalSearch_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harmupdate);
        setMenuTilte("최신 위해&리콜 정보");
        super.findViewById(R.id.bottomButton1).setBackgroundDrawable(super.getApplicationContext().getResources().getDrawable(R.drawable.common_button_menu_brand));
        super.findViewById(R.id.bottomButton1).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(HarmUpdateActivity.this, BrandSearchActivity.class);
                startActivity(intentActivity);
            }
        });
        super.findViewById(R.id.bottomButton2).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(HarmUpdateActivity.this, AgencySearchActivity.class);
                startActivity(intentActivity);
            }
        });

        ((ImageView)findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(HarmUpdateActivity.this, MainActivity.class);
                intentActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentActivity);
            }
        });

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        harm_ListView = (ExpandableListView) findViewById(R.id.harm_ListView);
        recall_ListView = (ExpandableListView) findViewById(R.id.recall_ListView);



        sixmonth_toggle = (ToggleButton)findViewById(R.id.sixmonth_togglebutton);
        day_toggle = (ToggleButton)findViewById(R.id.day_togglebutton);
        week_toggle = (ToggleButton)findViewById(R.id.week_togglebutton);
        month_toggle = (ToggleButton)findViewById(R.id.month_togglebutton);
        year_toggle = (ToggleButton)findViewById(R.id.year_togglebutton);

        toogleList =new ArrayList<ToggleButton>();
        toogleList.add(sixmonth_toggle);
        toogleList.add(day_toggle);
        toogleList.add(week_toggle);
        toogleList.add(month_toggle);
        toogleList.add(year_toggle);

        sixmonth_toggle.setOnClickListener(this);
        day_toggle.setOnClickListener(this);
        week_toggle.setOnClickListener(this);
        month_toggle.setOnClickListener(this);
        year_toggle.setOnClickListener(this);

        totalSearch_text = (EditText) findViewById(R.id.harmupdate_search_editText);


//        setBeforeMonth(-1);
//        Log.i("1개월전", String.valueOf(harm_date));
//        setBeforeMonth(-12);
//        Log.i("1년전", String.valueOf(harm_date));
//        setBeforeDay(-1);
//        Log.i("1일전", String.valueOf(harm_date));
//        setBeforeDay(-7);
//        Log.i("일주일전", String.valueOf(harm_date));


//        tabHost.addTab(tabHost.newTabSpec("tab1").setContent(R.id.harm_ListView).setIndicator("",getResources().getDrawable(R.drawable.harmupdate_harm_tab)));
        tabHost.addTab(setIndicator(this, tabHost.newTabSpec("tab1"), R.drawable.harmupdate_harm_tab, "").setContent(R.id.harm_ListView));
        tabHost.addTab(setIndicator(this, tabHost.newTabSpec("tab2"),R.drawable.harmupdate_recall_tab,"").setContent(R.id.recall_ListView));
//        tabHost.addTab(tabHost.newTabSpec("tab2").setContent(R.id.recall_ListView).setIndicator("", getResources().getDrawable(R.drawable.harmupdate_recall_tab)));

        harm_Adapter = new CustomAdapter<ArrayList>();
        recall_Adapter = new CustomAdapter<ArrayList>();

        harm_FieldList = new ArrayList<String>();
        harm_FieldList.add("productModel");
        harm_FieldList.add("productModel");
        harm_FieldList.add("companyName");
        harm_FieldList.add("productName");
        harm_FieldList.add("troubleSangwangName");
        harm_FieldList.add("sagoWoninETC");//사고이
        harm_FieldList.add("informerTypeName");//사고원
        harm_FieldList.add("troubleDate");//사고원
        harm_FieldList.add("signDate");//사고원

        harm_FieldNameList = new ArrayList<String>();
        harm_FieldNameList.add("모델명");
        harm_FieldNameList.add("모델명");
        harm_FieldNameList.add("회사명");
        harm_FieldNameList.add("품목");
        harm_FieldNameList.add("사고 이름");
        harm_FieldNameList.add("사고 내용");
        harm_FieldNameList.add("정보 종류");
        harm_FieldNameList.add("사고 발생일");
        harm_FieldNameList.add("결제일");

        recall_FieldList = new ArrayList<String>();
        recall_FieldList.add("model");
        recall_FieldList.add("model");
        recall_FieldList.add("companyName");
        recall_FieldList.add("productContents");
        recall_FieldList.add("recallType");
        recall_FieldList.add("actions");
        recall_FieldList.add("harmContents");
        recall_FieldList.add("linkURL");
        recall_FieldList.add("publicDate");

        recall_FieldNameList = new ArrayList<String>();
        recall_FieldNameList.add("모델명");
        recall_FieldNameList.add("모델명");
        recall_FieldNameList.add("회사명");
        recall_FieldNameList.add("품목");
        recall_FieldNameList.add("리콜 종류");
        recall_FieldNameList.add("조치 사항");
        recall_FieldNameList.add("리콜 내용");
        recall_FieldNameList.add("이미지");
        recall_FieldNameList.add("공표일자");

        sixmonth_toggle.setChecked(true);
        setBeforeMonth(-6);

        harmQuery = new ArrayList<NameValuePair>();
        harmQuery.add(new BasicNameValuePair("model_query_pageable", "{enable:true,pageSize:500}"));
        harmQuery.add(new BasicNameValuePair("model_query", "{\"signDate\":{\"$gte\":\"" + harm_date + "\"}}"));
        recallQuery = new ArrayList<NameValuePair>();
        recallQuery.add(new BasicNameValuePair("model_query_pageable", "{enable:true,pageSize:500}"));
        recallQuery.add(new BasicNameValuePair("model_query", "{\"publicDate\":{\"$gte\":\"" + recall_date + "\"}}"));

        findViewById(R.id.harmupdate_search_button).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                harmQuery.add(1,new BasicNameValuePair("model_query",
                        "{$or:[{\"productModel\":{\"$regex\":\""+totalSearch_text.getText()+"\"}}," +
                        "{\"companyName\":{\"$regex\":\""+totalSearch_text.getText()+"\"}},"+
                        "{\"productName\":{\"$regex\":\""+totalSearch_text.getText()+"\"}},"+
                        "{\"informerTypeName\":{\"$regex\":\""+totalSearch_text.getText()+"\"}}]}"+
                        "]}"));
                recallQuery.add(1,new BasicNameValuePair("model_query",
                        "{$or:[{\"model\":{\"$regex\":\""+totalSearch_text.getText()+"\"}}," +
                        "{\"companyName\":{\"$regex\":\""+totalSearch_text.getText()+"\"}},"+
                        "{\"productContents\":{\"$regex\":\""+totalSearch_text.getText()+"\"}},"+
                        "{\"recallType\":{\"$regex\":\""+totalSearch_text.getText()+"\"}}]}"+
                        "]}"));
                new JsonLoadingTask().execute();
            }
        });

        new JsonLoadingTask().execute();



    }

    private void setBeforeMonth(int month) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.MONTH, month);
        harm_date = String.valueOf(today.get(today.MONTH)+1)+"/"+String.valueOf(today.get(today.DATE))+"/"+String.valueOf(today.get(today.YEAR)).substring(2,4);
        recall_date = String.valueOf(today.get(today.YEAR)).substring(2,4)+". "+String.valueOf(today.get(today.MONTH)+1)+". "+String.valueOf(today.get(today.DATE));
    }
    private void setBeforeDay(int day) {
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, day);
        harm_date = String.valueOf(today.get(today.MONTH)+1)+"/"+String.valueOf(today.get(today.DATE))+"/"+String.valueOf(today.get(today.YEAR)).substring(2,4);
        recall_date = String.valueOf(today.get(today.YEAR)).substring(2,4)+". "+String.valueOf(today.get(today.MONTH)+1)+". "+String.valueOf(today.get(today.DATE));
    }

    public TabSpec setIndicator(Context ctx,TabSpec spec,int resId,String name) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_tab, null);
        ImageView imgTab = (ImageView) v.findViewById(R.id.tab_imageview);
        imgTab.setImageDrawable(getResources().getDrawable(resId));

        return spec.setIndicator(v);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        for (int i = 0; i < toogleList.size(); i++) {
            toogleList.get(i).setChecked(false);
        }
        switch (id){
            case R.id.sixmonth_togglebutton:
                sixmonth_toggle.setChecked(true);
                setBeforeMonth(-6);
                harmQuery.add(1, new BasicNameValuePair("model_query", "{\"signDate\":{\"$gte\":\"" + harm_date + "\"}}"));
                recallQuery.add(1, new BasicNameValuePair("model_query", "{\"publicDate\":{\"$gte\":\"" + recall_date + "\"}}"));
                new JsonLoadingTask().execute();
                break;
            case R.id.day_togglebutton:
                day_toggle.setChecked(true);
                setBeforeDay(-1);
                harmQuery.add(1,new BasicNameValuePair("model_query", "{\"signDate\":{\"$gte\":\"" + harm_date + "\"}}"));
                recallQuery.add(1,new BasicNameValuePair("model_query", "{\"publicDate\":{\"$gte\":\"" + recall_date + "\"}}"));
                new JsonLoadingTask().execute();
                break;
            case R.id.week_togglebutton:

                week_toggle.setChecked(true);
                setBeforeDay(-7);
                harmQuery.add(1, new BasicNameValuePair("model_query", "{\"signDate\":{\"$gte\":\"" + harm_date + "\"}}"));
                recallQuery.add(1, new BasicNameValuePair("model_query", "{\"publicDate\":{\"$gte\":\"" + recall_date + "\"}}"));
                new JsonLoadingTask().execute();
                break;
            case R.id.month_togglebutton:
                month_toggle.setChecked(true);
                setBeforeMonth(-1);
                harmQuery.add(1,new BasicNameValuePair("model_query", "{\"signDate\":{\"$gte\":\"" + harm_date + "\"}}"));
                recallQuery.add(1,new BasicNameValuePair("model_query", "{\"publicDate\":{\"$gte\":\"" + recall_date + "\"}}"));
                new JsonLoadingTask().execute();
                break;
            case R.id.year_togglebutton:
                year_toggle.setChecked(true);
                setBeforeMonth(-12);
                harmQuery.add(1, new BasicNameValuePair("model_query", "{\"signDate\":{\"$gte\":\"" + harm_date + "\"}}"));
                recallQuery.add(1,new BasicNameValuePair("model_query", "{\"publicDate\":{\"$gte\":\"" + recall_date + "\"}}"));
                new JsonLoadingTask().execute();
                break;

        }
    }

    public class JsonLoadingTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mProgress;

//        private  ArrayList<NameValuePair> harmQuery;
//        private  ArrayList<NameValuePair> recallQuery;
//
//        public JsonLoadingTask(ArrayList<NameValuePair> harmQuery, ArrayList<NameValuePair> recallQuery) {
//            this.harmQuery = harmQuery;
//            this.recallQuery= recallQuery;
//        }

        @Override
        protected Void doInBackground(Void... Params) {

            HandleJson handleJson = new HandleJson();

            harm_API = handleJson.getStringFromUrl("http://www.ibtk.kr/harm_api/2d81ae2bbea5f3b9e84555b7218a8b34?" + URLEncodedUtils.format(harmQuery, "utf-8"));
            recall_API = handleJson.getStringFromUrl("http://www.ibtk.kr/recallDetail_api/f896476dccab4fbc7f233725706b273e?" + URLEncodedUtils.format(recallQuery, "utf-8"));
            Log.i("날짜", harm_date.toString());
            harm_Adapter = handleJson.getJson(harm_date,"harmUpdate",harm_API, harm_FieldList, harm_FieldNameList);
            recall_Adapter = handleJson.getJson("recallUpdate",recall_API, recall_FieldList, recall_FieldNameList);
            Log.i("총갯수", String.valueOf(harm_Adapter.getGroupCount()));

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(HarmUpdateActivity.this);
            mProgress.setMessage("로딩중입니다.");
            mProgress.setCancelable(false);
            mProgress.show();

            //ProgressDialog dialog = ProgressDialog.show(mContext,"", "로딩중입니다 잠시만 기달려주세요.", true);

        }

        @Override
        protected void onPostExecute(Void result) {
            if(harm_Adapter.getGroupCount()==0||recall_Adapter.getGroupCount()==0){

                String toast = "표시 할 " ;
                if(harm_Adapter.getGroupCount()==0)
                    toast += "위해정보 ";
                if(recall_Adapter.getGroupCount()==0)
                    toast += "리콜정보 ";
                toast+="가 없습니다.";
                Toast.makeText(getApplicationContext(),toast,Toast.LENGTH_SHORT).show();
           }
                harm_ListView.setAdapter(harm_Adapter);
                recall_ListView.setAdapter(recall_Adapter);

            mProgress.dismiss();
        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.




    }

}
