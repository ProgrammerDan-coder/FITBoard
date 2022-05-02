package com.example.fitboard;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.SplittableRandom;

public class UserEntry extends AppCompatActivity {
    String NAME_USER;
    Cursor cursor;
    int id_user;
    SimpleCursorAdapter userAdapter;
    ArrayList<String> listItem;
    ArrayList<String> name_event = new ArrayList<String>();
    ArrayList<String> selectedUsers = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    ListView listView;
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_entry);
        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getReadableDatabase();
        Bundle arguments = getIntent().getExtras();
        NAME_USER = arguments.get("name").toString();
        listView = findViewById(R.id.listEntryUser);
        listItem = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + DBHelper.TABLE_CONTACTS + " where name = ?", new String[]{NAME_USER});
        cursor.moveToFirst();
        id_user = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.KEY_ID));
    }



    @Override
    protected void onResume() {
        super.onResume();

        String str_id_user = String.valueOf(id_user);
        cursor = db.rawQuery("select _id_event from " + DBHelper.TABLE_ENTRY + " where _id_user = ?", new String[]{str_id_user});
        Log.d("Cursor", "= " + str_id_user);
        cursor.moveToFirst();
        String id_event, nameEventId;
        Cursor cursor_event;
//        String [] header = new String[] {DBHelper.KEY_ID , DBHelper.KEY_NAME,
//                DBHelper.KEY_PLACE, DBHelper.KEY_TIME, DBHelper.KEY_DATE, DBHelper.KEY_DESC, DBHelper.KEY_AUTH_EVENT}; //  DBHelper.KEY_TIME, DBHelper.KEY_DATE, DBHelper.KEY_DESC
//
//       int[] to = new int[] {R.id.ViewId ,R.id.ViewName, R.id.ViewPlace, R.id.ViewTime,
//                R.id.ViewDate, R.id.ViewDesc, R.id.ViewAuth};

//        String [] header = new String[] { DBHelper.KEY_NAME}; //  DBHelper.KEY_TIME, DBHelper.KEY_DATE, DBHelper.KEY_DESC
//
//        int[] to = new int[] {R.id.ViewNameEntry};

        /////Пытаюсь использовать адаптер
        Collections.addAll(name_event);
        adapter = new ArrayAdapter(this, R.layout.item_entry, R.id.ViewNameEntry, name_event);
        Log.d("While", "ERROR" );
        while (cursor.isAfterLast() == false){
            id_event = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.KEY_EVENT_ENTRY));
            Log.d("While", "id_event = " + id_event );
            cursor_event = db.rawQuery("select name from " + DBHelper.TABLE_EVENT + " where _id = ?", new String[]{id_event});
            Log.d("While", "cursor_event = " + cursor_event.getCount() );
            ///
            cursor_event.moveToFirst();
            Log.d("nameEventdId", "FUCK"  );
            nameEventId = cursor_event.getString(cursor_event.getColumnIndexOrThrow(DBHelper.KEY_NAME));
            Log.d("nameEventdId", "FUCK2"  );
            adapter.add(nameEventId);
            adapter.notifyDataSetChanged();

            ////////

            //userAdapter = new SimpleCursorAdapter(this, R.layout.item_entry ,cursor_event, header, to, 0);

            cursor.moveToNext();
        }


        listView.setAdapter(adapter);

//


    }

    public void onBackPressed() {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("name", NAME_USER);
        startActivity(intent);
        finish();
    }

}
