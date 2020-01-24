package com.gova.openmap.models;

import java.io.Serializable;
import java.util.ArrayList;
public class Mechanic implements Serializable {

    private String name;
    private String imageUri;
    private String primaryPhone;
    private String secondaryPhone;
    private String address;
    private ArrayList<String> mechanicTypes;


    public Mechanic(String name, String imageUri, String primaryPhone, String secondaryPhone, String address, ArrayList<String> mechanicTypes) {


        if (secondaryPhone.trim().isEmpty())
        {
            this.secondaryPhone = "No secondary phone";
        }

        this.name = name;
        this.imageUri = imageUri;
        this.primaryPhone = primaryPhone;
        this.secondaryPhone = secondaryPhone;
        this.address = address;
        this.mechanicTypes = mechanicTypes;
    }

    public Mechanic() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<String> getMechanicTypes() {
        return mechanicTypes;
    }

    public void setMechanicTypes(ArrayList<String> mechanicTypes) {
        this.mechanicTypes = mechanicTypes;
    }
}
