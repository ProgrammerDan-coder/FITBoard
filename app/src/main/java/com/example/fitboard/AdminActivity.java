package com.example.fitboard;

import static com.example.fitboard.DBHelper.KEY_ID;
import static com.example.fitboard.DBHelper.TABLE_CONTACTS;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;
    ListView list;
    Button btn_update;

    RelativeLayout relativeLayout;

    ////
    SimpleCursorAdapter userAdapter;
    ListView listView;


    EditText adminFilter;

    Cursor cursor;
    ArrayList<String> listItem;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_layout);
        Log.d(" ", "ADMIN_LAYOUT");

        relativeLayout = findViewById(R.id.admin_element);
        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();
        listItem = new ArrayList<>();
        list = findViewById(R.id.list_acc);
        adminFilter = findViewById(R.id.adminFilter);

        //viewData();

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String text = list.getItemAtPosition(position).toString();
//                Toast.makeText(AdminActivity.this, "" + text,Toast.LENGTH_LONG).show();
//                //showEditUsers(text); // для вызова редактирования
//
//            }
//        });


    }
/*
    private void showEditUsers(String text){
        ContentValues contentValues = new ContentValues(); // для добавления
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // переходим в другой Layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View edit_user = inflater.inflate(R.layout.edit_user, null); // , false)
        dialog.setView(edit_user);

        // получение значений
        Log.d("Text","text = " + text);
        EditText old_id = edit_user.findViewById(R.id.edit_id);
        EditText old_name = edit_user.findViewById(R.id.edit_name);
        EditText old_pass = edit_user.findViewById(R.id.edit_pass);
        String hashPass = null;
        btn_update = edit_user.findViewById(R.id.btn_update);
        RadioButton yes_root_user =edit_user.findViewById(R.id.yes_root);
        RadioButton no_root_user = edit_user.findViewById(R.id.no_root);

        Cursor cursor_for_start = dbHelper.select(db, text );
        cursor_for_start.moveToFirst();
        Log.d("raw", "after select");
        int r = cursor_for_start.getInt(cursor_for_start.getColumnIndexOrThrow(DBHelper.KEY_ROOT));

        //int r = 0;
        ///// ГДЕ-ТО ТУТ ПРОБЛЕМА, КАК Я ПОНЯЛ, ВЫЗВНАНАЯ В МЕТОДЕ SETTEXT !!! ОШИБКА
        Log.d("Text","cursor = " + cursor_for_start.getCount()  );


         if(cursor_for_start.moveToFirst()){

            Log.d("Text","string_id = " );
            old_id.setText(cursor_for_start.getString(cursor_for_start.getColumnIndexOrThrow("_id")));
            Log.d("Text","old_name "  );

            old_name.setText(cursor_for_start.getString(cursor_for_start.getColumnIndexOrThrow("name")));
            Log.d("Text","old_pass "  );
            hashPass = cursor_for_start.getString(cursor_for_start.getColumnIndexOrThrow("pass"));
           // old_pass.setText(cursor_for_start.getString(cursor_for_start.getColumnIndexOrThrow("pass")));

        }

        if(r == 0)
            no_root_user.setChecked(true);
        else
            yes_root_user.setChecked(true);

        cursor_for_start.close();
        // добавление кнопок
        // кнопка отмены дейстивия
        dialog.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        // кнопка удаление пользователя
        dialog.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(dbHelper.delete(db, text) == 0){
                    Snackbar.make(relativeLayout, "Произошла ошибка, попробуйте снова", BaseTransientBottomBar.LENGTH_LONG ).show();
                }
                else{
                    Cursor cursor_for_error = db.rawQuery("select " + KEY_ID +  " from " +
                            TABLE_CONTACTS +" where _id = ? ", new String[]{text});
                    cursor_for_error.moveToFirst();
                    if(cursor_for_error.getCount() == 0)
                    {
                        deleteRetrofit(Integer.parseInt(text));
                        Snackbar.make(relativeLayout, "Пользователь удален", BaseTransientBottomBar.LENGTH_LONG ).show();
                    }
                    else
                    {
                        Snackbar.make(relativeLayout, "Пользователь не может быть удален", BaseTransientBottomBar.LENGTH_LONG ).show();
                    }
                }

                viewData();
            }
        });
        // кнопка изменения пользователя
        String finalHashPass = hashPass;
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_id = old_id.getText().toString();

                String new_pass = old_pass.getText().toString();
                String new_name = old_name.getText().toString();
                String bcryptPassword = BCrypt.withDefaults().hashToString(15, new_pass.toCharArray());
                int access_root = 0;
                boolean retrofit_acess;
                if(no_root_user.isChecked()){
                    access_root = 0;
                    retrofit_acess = false;
                }
                else{
                    access_root = 1;
                    retrofit_acess = true;
                }

                if(old_pass.getText().length() < 6){
                    if(dbHelper.update(db, new_id, new_name, finalHashPass, access_root) == 0){
                        Snackbar.make(relativeLayout,"Произошла ошибка", Snackbar.LENGTH_SHORT).show();
                    }
                    else{
                        Object[] obj = new Object[2];
                        obj[0] = Integer.parseInt(text);
                        obj[1] = retrofit_acess;
                        Log.d("else", "before");
                        MyThread myThread = new MyThread();
                        myThread.execute(obj);
                        Snackbar.make(relativeLayout,"Пользователь обнавлен", Snackbar.LENGTH_SHORT).show();
                    }

                    viewData();
                    return;
                }
               else{
//                    if(dbHelper.update(db, new_id, new_name, bcryptPassword, access_root) == 0){
//                        Snackbar.make(relativeLayout,"Произошла ошибка", Snackbar.LENGTH_SHORT).show();
//                    }
//                    else{
//
//                       updateRetrofit(Integer.parseInt(text), retrofit_acess);
//                        // Определяем объект Thread - новый поток
//
//                        Snackbar.make(relativeLayout,"Пользователь обнавлен", Snackbar.LENGTH_SHORT).show();
//                    }


                    Object[] obj = new Object[2];
                    obj[0] = Integer.parseInt(text);
                    obj[1] = retrofit_acess;
                    Log.d("else", "before");
                    MyThread myThread = new MyThread();
                    myThread.execute(obj);
                    viewData();
                    return;
                }


            }
        });

        dialog.show();

    }
*/
/////////////////class

    class MyThread extends AsyncTask<Object, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Object... obj) {
            Update loginRequest = new Update();

            int _id = (Integer)obj[0];
            boolean access = (boolean)obj[1];
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
                    Log.d("RETROFIT", "ERROR_UPDATE");
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        protected void onPostExecute(int _id, boolean access) {
            //super.onPostExecute(_id, access);
            Log.d("onPostExecute", "start");

        }
    }



    public void updateRetrofit(int _id, boolean access){
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

    public void deleteRetrofit(int _id){
        DeleteUser loginRequest = new DeleteUser();

        loginRequest.setId(_id);


        Call<DeleteUser> loginResponseCall = ApiClient.getIRetrofit().delete(loginRequest);
        loginResponseCall.enqueue(new Callback<DeleteUser>() {
            @Override
            public void onResponse(Call<DeleteUser> call, Response<DeleteUser> response) {
                if(response.isSuccessful()){
                    Log.d("RETROFIT", "Update");
                }
                else
                    Log.d("RETROFIT", "no Update");
            }

            @Override
            public void onFailure(Call<DeleteUser> call, Throwable t) {
                Log.d("RETROFIT", "ERROR");
            }
        });


    }

    @Override
    public void onResume() {

        super.onResume();

    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        // public void viewData() {
//
//        // вывод всей информации о пользоваетел
//
//        cursor = db.rawQuery("select * from " + TABLE_CONTACTS, null);
//
//
//        String [] header = new String[] {DBHelper.KEY_NAME, KEY_ID, DBHelper.KEY_PASS, DBHelper.KEY_ROOT}; //  DBHelper.KEY_TIME, DBHelper.KEY_DATE, DBHelper.KEY_DESC
//        int[] to = new int[] {R.id.ViewNameUser, R.id.ViewId, R.id.ViewPass, R.id.ViewRoot};
//
//        userAdapter = new SimpleCursorAdapter(this, R.layout.item_admin ,cursor, header, to, 0);
//
//        list.setAdapter(userAdapter);
//
//        //////////////////
//
//        if(!adminFilter.getText().toString().isEmpty())
//            userAdapter.getFilter().filter(adminFilter.getText().toString());
//
//
//        adminFilter.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) { }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//            // при изменении текста выполняем фильтрацию
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                userAdapter.getFilter().filter(s.toString());
//            }
//        });
//
//        try {
//            // устанавливаем провайдер фильтрации
//            userAdapter.setFilterQueryProvider(new FilterQueryProvider() {
//                @Override
//                public Cursor runQuery(CharSequence constraint) {
//
//                    if (constraint == null || constraint.length() == 0) {
//
//                        return db.rawQuery("select * from " + TABLE_CONTACTS, null);
//                    }
//                    else {
//                        return db.rawQuery("select * from " + TABLE_CONTACTS + " where " +
//                                DBHelper.KEY_NAME + " like ? or " + KEY_ID + " like ? or " + DBHelper.KEY_ROOT +
//                                " like ?", new String[]{"%" + constraint.toString() + "%", "%" + constraint.toString() + "%", "%" + constraint.toString() + "%"});
//                    }
//                }
//            });
//
//            list.setAdapter(userAdapter);
//        }
//        catch (SQLException ex){
//
//        }
//
//        ////
//
//
//
//        // вывод только id
//        listItem.clear();
//        cursor = dbHelper.viewData();
//        while (cursor.moveToNext()){
//            listItem.add(cursor.getString(0));
//        }
//
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
//        list.setAdapter(adapter);
//
//
//
//
//
//    }
//    //////////////////
//

    private void viewData() {
        listItem.clear();
        cursor = dbHelper.viewData();
        while (cursor.moveToNext()){
            listItem.add(cursor.getString(0));
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItem);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart_admin:

                    //Intent intent = new Intent(this, CreateEvent.class);
                    Intent intent = new Intent(AdminActivity.this, AdminEvent.class);


                    startActivity(intent);

                    //startActivity(intent);
                    return true;


            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        ///


        return true;
    }




    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

}
