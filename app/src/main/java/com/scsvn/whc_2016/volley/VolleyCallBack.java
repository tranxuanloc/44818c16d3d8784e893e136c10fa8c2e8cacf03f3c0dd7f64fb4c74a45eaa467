package com.scsvn.whc_2016.volley;

import com.android.volley.VolleyError;


public interface VolleyCallBack {
    void onSuccess(String response);

    void onError(VolleyError error);


}
