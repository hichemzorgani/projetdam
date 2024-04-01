package com.example.projet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.accounts.AbstractAccountAuthenticator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Dbemploye Db;
    ListView Lv;
    ArrayList<employe> arraylist;
    int did;
    String dname;
    emplyeadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Db = new Dbemploye(this);
        Lv = findViewById(R.id.Lv);
        showemployes();
        Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, afficheremploye.class);
                intent.putExtra("POSITION", String.valueOf(position));
                startActivity(intent);
            }
        });
        //button ajouter
        Button btnadd = findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEmployeeDialog();
            }
        });
    }

    private void showemployes() {
        arraylist = Db.getallemploye();
        adapter = new emplyeadapter(this, arraylist);
        Lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showAddEmployeeDialog() {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View view = inflater.inflate(R.layout.activity_addemploye, null);
        EditText editfirstname = view.findViewById(R.id.editfirstname);
        EditText editlastname = view.findViewById(R.id.editlastname);
        EditText editnumber = view.findViewById(R.id.editnumber);
        EditText editemail = view.findViewById(R.id.editemail);
        EditText editiden = view.findViewById(R.id.editiden);
        ImageView img = view.findViewById(R.id.img);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view)
                .setTitle("Ajouter un employer")
                .setMessage("entrer les informations")
                .setIcon(R.drawable.icon)
                .setPositiveButton("ajouter un employer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String identifier = editiden.getText().toString();
                        String firstname = editfirstname.getText().toString();
                        String lastname = editlastname.getText().toString();
                        String phone = editnumber.getText().toString();
                        String email = editemail.getText().toString();
                        boolean res = Db.insertemploye(identifier, firstname, lastname, phone, email);
                        if (res) {
                            showemployes();
                            Toast.makeText(MainActivity.this, "Nouvel employé ajouté", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Échec de l'ajout de l'employé", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
