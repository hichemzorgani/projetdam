package com.example.projet;

import android.graphics.Bitmap;

public class employe {
    public int id;
    public String firstname;
    public String lastname;
    public String iden;
    public String number;
    public String email;
    public Bitmap employeimage;


    public employe(int id,String firstname, String lastname, String iden, String number,String email, Bitmap employeimage) {
        this.id =id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.iden = iden;
        this.number = number;
        this.email = email;
        this.employeimage = employeimage;
    }

    public Bitmap getEmployeimage() {
        return employeimage;
    }

    public void setEmployeimage(Bitmap employeimage) {
        this.employeimage = employeimage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setIden(String iden) {
        this.iden = iden;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public String getIden() {
        return iden;
    }
    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }
}

