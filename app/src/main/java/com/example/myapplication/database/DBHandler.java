package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.model.Result;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "tokendb";
    private static final String TABLE_NAME = "tokendb";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String TOKEN = "token";
    private static final String COLUMN_ID = "id";

    public DBHandler(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INT,"
                + REFRESH_TOKEN + " TEXT,"
                + TOKEN + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    public void addNewToken(String refreshToken, String token){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, 1);
        values.put(REFRESH_TOKEN, refreshToken);
        values.put(TOKEN, token);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    //1 - refresh token ////2 - token
    public ArrayList<String> readDB(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<String> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(1));
                list.add(cursor.getString(2));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void update(String refreshToken, String token){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, 1);
        values.put(REFRESH_TOKEN, refreshToken);
        values.put(TOKEN, token);

        db.update(TABLE_NAME, values, "id=?", new String[]{"1"});
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
