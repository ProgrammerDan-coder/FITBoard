package com.example.fitboard;

import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;



public interface IRetrofit {
    @POST("api/auth/registration")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("api/auth/login")
    Call<LoginResponseForLogin> Login (@Body LoginResponseForLogin login);

    @POST("api/delete")
    Call<DeleteUser> delete(@Body DeleteUser del);
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