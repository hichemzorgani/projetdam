package com.example.projet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class emplyeadapter extends BaseAdapter {
    ArrayList<employe> employes;
    Context context;

    public emplyeadapter(Context context, ArrayList<employe> employes) {
        this.context = context;
        this.employes = employes;
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

        // Set the data to the TextViews
        firstname.setText(emp.firstname);
        lastname.setText(emp.lastname);
        iden.setText(emp.iden);
        number.setText(emp.number);
        email.setText(emp.email);

        return convertView;
    }
}
