package com.example.projet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class afficheremploye extends AppCompatActivity {
    String email ;
    String phone;
    Button contact;
    private static final String PREF_THEME_KEY = "theme_key";
    private static final int THEME_DEFAULT = 0;
    private static final int THEME_HOSPITAL = 1;
    private static final int THEME_UNIVERSITY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_afficheremploye);
        TextView d_firstname = findViewById(R.id.firstname);
        TextView d_lastname = findViewById(R.id.lastname);
        TextView d_identifier = findViewById(R.id.identifier);
        TextView d_email = findViewById(R.id.email);
        TextView d_phone = findViewById(R.id.phone);
        ImageView image = findViewById(R.id.imageView);
        Dbemploye Db = new Dbemploye(this);
        ArrayList<employe> arrayList = Db.getallemploye();
        Intent intent =getIntent();
        String pos = intent.getStringExtra("POSITION");
        int position = Integer.parseInt(pos);
        employe employe = arrayList.get(position);
        String firstname = employe.getFirstname();
        String lastname = employe.getLastname();
        String identifier = employe.getIden();
         email = employe.getEmail();
         phone = employe.getNumber();
        Bitmap bitmap = employe.getEmployeimage();
        image.setImageBitmap(bitmap);
        d_firstname.setText(firstname);
        d_lastname.setText(lastname);
        d_identifier.setText(identifier);
        d_phone.setText(phone);
        d_email.setText(email);
        getSupportActionBar().setTitle(R.string.title);



        contact = findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              contactdialog();
            }
        });

    }

    private void setAppTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = preferences.getInt(PREF_THEME_KEY, THEME_DEFAULT);
        switch (theme) {
            case THEME_DEFAULT:
                setTheme(R.style.AppTheme);
                break;
            case THEME_HOSPITAL:
                setTheme(R.style.HospitalTheme);
                break;
            case THEME_UNIVERSITY:
                setTheme(R.style.UniversityTheme);
                break;
        }
    }

    private int getCurrentTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt(PREF_THEME_KEY, THEME_DEFAULT);
    }

    private void contactdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choosecontact);


        builder.setPositiveButton(R.string.byphone, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                phone.toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ Uri.encode(phone)));
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.byemail, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                email.toString();
                sendemail(email);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void sendemail(String email){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"choose email client:"));

    }

}