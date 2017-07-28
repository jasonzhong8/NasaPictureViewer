package com.jasonzhong.nasapictureviewer.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

        void onError(String error);

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
                        // Display the first 500 characters of the response string.
                        responseHandler.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseHandler.onError("Error");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return null;
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


}
