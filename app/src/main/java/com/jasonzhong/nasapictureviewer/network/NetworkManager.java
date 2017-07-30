package com.jasonzhong.nasapictureviewer.network;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import java.util.Map;

/**
 * Created by junzhong on 2017-07-28.
 */

public class NetworkManager {

    RequestQueue mRequestQueue;
    static NetworkManager mInstance;

    Context context;

    private NetworkManager(Context context) {
        this.context = context;
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (mInstance == null) mInstance = new NetworkManager(context.getApplicationContext());

        return mInstance;
    }

    public interface ResponseHandler {

        void onSuccess(String response);

        void onError(VolleyError error);

    }

    public interface ImageResponseHandler {
        void onSuccess(Bitmap arg0);

        void onError(VolleyError error);
    }

    private RequestQueue getVolleyRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue
                    (context, new OkHttpStack(new OkHttpClient()));
        }

        return mRequestQueue;
    }

    public void processVolleyGetRequest(final String url, final ResponseHandler responseHandler, final String TAG) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseHandler.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseHandler.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            public Priority getPriority() {
                return Priority.NORMAL;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        stringRequest.setTag(TAG);
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 0, 1.0f));
        getVolleyRequestQueue().add(stringRequest);
    }
    public void processImageVolleyRequest(final String url, final ImageResponseHandler imageResponseHandler, final String TAG) {
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap arg0) {
                        imageResponseHandler.onSuccess(arg0);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageResponseHandler.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }
        };
        imgRequest.setTag(TAG);
        imgRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, 1, 1.0f));
        getVolleyRequestQueue().add(imgRequest);
    }

}
