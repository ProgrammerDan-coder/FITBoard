package com.example.fitboard;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

//public interface IRetrofit {
//    @FormUrlEncoded
//    @POST("api/auth/registration")
//    Call<IRetrofit>  createUser(@Body RegistrationBody registrationBody);//createUser(@Field("id") int id, @Field("name") String name, @Field("password") String password); //createUser(@Body RegistrationBody registrationBody);
//}
//
//     class RegistrationBody{
//    public int _id;
//    public String password;
//    public String name;
//
//    RegistrationBody(int id, String pass, String name){
//        this._id = id;
//        this.name = name;
//        this.password = pass;
//    }
//}
//

public interface IRetrofit {
    @POST("api/auth/registration")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("api/auth/login")
    Call<LoginResponseForLogin> Login (@Body LoginResponseForLogin login);
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