package com.pihta24.dolgi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyAPI {

    class MyRegisterUserBody{
        public String nick;
        public String email;
        public String name;
        public String last_name;
        public String password;
    }

    class MyGetTokenBody{
        public String email_nick;
        public String password;
    }

    class MyResponse{
        public String response;
    }

    @POST("register_user")
    Call<MyResponse> register_user(@Body MyRegisterUserBody myRegisterUserBody);

    @POST("get_access_token")
    Call<MyResponse> get_access_token(@Body MyGetTokenBody myGetTokenBody);
}
