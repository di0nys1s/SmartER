package com.di0nys1s.smarter.JavaClass;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    private int resId;
    private String address;
    private String dob;
    private String email;
    private String energyProvider;
    private String firstName;
    private String mobile;
    private int residentNumber;
    private String postCode;
    private String lastName;

    public User(String firstName,String lastName, String dob, String address, String postCode,
                String mobile, String email, int residentNumber, String energyProvider) {

            this.address = address;
            this.dob = dob;
            this.email = email;
            this.energyProvider = energyProvider;
            this.firstName = firstName;
            this.mobile = mobile;
            this.residentNumber = residentNumber;
            this.postCode = postCode;
            this.lastName = lastName;
    }


    public User(JSONObject jsonObject) throws JSONException, ParseException {
        resId = jsonObject.getInt(URLs.RESIDKEY);
        address = jsonObject.getString(URLs.ADDRESSKEY);
        Date date = new SimpleDateFormat(URLs.DATE_FORMAT).parse(jsonObject.getString(URLs.DOBKEY).substring(0, 9));
//      dob = date;
        email = jsonObject.getString(URLs.EMAILKEY);
        energyProvider = jsonObject.getString(URLs.PROVIDERKEY);
        firstName = jsonObject.getString(URLs.NAMEKEY);
        mobile = jsonObject.getString(URLs.MOBILEKEY);
        residentNumber = jsonObject.getInt(URLs.RESIDENTNUMBERKEY);
        postCode = jsonObject.getString(URLs.POSTCODEKEY);
        lastName = jsonObject.getString(URLs.LASTNAMEKEY);
    }

    public int getResId() {
        return resId;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getEnergyProvider() {
        return energyProvider;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return mobile;
    }

    public int getResidentNumber() {
        return residentNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnergyProvider(String energyProvider) {
        this.energyProvider = energyProvider;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPhone(String mobile) {
        this.mobile = mobile;
    }

    public void setResidentNumber(int residentNumber) {
        this.residentNumber = residentNumber;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
