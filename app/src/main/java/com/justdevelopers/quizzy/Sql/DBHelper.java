package com.justdevelopers.quizzy.Sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context ) {
        super(context,"UserData",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table UserDetails(userID TEXT primary key,name TEXT,password PASSWORD,number NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists UserDetails");
    }
    public Boolean insetUserData(String name,String number,String email,String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userID",email);
        contentValues.put("name",name);
        contentValues.put("password",password);
        contentValues.put("number",number);
        long result= DB.insert("UserDetails",null,contentValues);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }
    public ArrayList<String> getData(String email, String pass){
        SQLiteDatabase DB = this.getWritableDatabase();
        String[] args = { email, pass };
        Cursor cursor = DB.rawQuery("Select * from Userdetails where userID = ? and password = ? ",args);
        ArrayList<String> names
                = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                names.add(cursor.getString(1));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return names;
    }
}