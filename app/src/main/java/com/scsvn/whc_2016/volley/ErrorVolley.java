package com.scsvn.whc_2016.volley;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.scsvn.whc_2016.R;

/**
 * Created by Trần Xuân Lộc on 12/26/2015.
 */
public class ErrorVolley {
    private static Snackbar snackbar;

    public static void errorNoAction(Context context, VolleyError error, String TAG, View view) {
        if (error instanceof NoInternet) {
            snackbar = Snackbar.make(view, context.getString(R.string.no_internet), Snackbar.LENGTH_LONG);

        } else if (error instanceof NoConnectionError || error instanceof TimeoutError) {
            snackbar = Snackbar.make(view, context.getString(R.string.timeout_conn), Snackbar.LENGTH_LONG);
        } else if (error instanceof AuthFailureError) {
            snackbar = Snackbar.make(view, context.getString(R.string.error_system), Snackbar.LENGTH_LONG);
            Log.e(TAG, "AuthFailureError");
        } else if (error instanceof ServerError) {
            snackbar = Snackbar.make(view, context.getString(R.string.error_system), Snackbar.LENGTH_LONG);
            Log.e(TAG, "ServerError");
        } else if (error instanceof NetworkError) {
            snackbar = Snackbar.make(view, context.getString(R.string.error_system), Snackbar.LENGTH_LONG);
            Log.e(TAG, "NetworkError");
        } else if (error instanceof ParseError) {
            snackbar = Snackbar.make(view, context.getString(R.string.error_system), Snackbar.LENGTH_LONG);
            Log.e(TAG, "ParseError");
        }
        snackbar.show();
    }

    public static void errorWithAction(Context context, VolleyError error, String TAG, View view, View.OnClickListener action) {
        if (error instanceof NoInternet) {
            snackbar = Snackbar.make(view, context.getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE).setAction(R.string.try_again, action);
        } else if (error instanceof NoConnectionError || error instanceof TimeoutError) {
            snackbar = Snackbar.make(view, context.getString(R.string.timeout_conn), Snackbar.LENGTH_INDEFINITE).setAction(R.string.try_again, action);
        } else if (error instanceof AuthFailureError) {
            snackbar = Snackbar.make(view, context.getString(R.string.error_system), Snackbar.LENGTH_INDEFINITE).setAction(R.string.try_again, action);
            Log.e(TAG, "AuthFailureError");
        } else if (error instanceof ServerError) {
            snackbar = Snackbar.make(view, context.getString(R.string.error_system), Snackbar.LENGTH_INDEFINITE).setAction(R.string.try_again, action);
            Log.e(TAG, "ServerError");
        } else if (error instanceof NetworkError) {
            snackbar = Snackbar.make(view, context.getString(R.string.error_system), Snackbar.LENGTH_INDEFINITE).setAction(R.string.try_again, action);
            Log.e(TAG, "NetworkError");
        } else if (error instanceof ParseError) {
            snackbar = Snackbar.make(view, context.getString(R.string.error_system), Snackbar.LENGTH_INDEFINITE).setAction(R.string.try_again, action);
            Log.e(TAG, "ParseError");
        }
        snackbar.show();
    }

    public static Snackbar getSnackbar() {
        return snackbar;
    }
}
