package com.example.fitboard;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;
import static com.example.fitboard.DBHelper.KEY_ID;
import static com.example.fitboard.DBHelper.KEY_NAME;
import static com.example.fitboard.DBHelper.KEY_PASS;
import static com.example.fitboard.DBHelper.TABLE_CONTACTS;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity
{

    private ArrayAdapter<User> adapter; // JSON

    private List<User> users; // JSON

    Button btnSignIn, btnRegister;

    // данные для админа
    String admin_id = "0";
    String admin_pass = "admin0";


    RelativeLayout root;

    DBHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        root = findViewById(R.id.root_element);

        users = new ArrayList<>(); // JSON

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users); // JSON

        //dbHelper = new DBHelper(this);
        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();
        /// возможно еще что то связаное с БД
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInWindow();
            }
        });

        
    }


    public void OnCLick(View view){

        Intent intent = new Intent(MainActivity.this, NewsActivity.class);
        // intent.putExtra("name", admin_name);
        //startActivity(intent);
        startActivity(intent);
        finish();



    }

    private void showSignInWindow(){
       // ContentValues contentValues = new ContentValues(); // для добавления
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите данные для входа");
        // переходим в другой Layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_in_window);
        // получение значений
        EditText id = sign_in_window.findViewById(R.id.idTicket);
        MaterialEditText pass = sign_in_window.findViewById(R.id.passField);
        // добавление кнопок
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @SuppressLint("Range")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(id.getText().toString())  ){
                    Snackbar.make(root, "Введите ваш номер студенческого билета", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if(pass.getText().toString().length() < 6){
                    Snackbar.make(root, "Введите пароль длинною более 6 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // авторизация, проверка на корректную инфу
                // переводим id и name

                Log.d("WTF", "START " );
                String id_string = id.getText().toString();
                int id_int = Integer.parseInt(id_string);
                String pass_string = pass.getText().toString();
                if(id_string.equals(admin_id)  && pass_string.equals(admin_pass)) {
                    Log.d("Admin", "wadwda" );
                    // для передачи данных Name
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                   // intent.putExtra("name", admin_name);
                    //startActivity(intent);
                    startActivity(intent);
                    finish();

                }

                //bcrypt
                String bcryptPassword = BCrypt.withDefaults().hashToString(15, pass_string.toCharArray());

                //endBcrypt
                Log.d("WTF", "id: " + id_string + "\npass: " + bcryptPassword );


                Cursor cursor = db.rawQuery("select " + KEY_ID + ", " + KEY_NAME + ", " + KEY_PASS + " from " +
                        TABLE_CONTACTS +" where _id = ?", new String[]{id_string});
                Log.d("Cursor","rawQuery");
                if(cursor.getCount() == 0){
                    Log.d("Cursor","NULL");
                    Snackbar.make(root, "Не корректные данные", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                String hashPass;
                cursor.moveToFirst();
                Log.d("Cursor","first");
                hashPass = cursor.getString(cursor.getColumnIndexOrThrow("pass")); //BCRYPT

                int id_column = cursor.getInt( cursor.getColumnIndex(KEY_ID) );
                //String pass_column = cursor.getString(cursor.getColumnIndex(KEY_PASS));
                Log.d("WTF", "ID_column = " + id_column);
                String name_user;
                cursor.moveToFirst();
                name_user = cursor.getString(cursor.getColumnIndex("name"));
                cursor.close();
                BCrypt.Result result = BCrypt.verifyer().verify(pass_string.toCharArray(), hashPass); //BCRYPT
                Log.d("BCRYPT", "RESULT = " + result.verified);//
                Log.d("BCRYPT", "hash = " + result.verified);//
                if(result.verified)
                {
                    Log.d("Intent", "name_user = " + name_user);
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    intent.putExtra("name", name_user);
                    startActivity(intent);
                    finish();
                }
                else{
                    Snackbar.make(root, "Не корректные данные", Snackbar.LENGTH_SHORT).show();
                    return;
                }



                //
            }
        });

        dialog.show();
    }


    private void showRegisterWindow() {

        ContentValues contentValues = new ContentValues(); // для добавления
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироваться");
        dialog.setMessage("Введите все данные для регистрации");
        // переходим в другой Layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);
        // получение значений
        EditText id = register_window.findViewById(R.id.idTicket);
        MaterialEditText name = register_window.findViewById(R.id.nameField);
        MaterialEditText pass = register_window.findViewById(R.id.passField);
        // добавление кнопок
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(id.getText().toString())){
                    Snackbar.make(root, "Введите ваш номер студенческого билета", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                else if(id.getText().toString() == admin_id){
                    Snackbar.make(root, "Этот id занят админом", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(name.getText().toString())){
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(pass.getText().toString().length() < 6){
                    Snackbar.make(root, "Введите пароль длинною более 6 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // Регистрация пользователя
                //

                String id_string = id.getText().toString();
                int id_int = Integer.parseInt(id_string);
                String name_string = name.getText().toString();
                String pass_string = pass.getText().toString();
                Cursor cursor = db.rawQuery("select " + KEY_ID +  " from " +
                        TABLE_CONTACTS +" where _id = ? ", new String[]{id_string});
                if(cursor.getCount() != 0){
                    Snackbar.make(root, "Такой номер студента уже есть в базе", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Log.d("WTf", "save_method");
                save(name_string, id_int); // JSON




               // db.insert(id_string, name_string, pass.getText().toString());
                //bcrypt
                    String bcryptPassword = BCrypt.withDefaults().hashToString(15, pass_string.toCharArray());
                //




                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_ID, id_int);
                contentValues.put(DBHelper.KEY_PASS, bcryptPassword); // pass.getText().toString()
                contentValues.put(DBHelper.KEY_NAME, name.getText().toString());
                db.insert(TABLE_CONTACTS,null, contentValues);


//                contentValues.put(KEY_ID, id_int);
//                contentValues.put(DBHelper.KEY_NAME, name.getText().toString()); //
//                contentValues.put(DBHelper.KEY_PASS, pass.getText().toString()); //
//                contentValues.put(DBHelper.KEY_ROOT, 0); //
//                Uri uri = getContentResolver().insert(MyContentProvider.CONTENT_URI,contentValues);
//                Log.d(LOG_TAG, "insert, result Uri : " + uri.toString());
//               // db.insert(DBHelper.TABLE_CONTACTS, KEY_ID, contentValues); // добавление в БД
//                Log.d(LOG_TAG, "insert, result Uri : " + uri.toString());


                 //ContentProvider



//                Uri uri = getContentResolver().insert(MyContentProvider.CONTENT_URI, contentValues1);
                //Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();





                Snackbar.make(root,"Пользователь добавлен", Snackbar.LENGTH_SHORT).show();

                //
            }
        });

        dialog.show();
    }
 // JSON



    public void save(String _name, int _id){




        User user = new User(_name, _id);

        users.add(user);
       adapter.notifyDataSetChanged();

        boolean result = JSONHelper.exportToJSON(this, users);
        if(result){
            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Не удалось сохранить данные", Toast.LENGTH_LONG).show();
        }
        
    }

    // !JSON
}