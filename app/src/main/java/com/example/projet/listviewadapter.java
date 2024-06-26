package com.example.projet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.util.ArrayList;

public class listviewadapter extends BaseAdapter implements Filterable {
    private static final int PICK_IMAGE_REQUEST = 99;
    private Uri imagepath;
    ArrayList<employe> employes,tempemployes;
    Customfilter cs;
    private Bitmap imagetostore;
    ImageView img1;

    Context context;

    int did;
    String dname;
    Dbemploye Db;
    employe employe;
    boolean imageChanged = false;

    public listviewadapter(Context context, ArrayList<employe> employes) {
        this.context = context;
        this.employes = employes;
        this.tempemployes=employes;
        Db = new Dbemploye(context);
        img1 = new ImageView(context);
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
            convertView = layoutInflater.inflate(R.layout.itemlistemploye, parent, false);
        }

        TextView firstname = convertView.findViewById(R.id.firstname);
        TextView lastname = convertView.findViewById(R.id.lastname);
        TextView iden = convertView.findViewById(R.id.iden);
        ImageView img = convertView.findViewById(R.id.img);

        Button btn = convertView.findViewById(R.id.btn);

        firstname.setText(emp.firstname);
        lastname.setText(emp.lastname);
        iden.setText(emp.iden);
        Bitmap bitmap = emp.getEmployeimage();
        img.setImageBitmap(bitmap);


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

    public void Modificationbuilder(int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_updateemploye, null);
        EditText editfirstname = view.findViewById(R.id.editf);
        EditText editlastname = view.findViewById(R.id.editl);
        EditText editnumber = view.findViewById(R.id.editn);
        EditText editemail = view.findViewById(R.id.edite);
        EditText editiden = view.findViewById(R.id.editi);
        img1 = view.findViewById(R.id.img1);

        String oldfirstname = employe.getFirstname();
        String oldlastname = employe.getLastname();
        String oldnumber = employe.getNumber();
        String oldemail = employe.getEmail();
        String oldiden = employe.getIden();
        Bitmap bitmap = employe.getEmployeimage();

        // Set existing data to the views
        img1.setImageBitmap(bitmap);
        editfirstname.setText(oldfirstname);
        editlastname.setText(oldlastname);
        editemail.setText(oldemail);
        editiden.setText(oldiden);
        editnumber.setText(oldnumber);

        // Image selection listener
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseImage();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setTitle(R.string.updateconfirm)
                .setMessage(R.string.enterinfo)
                .setIcon(R.drawable.baseline_autorenew_24)
                .setPositiveButton(R.string.updateconfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String identifier = editiden.getText().toString();
                        String firstname = editfirstname.getText().toString();
                        String lastname = editlastname.getText().toString();
                        String phone = editnumber.getText().toString();
                        String email = editemail.getText().toString();
                        int res;

                        // Check if a new image was selected
                        if (imageChanged) {
                            // Update employee with new image
                             res = Db.updateemploye(String.valueOf(did), identifier, firstname, lastname, email, phone, imagetostore);
                            employe.setEmployeimage(imagetostore);
                        } else {
                            // Update employee without changing the image
                             res = Db.updateemploye(String.valueOf(did), identifier, firstname, lastname, email, phone, bitmap);
                        }

                        if (res > 0) {
                            // Update other employee details
                            employe.setFirstname(firstname);
                            employe.setLastname(lastname);
                            employe.setNumber(phone);
                            employe.setEmail(email);
                            employe.setIden(identifier);
                            Toast.makeText(context, R.string.update, Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, R.string.toastupdatex, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
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

    private void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void handleImageSelectionResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                imagetostore = selectedImage;
                img1.setImageBitmap(selectedImage);
                imageChanged = true; // Image has been changed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void Suppressionbuilder(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete)
                .setMessage(context.getString(R.string.areyousurtodelete) + " " + dname)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int res = Db.deleteemploye(String.valueOf(did));
                        if (res >0) {
                            employes.remove(position);
                            notifyDataSetChanged();
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
