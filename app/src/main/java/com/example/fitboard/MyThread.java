package com.example.fitboard;

import android.os.AsyncTask;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract class MyThread extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected void doInBackground(int _id, boolean access) {
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

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}