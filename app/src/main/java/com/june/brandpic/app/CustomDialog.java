package com.june.brandpic.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by jhbang on 205. 2. 20..
 */
public class CustomDialog extends DialogFragment {


    private ListView listview;
    private ArrayList<NameValuePair> queryList;
    private String searchText;
    private String type;
    private View v;
    private ProgressDialog mProgress;


    //    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        int title = getArguments().getInt("title");
//        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
//
//
//    }
//                .create();
//                .setTitle(title)
////                .setIcon(R.drawable.alert_dialog_icon)
    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.popup_width); // specify a value here
        int dialogHeight = getResources().getDimensionPixelSize(R.dimen.popup_height); // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("로딩중입니다.");
        mProgress.setCancelable(false);
        mProgress.show();
        new JsonLoadingTask().execute();

    }

    public static CustomDialog newInstance(int viewId, String searchText, String type) {
        CustomDialog frag = new CustomDialog();
        Bundle args = new Bundle();
        args.putInt("viewId", viewId);
        args.putString("searchText", searchText);
        args.putString("type", type);
        frag.setArguments(args);
        return frag;
    }

//    @Override
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }


//        return new AlertDialog.Builder(getActivity())

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int viewId = getArguments().getInt("viewId");
         type = getArguments().getString("type");
         searchText = getArguments().getString("searchText");

        v = inflater.inflate(viewId, container, false);
        listview = (ListView) v.findViewById(R.id.popup_listview);

        if(type.equals("brand")) {
            ((TextView)v.findViewById(R.id.popup_listview_title)).setText("브랜드 선택");
            queryList = new ArrayList<NameValuePair>();
            queryList.add(new BasicNameValuePair("model_query_pageable", "{enable:true,pageSize:10000}"));
            queryList.add(new BasicNameValuePair("model_query_distinct", "sb_makingCompany"));
            queryList.add(new BasicNameValuePair("model_query", "{\"sb_makingCompany\":{\"$regex\":\"" + searchText + "\"}}"));

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String companyName = (String) parent.getAdapter().getItem(position);
                    Intent intentActivity = new Intent(getActivity().getApplicationContext(), BrandResultActivity.class);
                    intentActivity.putExtra("companyName", companyName);
                    startActivity(intentActivity);
                }

            });
        }else if(type.equals("agency")){
            ((TextView)v.findViewById(R.id.popup_listview_title)).setText("기관 선택");
            queryList = new ArrayList<NameValuePair>();
            queryList.add(new BasicNameValuePair("model_query_pageable", "{enable:true,pageSize:10000}"));
            queryList.add(new BasicNameValuePair("model_query_distinct", "company"));
            queryList.add(new BasicNameValuePair("model_query", "{\"company\":{\"$regex\":\"" + searchText + "\"}}"));

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String companyName = (String) parent.getAdapter().getItem(position);
                    Intent intentActivity = new Intent(getActivity().getApplicationContext(), AgencyResultActivity.class);
                    intentActivity.putExtra("companyName", companyName);
                    startActivity(intentActivity);
                }

            });
        }

        return v;
    }

    public class JsonLoadingTask extends AsyncTask<Void, Void, Void> {


        private ArrayAdapter<ArrayList> adapter;

        @Override
        protected Void doInBackground(Void... Params) {
            json();
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            if(adapter.getCount()==0){
                Log.i("getcount", String.valueOf(adapter.getCount()));
                Toast.makeText(getActivity(),"회사 정보가 없습니다.\n다시 검색해주세요.",Toast.LENGTH_SHORT).show();
                getDialog().cancel();
            }else{
                listview.setAdapter(adapter);
            }

            mProgress.dismiss();
        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.

        private void json(){
            HandleJson handlejson = new HandleJson();
            String url="";
            if(type.equals("brand")){
                url = handlejson.getStringFromUrl("http://www.ibtk.kr/api_confirm_detail/a334b557dcc627e0d61990cc14ea8c52?" + URLEncodedUtils.format(queryList, "utf-8"));
                adapter = handlejson.getPopupAdapter(getActivity().getApplicationContext(),url,"sb_makingCompany");
            }else if(type.equals("agency")){
                url = handlejson.getStringFromUrl("http://www.ibtk.kr/inspectionAgencyDetail_api/1d95f94f14082f635f1af9d044debbdd?"+ URLEncodedUtils.format(queryList, "utf-8"));
                adapter = handlejson.getPopupAdapter(getActivity().getApplicationContext(),url,"company");
            }
        }


    } // JsonLoadingTask
}
