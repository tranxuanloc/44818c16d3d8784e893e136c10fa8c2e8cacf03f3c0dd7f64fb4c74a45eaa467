package com.scsvn.whc_2016.utilities;

import android.app.AlarmManager;

import java.util.ArrayList;

/**
 * Created by tranxuanloc on 2/20/2016.
 */
public class Const {
    public static final String EMPLOYEE_ID = "2";
    public static final int IMAGE_UPLOAD_WIDTH = 400;
    public static final int SAMPLE_SIZE = 4;
    public static int timeSchedule = 0;
    public static ArrayList<Integer> arrayListIDNotify = new ArrayList<>();
    public static String WHC_DIRECTORY = "whc-2016";
    public static String MANAGER = "Manager";
    public static String SUPERVISOR = "Supervisor";
    public static String PRODUCT_CHECKER = "Product Checker";
    public static String FORKLIFT_DRIVER = "Forklift Driver";
    public static String NO_POSITION = "No position";
    public static String LOWER_USER = "Lower User";
    public static String TECHNICAL = "Technical";
    public static String GROUP_DOCUMENTS = "Documents";
    public static boolean isActivating;
    public static int timePauseActive = 0;
    public static AlarmManager alarmManager;
}
