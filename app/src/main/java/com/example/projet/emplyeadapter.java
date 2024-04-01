package com.example.projet;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class emplyeadapter extends BaseAdapter {
    ArrayList<employe> employes;
    Context context;
    int did;
    String dname;
    Dbemploye Db;
    employe employe;

    public emplyeadapter(Context context, ArrayList<employe> employes) {
        this.context = context;
        this.employes = employes;
        Db = new Dbemploye(context);
    }

    @Override
    public int getCount() {
        return employes.size(); // Return the size of the employes list
    }

    @Override
    public Object getItem(int position) {
        return employes.get(position); // Return the employe at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position; // Return the position as the ID of the item
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        employe emp = employes.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.employe, parent, false);
        }

        TextView firstname = convertView.findViewById(R.id.firstname);
        TextView lastname = convertView.findViewById(R.id.lastname);
        TextView iden = convertView.findViewById(R.id.iden);
        TextView number = convertView.findViewById(R.id.number);
        TextView email = convertView.findViewById(R.id.email);
        Button btn = convertView.findViewById(R.id.btn);

        firstname.setText(emp.firstname);
        lastname.setText(emp.lastname);
        iden.setText(emp.iden);
        number.setText(emp.number);
        email.setText(emp.email);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showPopupMenu(v, position);
            }
        });

        return convertView;
    }

   public void showPopupMenu(View view, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        employe = employes.get(position);
        did = Integer.parseInt(employe.getIden());
        dname = employe.getFirstname();


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.activity_updateemploye, null);
                    EditText editfirstname = view.findViewById(R.id.editfirstname);
                    EditText editlastname = view.findViewById(R.id.editlastname);
                    EditText editnumber = view.findViewById(R.id.editnumber);
                    EditText editemail = view.findViewById(R.id.editemail);
                    EditText editiden = view.findViewById(R.id.editiden);
                    ImageView img = view.findViewById(R.id.img);

                    String oldfirstname = employe.getFirstname();
                    String oldlastname = employe.getLastname();
                    String oldnumber = employe.getNumber();
                    String oldemail = employe.getEmail();
                    String oldiden = employe.getIden();

                    editfirstname.setText(oldfirstname);
                    editlastname.setText(oldlastname);
                    editemail.setText(oldemail);
                    editiden.setText(oldiden);
                    editiden.setText(oldiden);


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(view)
                            .setTitle("Modifier un employer")
                            .setMessage("entrer les informations")
                            .setIcon(R.drawable.icon)
                            .setPositiveButton("modifier un employer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String identifier = editiden.getText().toString();
                                    String firstname = editfirstname.getText().toString();
                                    String lastname = editlastname.getText().toString();
                                    String phone = editnumber.getText().toString();
                                    String email = editemail.getText().toString();
                                    boolean res = Db.updateemploye(String.valueOf(did), identifier, firstname, lastname, phone, email);
                                    if (res) {
                                        Toast.makeText(context, "modifier", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged(); // Mettre à jour l'affichage de la liste
                                    } else {
                                        Toast.makeText(context, "Échec de la modification de l'employé", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss(); // Fermer le dialogue après la modification
                                }
                            })
                            .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();

                } else if (item.getItemId() == R.id.delete) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Suppression")
                            .setMessage("Tu es sur de supprimer " + dname)
                            .setPositiveButton("Supprimer ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Db.deleteemploye(String.valueOf(did));
                                    employes.remove(position);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing or handle cancellation
                                }
                            });
                    builder.create().show();
                } else {

                }
                return true;
            }
        });

        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
    }

}
