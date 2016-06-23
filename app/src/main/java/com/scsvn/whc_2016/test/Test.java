package com.scsvn.whc_2016.test;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.scsvn.whc_2016.volley.MyPOST;
import com.scsvn.whc_2016.volley.VolleyCallBack;

import java.util.HashMap;

/**
 * Created by Trần Xuân Lộc on 1/14/2016.
 */
public class Test {
    public void test(Context context, final String TAG) {
        HashMap<String, String> params = new HashMap<>();
        params.put("UserName", "49");
        MyPOST.post(context, "http://115.78.13.10:810/api/EmployeeWorking", params, new VolleyCallBack() {
            @Override
            public void onSuccess(String response) {
                Log.e(TAG, "onSuccess: " + response);
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "onError: " + error.networkResponse + error);
            }
        });
    }
}
