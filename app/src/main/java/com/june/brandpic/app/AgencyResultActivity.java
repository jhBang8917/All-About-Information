package com.june.brandpic.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.jar.Attributes;

/**
 * Created by jhbang on 2015. 2. 12..
 */
public class AgencyResultActivity extends CommonNMapActivity implements View.OnTouchListener {

    private NMapView mMapView;
    private NMapController mMapController;
    private ArrayList<NameValuePair> geoQuery;
    private String companyName;
    private ArrayList geoPoint;
    private ArrayList<NameValuePair> query;
    private ArrayList<String> agency_FieldList;
    private ArrayList<String> agency_FieldNameList;
    private ArrayList<String> agency_Data;
    private TextView title1;
    private TextView title2;
    private TextView title3;
    private TextView content1;
    private TextView content2;
    private TextView content3;
    private TextView agencyName;
    private NMapResourceProvider mMapResourceProvider;
    private NMapOverlayManager mOverlayManager;
    private NMapViewerResourceProvider mMapViewerResourceProvider;
    private NMapPOIdata poiData;
    private int markerId;
    private Double geoPointX;
    private Double geoPointY;
    private String combineAddress;
    private Button nMapMoveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agencyresult);
        setMenuTilte("기관 정보");

        super.findViewById(R.id.bottomButton2).setBackgroundResource(R.drawable.common_button_menu_brand);

        super.findViewById(R.id.bottomButton1).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(AgencyResultActivity.this, HarmUpdateActivity.class);
                startActivity(intentActivity);
            }
        });
        super.findViewById(R.id.bottomButton2).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentActivity = new Intent(AgencyResultActivity.this, BrandSearchActivity.class);
                startActivity(intentActivity);
            }
        });

        agencyName = (TextView)findViewById(R.id.agencyname_textview);

        title1 = (TextView)findViewById(R.id.agencytitle1_textview);
        title2 = (TextView)findViewById(R.id.agencytitle2_textview);
        title3 = (TextView)findViewById(R.id.agencytitle3_textview);

        content1 =(TextView)findViewById(R.id.agencycontent1_textview);
        content2 =(TextView)findViewById(R.id.agencycontent2_textview);
        content3 =(TextView)findViewById(R.id.agencycontent3_textview);

        agencyName.setOnTouchListener(this);
        content1.setOnTouchListener(this);
        content3.setOnTouchListener(this);

        nMapMoveButton = (Button)findViewById(R.id.nmapmove_button);


        Intent intent = getIntent();
        companyName = intent.getStringExtra("companyName");
        Log.i("company",companyName);
        // create map view
        mMapView = (NMapView) findViewById(R.id.naver_mapView);
// set a registered API key for Open MapViewer Library
        mMapView.setApiKey("aae9cbe5fc60bd6e235b4c9bbf448e3b");

// set the activity content to the map view

// initialize map view
        mMapView.setClickable(true);

// register listener for map state changes

//        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
//        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

// use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();



        //아래부터 마커표시하는거거

       // create resource provider
        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

// create overlay manager
        mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

        markerId = NMapPOIflagType.PIN;

// set POI data
        poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        poiData.beginPOIdata(2);


        geoQuery = new ArrayList<NameValuePair>();
        geoQuery.add(new BasicNameValuePair("key", "48dedd933d28d22f52159d502a116812"));
        geoQuery.add(new BasicNameValuePair("encoding", "utf-8"));
        geoQuery.add(new BasicNameValuePair("coord", "latlng"));


        query = new ArrayList<NameValuePair>();
        query.add(new BasicNameValuePair("model_query_pageable", "{enable:true}"));
        query.add(new BasicNameValuePair("model_query_distinct", "company"));
        query.add(new BasicNameValuePair("model_query", "{\"company\":\""+companyName+"\"}"));
//        geoQuery.add(new BasicNameValuePair("query", "서울시 노원구 상계9"));
        agency_FieldList = new ArrayList<String>();
        agency_FieldList.add("company");
        agency_FieldList.add("rangepower");
        agency_FieldList.add("corporatephone");
        agency_FieldList.add("corporateaddr");
        agency_FieldList.add("corporateaddr2");

        agency_FieldNameList = new ArrayList<String>();
        agency_FieldNameList.add("기관명");
        agency_FieldNameList.add("측정능력");
        agency_FieldNameList.add("전화번호");
        agency_FieldNameList.add("주소");


        new JsonLoadingTask().execute();


    }

    private InputStream getInputStream(String para_url) {
        while (true) {
            try {
                URL url = new URL(para_url);
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();
                return is;
            } catch (Exception e) {
            }
        }
    }

    public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) { // success
            mMapController.setMapCenter(new NGeoPoint(309189, 522757));
        } else { // fail
//            Log.e("onMapInitHandler: error=" + errInfo.toString());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        String toast = "";
        switch (id){
            case R.id.agencyname_textview:
                toast = agencyName.getText().toString();
                break;

            case R.id.agencycontent1_textview:
                toast = content1.getText().toString();
                break;

            case R.id.agencycontent3_textview:
                toast = content3.getText().toString();
                break;

        }
        Toast.makeText(getApplicationContext(),toast,Toast.LENGTH_SHORT).show();
        return false;
    }

//    mMapView.setBuiltInZoomControls(true, null);

    public class JsonLoadingTask extends AsyncTask<Void, Void, Void> {


        private ProgressDialog mProgress;

        @Override
        protected Void doInBackground(Void... Params) {
            HandleJson handleJson = new HandleJson();

            agency_Data = handleJson.getAgencyData("http://www.ibtk.kr/inspectionAgencyDetail_api/1d95f94f14082f635f1af9d044debbdd?",query, agency_FieldList);
            ArrayList<String> address = new ArrayList<String>();
            address.add(agency_Data.get(3).toString());
            address.add(agency_Data.get(4).toString());
            Log.i("주소2개", address.toString());
            geoPoint = handleJson.getGeoPoint("http://openapi.map.naver.com/api/geocode.php?", geoQuery,address);
            combineAddress = (String)geoPoint.get(0);
            geoPointX = (Double)geoPoint.get(1);
            geoPointY = (Double)geoPoint.get(2);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(AgencyResultActivity.this);
            mProgress.setMessage("로딩중입니다.");
            mProgress.setCancelable(false);
            mProgress.show();

            //ProgressDialog dialog = ProgressDialog.show(mContext,"", "로딩중입니다 잠시만 기달려주세요.", true);

        }

        @Override
        protected void onPostExecute(Void result) {
            agencyName.setText(agency_Data.get(0));
            agencyName.setSelected(true);
            title1.setText(agency_FieldNameList.get(1));
            title2.setText(agency_FieldNameList.get(2));
            title3.setText(agency_FieldNameList.get(3));
            content1.setText(agency_Data.get(1));
            content1.setSelected(true);
            content2.setText(agency_Data.get(2));
            content3.setText(combineAddress);
            content3.setSelected(true);


            NGeoPoint ngeopoint = new NGeoPoint(geoPointX,geoPointY);
            Log.i("x", geoPointX.toString());
            Log.i("y", geoPointY.toString());

            if(geoPointX==0){
                Toast.makeText(getApplicationContext(),"주소정보가 정확하지 않습니다.\n네이버 지도 앱을 통해 검색해주세요.",Toast.LENGTH_SHORT).show();

            }else{
                mMapController.setMapCenter(ngeopoint);
                poiData.addPOIitem(ngeopoint, companyName, markerId, 0);
                poiData.endPOIdata();
// create POI data overlay
                NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);




                poiDataOverlay.showAllPOIdata(0);
            }


            findViewById(R.id.agencycall_button).setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+agency_Data.get(2))));
                }
            });
            nMapMoveButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMapView.setBuiltInAppControl(true);
                    mMapView.executeNaverMap();
                    mMapView.setBuiltInAppControl(false);
                }
            });
            mProgress.dismiss();

        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.


    }

}
