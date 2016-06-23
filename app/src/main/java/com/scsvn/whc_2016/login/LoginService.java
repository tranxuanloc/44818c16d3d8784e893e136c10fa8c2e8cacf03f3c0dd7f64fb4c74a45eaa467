package com.scsvn.whc_2016.login;

import retrofit.http.GET;
import retrofit.http.Query;


public interface LoginService {
    @GET("api/values")
    LoginResult getvalues(@Query("id") String id);

}
