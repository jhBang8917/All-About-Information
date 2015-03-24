package com.june.brandpic.app;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jhbang on 205. 2. 20..
 */
public class ImageDialog extends DialogFragment {


    private ProgressDialog mProgress;
    private String imageUrl;
    private View v;
    private ImageView imageView;
    private Bitmap bmp;
    private URL url;

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null)
            return;

        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.imagepopup_width); // specify a value here
        int dialogHeight = getResources().getDimensionPixelSize(R.dimen.imagepopup_height); // specify a value here

        getDialog().getWindow().setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        mProgress = new ProgressDialog(getActivity());
        mProgress.setMessage("로딩중입니다.");
        mProgress.setCancelable(false);
        mProgress.show();
        new JsonLoadingTask().execute();

    }

    public static ImageDialog newInstance(int viewId, String imageUrl) {
        ImageDialog frag = new ImageDialog();
        Bundle args = new Bundle();
        args.putInt("viewId", viewId);
        args.putString("imageUrl", imageUrl);
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
        imageUrl = getArguments().getString("imageUrl");
        if(imageUrl.toLowerCase().contains("jpg"))
            imageUrl = imageUrl.substring(0,imageUrl.toLowerCase().indexOf("jpg")+3);
        else if(imageUrl.toLowerCase().contains("png"))
            imageUrl = imageUrl.substring(0,imageUrl.toLowerCase().indexOf("png")+3);
        else if(imageUrl.toLowerCase().contains("gif"))
            imageUrl = imageUrl.substring(0,imageUrl.toLowerCase().indexOf("gif")+3);

        Log.i("이미주지소", imageUrl);

        v = inflater.inflate(viewId, container, false);
        imageView = (ImageView) v.findViewById(R.id.imageviewPopup);


       return v;
    }

    public class JsonLoadingTask extends AsyncTask<Void, Void, Void> {


        private ArrayAdapter<ArrayList> adapter;

        @Override
        protected Void doInBackground(Void... Params) {
            url = null;
            bmp = null;
            try {
                url = new URL(imageUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            imageView.setImageBitmap(bmp);
            mProgress.dismiss();
        } // onPostExecute : 백그라운드 작업이 끝난 후 UI 작업을 진행한다.




    } // JsonLoadingTask
}
