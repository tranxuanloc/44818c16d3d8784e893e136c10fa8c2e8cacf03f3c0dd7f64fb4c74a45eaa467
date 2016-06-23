package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 2/22/2016.
 */
public class ChangePasswordParameter {
    private String userName;
    private String password;
    private String newPassword;

    public ChangePasswordParameter(String userName, String password, String newPassword) {
        this.newPassword = newPassword;
        this.password = password;
        this.userName = userName;
    }
}
