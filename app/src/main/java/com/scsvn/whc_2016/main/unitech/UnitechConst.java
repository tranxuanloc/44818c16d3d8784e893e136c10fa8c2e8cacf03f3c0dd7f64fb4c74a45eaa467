package com.scsvn.whc_2016.main.unitech;

import android.os.Build;

/**
 * Created by tranxuanloc on 3/16/2016.
 */
public class UnitechConst {
    public static final String SCAN2KEY_SETTING = "unitech.scanservice.scan2key_setting";
    public static final String RECEIVE_DATA = "unitech.scanservice.data";
    public static final String SAVE_SETTING = "unitech.scanservice.save_setting";
    public static final String INIT_SCAN = "unitech.scanservice.ini";
    public static final String LOAD_SETTING = "unitech.scanservice.load_setting";
    public static final String START_SCAN_SERVICE = "unitech.scanservice.start";
    public static final String CLOSE_SCAN_SERVICE = "unitech.scanservice.close";
    public static final String SOFTWARE_SCAN_KEY = "unitech.scanservice.software_scankey";

    public static boolean isUnitech() {
        return Build.MODEL.contains("PA700");
    }
}
