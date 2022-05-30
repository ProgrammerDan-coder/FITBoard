package com.example.fitboard;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface IRetrofit {
    @POST("api/auth/registration")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("api/auth/login")
    Call<LoginResponseForLogin> Login (@Body LoginResponseForLogin login);

    @POST("api/delete")
    Call<DeleteUser> delete(@Body DeleteUser del);

    @PUT("api/update")
    Call<Update> update(@Body Update upd);

    @GET("api/user/all")
    Call<List<Post>> allUsers();
}

class Post{
    private int id;
    private String password;
    private String name;
    private boolean root;

    @SerializedName("body")
    private String text;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
}


class LoginRequest {
    private int id;
    private String password;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class LoginResponseForLogin{
    private int id;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class DeleteUser{
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class Update {
    private int id;
    private boolean root;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public void class_update_retrofit(int _id, boolean access){
        Update loginRequest = new Update();

        loginRequest.setId(_id);
        loginRequest.setRoot(access);

        Call<Update> loginResponseCall = ApiClient.getIRetrofit().update(loginRequest);
        loginResponseCall.enqueue(new Callback<Update>() {
            @Override
            public void onResponse(Call<Update> call, Response<Update> response) {
                if(response.isSuccessful()){
                    Log.d("RETROFIT", "Delete");
                }
                else
                    Log.d("RETROFIT", "no Delete");
            }

            @Override
            public void onFailure(Call<Update> call, Throwable t) {
                Log.d("RETROFIT", "ERROR");
            }
        });

    }
}