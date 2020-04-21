package com.pihta24.dolgi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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

    class MyGetDatabaseBody{
        public String id_token;
    }

    class MyGetOneBody{
        public String id_token;
        public int id;
    }

    class MyAddToDatabaseBody{
        public String id_token;
        public MyDatabaseNode data;

        public static MyAddToDatabaseBody create(String id_token, MyDatabaseNode data){
            MyAddToDatabaseBody body = new MyAddToDatabaseBody();
            body.id_token = id_token;
            body.data = data;
            return body;
        }
    }

    class MyResponse{
        public String response;
    }

    class MyDatabaseResponse{
        String response;
        public List<MyDatabaseNode> data;
    }

    class MyDatabaseNode{
        public int id = 0;
        public String name;
        public String lastname;
        public String curr;
        public double debt;
        public String type;

        public static MyDatabaseNode create(int id, String name, String lastname, String curr, double debt, String type){
            MyDatabaseNode node = new MyDatabaseNode();
            node.id = id;
            node.name = name;
            node.lastname = lastname;
            node.curr = curr;
            node.debt = debt;
            node.type = type;
            return node;
        }
    }

    @POST("register_user")
    Call<MyResponse> register_user(@Body MyRegisterUserBody myRegisterUserBody);

    @POST("get_access_token")
    Call<MyResponse> get_access_token(@Body MyGetTokenBody myGetTokenBody);

    @POST("get_database")
    Call<MyDatabaseResponse> get_database(@Body MyGetDatabaseBody myGetDatabaseBody);

    @POST("get_one")
    Call<MyDatabaseResponse> get_one(@Body MyGetOneBody myGetOneBody);

    @POST("add_to_database")
    Call<MyResponse> add_to_database(@Body MyAddToDatabaseBody myAddToDatabaseBody);

    @POST("update_database")
    Call<MyResponse> update_database(@Body MyAddToDatabaseBody myAddToDatabaseBody);

    @POST("delete_from_database")
    Call<MyDatabaseResponse> delete_from_database(@Body MyGetOneBody myGetOneBody);

    @POST("check_user")
    Call<MyResponse> check_user(@Body MyGetTokenBody myGetTokenBody);
}
