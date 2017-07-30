package com.jasonzhong.nasapictureviewer.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.jasonzhong.nasapictureviewer.R;
import com.jasonzhong.nasapictureviewer.models.NasaPicInfo;
import com.jasonzhong.nasapictureviewer.network.NetworkManager;
import com.jasonzhong.nasapictureviewer.util.CustomAnimator;
import com.jasonzhong.nasapictureviewer.util.JsonParser;
import com.jasonzhong.nasapictureviewer.util.Util;

public class NasaPicMainActivity extends Activity {

    private String VOLLEY_TAD = "Volley_tag";
    private static final String NASA_PICTURE_URL = "https://api.nasa.gov/planetary/apod?api_key=NNKOjkoul8n1CH18TWA9gwngW1s1SmjESPjNoUFo";
    private ImageView nasaImageView, nasa_full_imageview;
    private TextView nasaTitleTextView;
    private RelativeLayout loadingPanel;
    private View nasa_main_frameLayout, nasa_full_pic_frameLayout;

    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_nasa_pic_main);

        init();
        sendGetNasaPicInfoVolleyRequest();
    }

    private void init() {
        nasa_main_frameLayout = findViewById(R.id.nasa_main_frameLayout);
        nasa_full_pic_frameLayout = findViewById(R.id.nasa_full_pic_frameLayout);
        nasa_full_imageview = (ImageView) findViewById(R.id.nasa_full_imageview);
        nasa_full_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAnimator.reversePrevious();
                final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration(2350);
                fadeIn.setFillAfter(true);
                nasaTitleTextView.startAnimation(fadeIn);
            }
        });

        nasaImageView = (ImageView) findViewById(R.id.nasa_imageview);
        nasaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nasa_full_pic_frameLayout.setVisibility(View.VISIBLE);
                nasa_full_imageview.setImageDrawable(drawable);
                final Animation out = new AlphaAnimation(1.0f, 0.0f);

                out.setDuration(50);
                out.setFillAfter(true);
                nasaTitleTextView.startAnimation(out);
                CustomAnimator.slide(nasa_full_pic_frameLayout, nasa_main_frameLayout, CustomAnimator.DIRECTION_LEFT, 350);
            }
        });
        nasaTitleTextView = (TextView) findViewById(R.id.nasa_title_textview);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingPanel);
    }

    protected void sendGetNasaPicInfoVolleyRequest() {

        NetworkManager.getInstance(this).processVolleyGetRequest(NASA_PICTURE_URL, new NetworkManager.ResponseHandler() {
            @Override
            public void onSuccess(final String response) {
                ProcessGetNasaPicInfo(response);
            }

            @Override
            public void onError(final VolleyError error) {
                hideLoadingPanel();
                Util.openAlertDialog(NasaPicMainActivity.this, "Error", NasaPicMainActivity.this.getResources().getString(R.string.network_error));
            }
        }, VOLLEY_TAD);
    }

    private void ProcessGetNasaPicInfo(String result) {

        if (result == null || result.equals("")) {
            hideLoadingPanel();
            return;
        }
        try {
            NasaPicInfo nasaPicInfo = JsonParser.parseNasaPicInfo(result);
            if (nasaPicInfo != null && nasaPicInfo.getUrl() != null && !nasaPicInfo.getUrl().equalsIgnoreCase("") && nasaImageView != null && nasaTitleTextView != null) {
                loadImage(nasaPicInfo.getUrl(), nasaImageView);
                nasaTitleTextView.setText(nasaPicInfo.getTitle());
            } else {
                hideLoadingPanel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            hideLoadingPanel();
        }
    }

    private void loadImage(final String imageUrl, final View view) {
        NetworkManager.getInstance(this).processImageVolleyRequest(imageUrl, new NetworkManager.ImageResponseHandler() {
            @Override
            public void onSuccess(final Bitmap bitmap) {
                if (bitmap != null) {
                    int desiredHeight = Util.getScreenHeight(NasaPicMainActivity.this) / 2;
                    hideLoadingPanel();
                    drawable = new BitmapDrawable(getResources(), Util.scaleBitmap(bitmap, desiredHeight));
                    ((ImageView) view).setImageDrawable(drawable);
                }
            }

            @Override
            public void onError(final VolleyError error) {
                hideLoadingPanel();
                Util.openAlertDialog(NasaPicMainActivity.this, "Error", NasaPicMainActivity.this.getResources().getString(R.string.network_error));
            }
        }, VOLLEY_TAD);
    }

    private void hideLoadingPanel() {
        if (loadingPanel != null) {
            loadingPanel.setVisibility(View.GONE);
        }
    }

}
