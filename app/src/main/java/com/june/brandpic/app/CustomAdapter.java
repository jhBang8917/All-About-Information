package com.june.brandpic.app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhbang on 2015. 2. 7..
 */
public class CustomAdapter<T extends List> extends BaseExpandableListAdapter {

    private ArrayList<T> groupList;
    private ArrayList<T> groupTitleList;
    private ArrayList<T> childList;
    private ArrayList<T> childTitleList;
    private String infoType;
    private String imageUrl;

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public CustomAdapter() {
        groupList = new ArrayList<T>();
        groupTitleList = new ArrayList<T>();
        childList = new ArrayList<T>();
        childTitleList = new ArrayList<T>();
    }



    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition ;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if (convertView == null) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_grouplistview, parent, false);
        }else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_grouplistview, parent, false);
        }



        if (infoType.equals("certification")) {
//                if (groupList.get(groupPosition).get(0).toString().equals("적합")) {
//                    convertView.findViewById(R.id.grouplistview).setBackgroundResource(R.drawable.brandresult_table_appropriate);
//                }
//                else if (groupList.get(groupPosition).get(0).toString().equals("신규")) {
//                    convertView.findViewById(R.id.grouplistview).setBackgroundResource(R.drawable.brandresult_table_new);
//                }
//                else if (groupList.get(groupPosition).get(0).toString().equals("변경")) {
//                    convertView.findViewById(R.id.grouplistview).setBackgroundResource(R.drawable.brandresult_table_change);
//                }
//                else if (groupList.get(groupPosition).get(0).toString().equals("인증이관")) {
//                    convertView.findViewById(R.id.grouplistview).setBackgroundResource(R.drawable.brandresult_table_move);
//                }
//                else if (groupList.get(groupPosition).get(0).toString().equals("업체취소")) {
//                    convertView.findViewById(R.id.grouplistview).setBackgroundResource(R.drawable.brandresult_table_cancle);
//                }
//                else {
//                    convertView.findViewById(R.id.grouplistview).setBackgroundResource(R.drawable.brandresult_table_none);
//                }
            convertView.findViewById(R.id.grouplistview).setBackgroundResource(R.drawable.brandresult_table);
            TextView tv = (TextView)convertView.findViewById(R.id.status_textview);
            String status = groupList.get(groupPosition).get(0).toString();
            String[] array = convertView.getResources().getStringArray(R.array.division_spinner);
            for (int i = 0; i <array.length; i++) {

                if(status.equals(array[i])){
                    tv.setText(status);
                    tv.setSelected(true);

                }

            }

                // TextView에 현재 position의 문자열 추가


            } else if (infoType.equals("harmUpdate")||infoType.equals("recallUpdate")) {
            ((TextView)convertView.findViewById(R.id.content1_textview)).setTextColor(Color.parseColor("#ff468CCB"));
            ImageView image = (ImageView)convertView.findViewById(R.id.indicatior_imageview);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(image.getLayoutParams());
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp.setMargins(0, 0, 0, 10);
            image.setLayoutParams(lp);

            }

            TextView title1 = (TextView) convertView.findViewById(R.id.title1_textview);
            title1.setText(groupTitleList.get(groupPosition).get(1).toString());

            TextView title2 = (TextView) convertView.findViewById(R.id.title2_textview);
            title2.setText(groupTitleList.get(groupPosition).get(2).toString());
            TextView title3 = (TextView) convertView.findViewById(R.id.title3_textview);
            title3.setText(groupTitleList.get(groupPosition).get(3).toString());

            TextView content1 = (TextView) convertView.findViewById(R.id.content1_textview);
            content1.setText(groupList.get(groupPosition).get(1).toString());
            content1.setSelected(true);
            TextView content2 = (TextView) convertView.findViewById(R.id.content2_textview);
            content2.setText(groupList.get(groupPosition).get(2).toString());
//            content2.setSelected(true);
            TextView content3 = (TextView) convertView.findViewById(R.id.content3_textview);
            content3.setText(groupList.get(groupPosition).get(3).toString());
//            content3.setSelected(true);

        if(isExpanded){
            convertView.findViewById(R.id.indicatior_imageview).setBackgroundResource(R.drawable.common_indicator_minus);
            if(infoType.equals("certification"))
                convertView.findViewById(R.id.grouplistview).setBackgroundResource(R.drawable.brandresult_table_expand);
            content2.setSelected(true);
            content3.setSelected(true);

        }else{
        }




        return convertView;
    }

    private void makeTextViewHyperlink(TextView textview) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append(textview.getText());
        ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(ssb, TextView.BufferType.SPANNABLE);
        textview.setSingleLine();
        textview.setEllipsize(TextUtils.TruncateAt.END);
//        textview.setMarqueeRepeatLimit(-1);
//        textview.setSelected(true);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();


                // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
                if (convertView == null) {
                    // view가 null일 경우 커스텀 레이아웃을 얻어 옴
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_childlistview, parent, false);
                }else{
                    // view가 null일 경우 커스텀 레이아웃을 얻어 옴
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_childlistview, parent, false);
                }

                    // TextView에 현재 position의 문자열 추가
                    TextView title1 = (TextView) convertView.findViewById(R.id.title1_child_textview);
                    title1.setText(childTitleList.get(groupPosition).get(0).toString());
                    TextView title2 = (TextView) convertView.findViewById(R.id.title2_child_textview);
                    title2.setText(childTitleList.get(groupPosition).get(1).toString());
                    TextView title3 = (TextView) convertView.findViewById(R.id.title3_child_textview);
                    title3.setText(childTitleList.get(groupPosition).get(2).toString());
                    TextView title4 = (TextView) convertView.findViewById(R.id.title4_child_textview);
                    title4.setText(childTitleList.get(groupPosition).get(3).toString());
                    TextView title5 = (TextView) convertView.findViewById(R.id.title5_child_textview);
                    title5.setText(childTitleList.get(groupPosition).get(4).toString());

                    TextView content1 = (TextView) convertView.findViewById(R.id.content1_child_textview);
                    content1.setText(childList.get(groupPosition).get(0).toString());
                    TextView content2 = (TextView) convertView.findViewById(R.id.content2_child_textview);
                    content2.setText(childList.get(groupPosition).get(1).toString());
                    TextView content3 = (TextView) convertView.findViewById(R.id.content3_child_textview);
                    content3.setText(childList.get(groupPosition).get(2).toString());
                    TextView content4 = (TextView) convertView.findViewById(R.id.content4_child_textview);
                    content4.setText(childList.get(groupPosition).get(3).toString());
                    TextView content5 = (TextView) convertView.findViewById(R.id.content5_child_textview);
                    content5.setText(childList.get(groupPosition).get(4).toString());

        if(infoType.equals("certification")){
            convertView.findViewById(R.id.childlistview).setBackgroundResource(R.drawable.brandresult_table_expand_child);
            makeTextViewHyperlink(content1);

            content1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String companyName = ((TextView)v).getText().toString();
//                    Intent intentActivity = new Intent(context, AgencyResultActivity.class);
//                    intentActivity.putExtra("companyName", companyName);
//                    context.startActivity(intentActivity);
                    DialogFragment dialog = new CustomDialog().newInstance(R.layout.popup_listview,companyName.substring(0,4),"agency");
                    dialog.show(((Activity)context).getFragmentManager(), "test");
                }
            });


        }



                    if(infoType.equals("recallUpdate")){
                        imageUrl = childList.get(groupPosition).get(3).toString();
                        content4.setText("이미지보기");
                        content4.setSelected(false);
                        makeTextViewHyperlink(content4);
                            content4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(imageUrl.toLowerCase().contains("jpg")||imageUrl.toLowerCase().contains("png")||imageUrl.toLowerCase().contains("gif")) {
                                        DialogFragment dialog = new ImageDialog().newInstance(R.layout.popup_image, imageUrl);
                                        dialog.show(((Activity) context).getFragmentManager(), "test");
                                    }else
                                        Toast.makeText(context,"이미지가 없습니다.",Toast.LENGTH_SHORT).show();

                                }
                            });

                    }

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void addGroupItem(T item) {
        groupList.add(item);
    }
    public void addGroupTitle(T item) {groupTitleList.add(item);}
    public void addChildItem(T item) {
        childList.add(item);
    }
    public void addChildTitle(T item) {
        childTitleList.add(item);
    }


    //
    public int getArrListSize() {
        return groupList.get(1).size();
    }

    public String getArrList(int a) {
        return groupList.get(a).toString();
    }
}
