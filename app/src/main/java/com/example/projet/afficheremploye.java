package com.example.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class afficheremploye extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficheremploye);
        TextView d_firstname = findViewById(R.id.firstname);
        TextView d_lastname = findViewById(R.id.lastname);
        TextView d_identifier = findViewById(R.id.identifier);
        TextView d_email = findViewById(R.id.email);
        TextView d_phone = findViewById(R.id.phone);
        Dbemploye Db = new Dbemploye(this);
        ArrayList<employe> arrayList = Db.getallemploye();
        Intent intent =getIntent();
        String pos = intent.getStringExtra("POSITION");
        int position = Integer.parseInt(pos);
        employe employe = arrayList.get(position);
        String firstname = employe.getFirstname();
        String lastname = employe.getLastname();
        String identifier = employe.getIden();
        String email = employe.getEmail();
        String phone = employe.getNumber();
        d_firstname.setText(firstname);
        d_lastname.setText(lastname);
        d_identifier.setText(identifier);
        d_phone.setText(phone);
        d_email.setText(email);

    }

}