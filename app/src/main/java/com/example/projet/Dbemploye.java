package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Dbemploye extends SQLiteOpenHelper {
    public Dbemploye(@Nullable Context context) {
        super(context, "project_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table employe(id integer primary key,identifier integer,firstname varchar(30),lastname varchar(30),phone integer, email varchar(30))";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addemploye(employe employe) {
        SQLiteDatabase Db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("identifier", employe.getIden());
        values.put("firstname", employe.getFirstname());
        values.put("lastname", employe.getLastname());
        values.put("phone", employe.getNumber());
        values.put("email", employe.getEmail());

        Db.insert("employe", null, values);
    }

    public ArrayList<employe> getallemploye() {
        ArrayList<employe> employes = new ArrayList<>();
        String selectall = "select * from employe";
        SQLiteDatabase Db = this.getReadableDatabase();
        Cursor cursor = Db.rawQuery(selectall, null);
        if (cursor.moveToFirst()) {
            do {
                String Number = cursor.getString(cursor.getColumnIndex("phone"));
                String Firstname = cursor.getString(cursor.getColumnIndex("firstname"));
                String Lastname = cursor.getString(cursor.getColumnIndex("lastname"));
                String identifier = cursor.getString(cursor.getColumnIndex("identifier"));
                String Email = cursor.getString(cursor.getColumnIndex("email"));
                employe employe = new employe(Firstname, Lastname, identifier, Number, Email);
                employes.add(employe);
            } while (cursor.moveToNext());
        }
        return employes;
    }
}