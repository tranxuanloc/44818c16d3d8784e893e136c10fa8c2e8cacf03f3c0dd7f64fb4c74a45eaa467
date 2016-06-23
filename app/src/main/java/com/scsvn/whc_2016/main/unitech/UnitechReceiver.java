package com.scsvn.whc_2016.main.unitech;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by tranxuanloc on 3/16/2016.
 */
public class UnitechReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("unitech.scanservice.data".equals(intent.getAction())) {
            System.out.println("unitech.scanservice.data!");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String text = bundle.getString("text");

            }
        }
    }
}
