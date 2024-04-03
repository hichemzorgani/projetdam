package com.example.projet;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Locale;

public class emplyeadapter extends BaseAdapter implements Filterable {
    ArrayList<employe> employes,tempemployes;
    Customfilter cs;

    Context context;

    int did;
    String dname;
    Dbemploye Db;
    employe employe;

    public emplyeadapter(Context context, ArrayList<employe> employes) {
        this.context = context;
        this.employes = employes;
        this.tempemployes=employes;
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

        Button btn = convertView.findViewById(R.id.btn);

        firstname.setText(emp.firstname);
        lastname.setText(emp.lastname);
        iden.setText(emp.iden);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showPopupMenu(v, position);
            }
        });

        return convertView;
    }

   public void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        employe = employes.get(position);
        did = employe.getId();
        dname = employe.getFirstname();


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.edit) {
                     Modificationbuilder(position);

                } else if (item.getItemId() == R.id.delete) {
                    Suppressionbuilder(position);
                }
                return true;
            }
        });

        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
    }
    public  void Modificationbuilder(int position){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_updateemploye, null);
        EditText editfirstname = view.findViewById(R.id.editf);
        EditText editlastname = view.findViewById(R.id.editl);
        EditText editnumber = view.findViewById(R.id.editn);
        EditText editemail = view.findViewById(R.id.edite);
        EditText editiden = view.findViewById(R.id.editi);
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
        editnumber.setText(oldnumber);


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
                        int res = Db.updateemploye(String.valueOf(did), identifier, firstname, lastname, phone, email);
                        if (res>0) {
                            employe.setFirstname(firstname);
                            employe.setLastname(lastname);
                            employe.setNumber(phone);
                            employe.setEmail(email);
                            employe.setIden(identifier);
                            Toast.makeText(context, "modifier", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, "Échec de la modification de l'employé", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
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
    public void Suppressionbuilder(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Suppression")
                .setMessage("Tu es sur de supprimer " + dname)
                .setPositiveButton("Supprimer ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int res = Db.deleteemploye(String.valueOf(did));
                        if (res >0) {
                            employes.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public Filter getFilter() {
        if (cs ==null)
        {
         cs = new Customfilter();
        }

        return cs;
    }
    class Customfilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();

                ArrayList<employe> filters = new ArrayList<>();
                for (int i = 0; i < tempemployes.size(); i++) {
                    String fullName = tempemployes.get(i).getFirstname().toUpperCase() + " " + tempemployes.get(i).getLastname().toUpperCase();
                    if (fullName.contains(constraint)) {
                        employe employee = new employe(tempemployes.get(i).getId(), tempemployes.get(i).getFirstname(), tempemployes.get(i).getLastname(), tempemployes.get(i).getIden(), tempemployes.get(i).getNumber(), tempemployes.get(i).getEmail(),tempemployes.get(i).getEmployeimage());
                        filters.add(employee);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = tempemployes.size();
                results.values = tempemployes;
            }
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
          employes = (ArrayList<employe>)results.values;
          notifyDataSetChanged();
        }
    }
}
