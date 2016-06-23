package com.scsvn.whc_2016.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class MyPOST {
    public static String MyPOST = "MyPOST";
    private static RequestQueue requestQueue;

    private MyPOST() {
    }

    public static void post(final Context context, String url, final Map<String, String> params, final VolleyCallBack callback) {
        if (!MyNetwork.isConnected(context)) {
            callback.onError(new NoInternet());
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(MyPOST);
        queue.add(request);
        requestQueue = queue;
        queue.start();
    }

    public static void cancelRequest() {
        if (requestQueue != null)
            requestQueue.cancelAll(MyPOST);
    }


}



