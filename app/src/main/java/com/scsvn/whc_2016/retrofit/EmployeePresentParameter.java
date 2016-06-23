package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/27/2016.
 */
public class EmployeePresentParameter {
    private String position;
    private int Department = 1;

    public EmployeePresentParameter(int department, String position) {
        Department = department;
        this.position = position;
    }
}
