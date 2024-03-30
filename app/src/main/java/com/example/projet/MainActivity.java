package com.example.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.ActionBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
 Dbemploye Db;
    ListView Lv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Lv = findViewById(R.id.Lv);
           Button btnadd = findViewById(R.id.btnadd);
        Db = new Dbemploye(this);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,addemploye.class );
                        startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<employe> list=Db.getallemploye();
        emplyeadapter adapter = new emplyeadapter(this,list);
        Lv.setAdapter(adapter);
    }
}