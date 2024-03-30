package com.example.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addemploye extends AppCompatActivity {
 Dbemploye Db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addemploye);
        EditText editfirstname = this.findViewById(R.id.editfirstname);
        EditText editlastname = this.findViewById(R.id.editlastname);
        EditText editnumber = this.findViewById(R.id.editnumber);
        EditText editemail = this.findViewById(R.id.editemail);
        EditText editiden = this.findViewById(R.id.editiden);
        Button btnConfirm = this.findViewById(R.id.btnConfirm);
        Db = new Dbemploye(this);
 btnConfirm.setOnClickListener(new View.OnClickListener(){

     @Override
     public void onClick(View v) {
         String iden = editiden.getText().toString();
         String firstname = editfirstname.getText().toString();
         String lastname = editlastname.getText().toString();
         String number = editnumber.getText().toString();
         String email = editemail.getText().toString();
         employe employe = new employe(firstname,lastname,iden,number,email);
 Db.addemploye(employe);
         Toast.makeText(addemploye.this,"l'employer est bien ajouter",Toast.LENGTH_SHORT).show();

     }
 });
    }
}