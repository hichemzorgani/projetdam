package com.example.projet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.accounts.AbstractAccountAuthenticator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.BaseAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    ViewStub stubGrid;
    ViewStub stubList;
    int currentViewMode = 0;
    int VIEW_MODE_LISTVIEW = 0;
    int VIEW_MODE_GRIDVIEW = 1;
    private static final int PICK_IMAGE_REQUEST = 99;
    private Uri imagepath;
    private Bitmap imagetostore;


    Dbemploye Db;
    ListView Lv;
    GridView Gv;
    ArrayList<employe> arraylist;
    int did;
    String dname;
    emplyeadapter adapter;
    GridViewAdapter Gvadapter;
    EditText searchbar;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loadLocale();
        Db = new Dbemploye(this);
        stubList=findViewById(R.id.stub_list);
        stubGrid = findViewById(R.id.stub_grid);
        stubGrid.inflate();
        stubList.inflate();

        getSupportActionBar().setTitle(R.string.actionbarname);
        //getSupportActionBar().setIcon(R.mipmap.icon);
        getSupportActionBar().show();
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Lv = findViewById(R.id.Lv);
        Gv = findViewById(R.id.Gv);
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode",MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode",VIEW_MODE_LISTVIEW);
        switchView();
        Lv.setOnItemClickListener(onitemclick);
        Gv.setOnItemClickListener(onitemclick);


         searchbar = findViewById(R.id.searchbar);
        searchbar.addTextChangedListener(this);


    }
    AdapterView.OnItemClickListener onitemclick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, afficheremploye.class);
                intent.putExtra("POSITION", String.valueOf(position));
                startActivity(intent);


        }
    };


    private void switchView() {
        if (VIEW_MODE_LISTVIEW ==currentViewMode){
            stubList.setVisibility(View.VISIBLE);
            stubGrid.setVisibility(View.GONE);

        } else {
            stubList.setVisibility(View.GONE);
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == currentViewMode){
            showemployes();
        } else {
           gridshowemployes();
        }
    }

    private void gridshowemployes() {
        arraylist = Db.getallemploye();
        Gvadapter = new GridViewAdapter(this, arraylist);
        Gv.setAdapter(Gvadapter);
        Gvadapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbarmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.display) {
            if (VIEW_MODE_LISTVIEW == currentViewMode){
                currentViewMode = VIEW_MODE_GRIDVIEW;

            } else {
                currentViewMode = VIEW_MODE_LISTVIEW;
            }
            switchView();
            SharedPreferences sharedPreferences = getSharedPreferences("ViewMode",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("currentViewMode",currentViewMode);
            editor.commit();

        }
        if (item.getItemId() == R.id.add){
            showAddEmployeeDialog();
        }
        if (item.getItemId() == R.id.Language){
            showchangelanguagedialogue();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showchangelanguagedialogue() {
        final String[] listlanguage ={"Francais","English","العربية"};
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(MainActivity.this);
        mbuilder.setTitle(R.string.chooselanguage)
                .setSingleChoiceItems(listlanguage, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                       if (i==0){
                            setlocal("fr");
                            recreate();

                        }
                         else if (i==1){
                            setlocal("en");
                            recreate();

                        }
                       else if (i==2){
                            setlocal("ar");
                            recreate();

                        }
                       dialog.dismiss();
                    }

                });
        AlertDialog dialog = mbuilder.create();
        dialog.show();
    }

    private void setlocal(String lang) {
        Locale locale;
        if (lang.equals("fr")) {
            locale = new Locale("fr");
        } else if (lang.equals("en")) {
            locale = new Locale("en");
        } else if (lang.equals("ar")) {
            locale = new Locale("ar");
        } else {
            locale = Locale.getDefault(); // Fallback to default locale
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang","");
        setlocal(language);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(currentViewMode==0){
            this.adapter.getFilter().filter(s);
        } else {
            this.Gvadapter.getFilter().filter(s);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

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
        img = view.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseimage();
            }
        });
        imagetostore = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        img.setImageResource(R.drawable.icon);


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view)
                .setTitle(R.string.addemploye)
                .setMessage(R.string.enterinfo)
                .setIcon(R.drawable.baseline_add_24)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String identifier = editiden.getText().toString();
                        String firstname = editfirstname.getText().toString();
                        String lastname = editlastname.getText().toString();
                        String phone = editnumber.getText().toString();
                        String email = editemail.getText().toString();
                       /* if (imagetostore == null) {
                            imagetostore = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
                            img.setImageResource(R.drawable.icon);
                        }*/
                        boolean res = Db.insertemploye(identifier, firstname, lastname, phone, email,imagetostore);
                        if (res) {
                            if(currentViewMode==0){
                                adapter.notifyDataSetChanged();
                                showemployes();
                                Toast.makeText(MainActivity.this,R.string.toastajout, Toast.LENGTH_SHORT).show();

                            } else {
                                Gvadapter.notifyDataSetChanged();
                                gridshowemployes();
                                Toast.makeText(MainActivity.this, R.string.toastajout, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.toastajoutx, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.canceladd, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void choseimage() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,PICK_IMAGE_REQUEST);

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (adapter != null) {
                adapter.handleImageSelectionResult(requestCode, resultCode, data);
            }
            if (Gvadapter != null) {
                Gvadapter.handleImageSelectionResult(requestCode, resultCode, data);
            }
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
                imagepath = data.getData();
                imagetostore = MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                img.setImageBitmap(imagetostore);

            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

}
