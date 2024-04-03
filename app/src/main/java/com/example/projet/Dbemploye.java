package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Dbemploye extends SQLiteOpenHelper {
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageinbyte;
    public Dbemploye(@Nullable Context context) {
        super(context, "project_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table employe(id integer primary key autoincrement,identifier varchar(30),firstname varchar(30),lastname varchar(30),phone varchar(30), email varchar(30),image blob)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertemploye(String identifier, String firstname, String lastname, String phone, String email, Bitmap image) {
        SQLiteDatabase Db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("identifier", identifier);
        values.put("firstname", firstname);
        values.put("lastname", lastname);
        values.put("phone", phone);
        values.put("email", email);
        byte[] imageBytes = getBytes(image);
        values.put("image", imageBytes);
        long res = Db.insert("employe", null, values);
        if (res==-1){
            return false;
        } else {
            return true;
        }
    }

    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public ArrayList<employe> getallemploye() {
        ArrayList<employe> employes = new ArrayList<>();
        String selectall = "select * from employe";
        SQLiteDatabase Db = this.getReadableDatabase();
        Cursor cursor = Db.rawQuery(selectall, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String Number = cursor.getString(cursor.getColumnIndex("phone"));
                String Firstname = cursor.getString(cursor.getColumnIndex("firstname"));
                String Lastname = cursor.getString(cursor.getColumnIndex("lastname"));
                String identifier = cursor.getString(cursor.getColumnIndex("identifier"));
                String Email = cursor.getString(cursor.getColumnIndex("email"));
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap imageBitmap = null;
                if (imageBytes != null) {
                    imageBitmap = getImage(imageBytes);
                }
                employe employe = new employe(id,Firstname, Lastname, identifier, Number, Email,imageBitmap);
                employes.add(employe);
            } while (cursor.moveToNext());
        }
        return employes;
    }

    private Bitmap getImage(byte[] imageBytes) {
        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return null;
    }

    public int updateemploye(String id,String identifier,String firstname,String lastname,String email,String phone) {
        SQLiteDatabase Db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("identifier", identifier);
        values.put("firstname", firstname);
        values.put("lastname", lastname);
        values.put("phone", phone);
        values.put("email", email);

      return Db.update("employe", values, "id=?", new String[]{id});


    }
    public int deleteemploye(String id){
        SQLiteDatabase Db =this.getWritableDatabase();
         return Db.delete("employe","id=?", new String[]{id});
    }
}