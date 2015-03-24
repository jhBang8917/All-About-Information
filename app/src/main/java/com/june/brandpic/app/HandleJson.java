package com.june.brandpic.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by jhbang on 2015. 2. 4..
 */
public class HandleJson {

    public String getTotalElements() {
        return totalElements;
    }

    private String totalElements;

    public ArrayList getJsonArrList(){

        return null;
    }

    public CustomAdapter<ArrayList> getJson(String type, String url, ArrayList<String> field, ArrayList<String> fieldName) {
        CustomAdapter<ArrayList> adapter = new CustomAdapter<ArrayList>();

        adapter.setInfoType(type);
        try {
            JSONObject reader = new JSONObject(url);
            JSONArray jsonArr = new JSONArray(reader.getString("content"));
                totalElements  = reader.getString("totalElements");

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject arrObject = jsonArr.getJSONObject(i);
                ArrayList groupItem = new ArrayList();
                ArrayList groupTitle = new ArrayList();
                ArrayList childItem = new ArrayList();
                ArrayList childTitle = new ArrayList();
                for (int j = 0; j < field.size(); j++) {
                    String data=arrObject.getString((field.get(j)));
                    if(data.equals(""))
                        data="-";

                    if(j<4) {
                        groupItem.add(data);
                        groupTitle.add(fieldName.get(j));
                    }else {
                        childItem.add(data);
                        childTitle.add(fieldName.get(j));
                    }
                }
                adapter.addGroupItem(groupItem);
                adapter.addGroupTitle(groupTitle);
                adapter.addChildItem(childItem);
                adapter.addChildTitle(childTitle);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return adapter;
    }
    public CustomAdapter<ArrayList> getJson(String harmDate, String type, String url, ArrayList<String> field, ArrayList<String> fieldName) {
        CustomAdapter<ArrayList> adapter = new CustomAdapter<ArrayList>();
        int harmDateYear = Integer.parseInt(harmDate.substring(harmDate.length()-2,harmDate.length()));
        Log.i("연도추출",String.valueOf(harmDateYear));

        adapter.setInfoType(type);
        try {
            JSONObject reader = new JSONObject(url);
            JSONArray jsonArr = new JSONArray(reader.getString("content"));

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject arrObject = jsonArr.getJSONObject(i);
                ArrayList groupItem = new ArrayList();
                ArrayList groupTitle = new ArrayList();
                ArrayList childItem = new ArrayList();
                ArrayList childTitle = new ArrayList();
                String date = arrObject.getString("signDate");
                int year=0;
                if(date!=null&&!date.contains("-")){
                    year = Integer.parseInt(date.substring(date.lastIndexOf("/")+1,date.lastIndexOf("/")+3));
                    if (year>=harmDateYear) {

                        for (int j = 0; j < field.size(); j++) {
                            String data=arrObject.getString((field.get(j)));
                            if(data.equals(""))
                                data="-";

                            if(j<4) {
                                groupItem.add(data);
                                groupTitle.add(fieldName.get(j));
                            }else {
                                childItem.add(data);
                                childTitle.add(fieldName.get(j));
                            }
                        }
                        adapter.addGroupItem(groupItem);
                        adapter.addGroupTitle(groupTitle);
                        adapter.addChildItem(childItem);
                        adapter.addChildTitle(childTitle);
                    }
                } else
                    continue;


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return adapter;
    }

    public ArrayAdapter<ArrayList> getPopupAdapter(Context context, String url, String fieldName){
        ArrayList item = new ArrayList();
        ArrayAdapter<ArrayList> result;
        try {
            JSONObject reader = new JSONObject(url);
            JSONArray jsonArr = new JSONArray(reader.getString("content"));

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject arrObject = jsonArr.getJSONObject(i);
                String companyName = arrObject.getString(fieldName);
                    item.add(companyName);
            }
        }catch(Exception e){

            }
        result = new SimpleArrayAdapter<ArrayList>(context, R.layout.popup_listview_item, R.id.item_listview,item);
        return result;
    }

    public ArrayList<String> getAgencyData(String api, ArrayList<NameValuePair> query, ArrayList<String> field){
        ArrayList<String> result = new ArrayList<String>();
        try {
            String url = getStringFromUrl(api + URLEncodedUtils.format(query, "utf-8"));
            JSONObject reader = new JSONObject(url);
            Log.i("url",url);

            JSONArray jsonArr = new JSONArray(reader.getString("content"));

                JSONObject arrObject = jsonArr.getJSONObject(0);
            for (int i = 0; i < field.size(); i++) {
                String data = arrObject.getString(field.get(i));
                if(data.equals(""))
                    data="-";
                result.add(data);
                Log.i("데이터",data);
            }
        }catch(Exception e){

        }
        return result;
    }

    public ArrayList getGeoPoint(String api, ArrayList<NameValuePair> query,ArrayList<String> address){
        ArrayList result = new ArrayList();
        String address1 = address.get(0);
        String address2 = address.get(1);
        String combineAddress = "";
        Log.i("주소1 마지막 글자",String.valueOf(address1.substring(address1.length()-1,address1.length())));
        Log.i("주소2 첫번째 글자",String.valueOf(address2.substring(0,1)));

        if(isNum(address1.substring(address1.length()-1,address1.length()))){
            combineAddress=address1;
        }else if(isNum(address2.substring(0,1))){
            combineAddress=address1+" "+address2;
        }else{
            result.add(combineAddress);
            result.add(0.0);
            result.add(0.0);
            return result;
        }

        Log.i("주소결과",combineAddress);
        result.add(combineAddress);
        query.add(new BasicNameValuePair("query", combineAddress));

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            InputStream url = getInputStreamFromUrl(api + URLEncodedUtils.format(query, "utf-8"));
            parser.setInput(url, "UTF-8");
            int eventType = parser.getEventType();
            Log.i("이벤트타입", String.valueOf(eventType));
            while (eventType != XmlPullParser.END_DOCUMENT&&result.size()!=3) {

                if (eventType == XmlPullParser.START_TAG) {


                    String startTag = parser.getName();

//                if(startTag.equals("item")) { search = new SearchData(); }
//
//                //방법 1. 속성값일때 얻기
//                p.setName(parser.getAttributeValue(0));
//                p.setAge(parser.getAttributeValue(1));
//                p.setAddress(parser.getAttributeValue(2));
//
//                //방법 2. 태그값일때 얻기
//                if(search != null) {

                    if (startTag.equals("address")) {
                        String temp = parser.nextText();
                        Log.i("주소결과", temp);
                    } else if (startTag.equals("x")) {
                        String temp = parser.nextText();
                        Log.i("x", temp);
                        result.add(Double.parseDouble(temp));
                    }else if (startTag.equals("y")) {
                        String temp = parser.nextText();
                        Log.i("y", temp);
                        result.add(Double.parseDouble(temp));
                    }
//                    if(startTag.equals("link")) { search.setLink(parser.nextText()); }
//                    if(startTag.equals("description")) { search.setDescription(parser.nextText()); }
//                    if(startTag.equals("telephone")) { search.setTelephone(parser.nextText()); }
//                    if(startTag.equals("address")) { search.setAddress(parser.nextText()); }
//                    if(startTag.equals("mapx")) { search.setMapx(parser.nextText()); }
//                    if(startTag.equals("mapy")) { search.setMapy(parser.nextText()); }
//                }


//            case XmlPullParser.END_TAG:
//
//                String endTag = parser.getName();
//                if(endTag.equals("item")) { list.add(search); }

                }

                eventType = parser.next();
                Log.i("반복", "반복");
                Log.i("사이즈", String.valueOf(result.size()));

            }

        } catch (Exception e) {

        }

        if(result.size()<2){
            result.add(0.0);
            result.add(0.0);
        }

        Log.i("result 사이즈",String.valueOf(result.size()));
        return result;
    }


    // getStringFromUrl : 주어진 URL 페이지를 문자열로 얻는다.
    public String getStringFromUrl(String url) {


        // 읽은 데이터를 저장한 StringBuffer 를 생성한다.
        StringBuffer sb = new StringBuffer();

        try {
            // 입력스트림을 "UTF-8" 를 사용해서 읽은 후, 라인 단위로 데이터를 읽을 수 있는 BufferedReader 를 생성한다.
            BufferedReader br = new BufferedReader(new InputStreamReader(getInputStreamFromUrl(url), "UTF-8"));
            // 라인 단위로 읽은 데이터를 임시 저장한 문자열 변수 line
            String line = null;

            // 라인 단위로 데이터를 읽어서 StringBuffer 에 저장한다.
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    } // getStringFromUrl

    // getInputStreamFromUrl : 주어진 URL 에 대한 입력 스트림(InputStream)을 얻는다.
    public InputStream getInputStreamFromUrl(String url) {
        InputStream contentStream = null;
        Log.i("전체Url",url);
        try {
            // HttpClient 를 사용해서 주어진 URL에 대한 입력 스트림을 얻는다.
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            contentStream = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentStream;
    } // getInputStreamFromUrl

    private boolean isNum(String str){
        Log.i("isNum에들어온str", str);
        if(Pattern.matches("^[0-9]+$", str)){
            Log.i("isnum", "true");
            return true;
        }else{
            Log.i("isnum", "false");
            return false;
        }
    }

}
