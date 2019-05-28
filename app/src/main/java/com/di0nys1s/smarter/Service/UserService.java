package com.di0nys1s.smarter.Service;

import com.di0nys1s.smarter.Function.RegisterFunction;
import com.di0nys1s.smarter.JavaClass.URLs;
import com.di0nys1s.smarter.JavaClass.Universal;
import com.di0nys1s.smarter.JavaClass.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserService {

    public static JSONArray findByEmail(String email) throws IOException, JSONException {
        JSONArray jsonObject;
        try {
            jsonObject = GeneralWebService.requestRESTServiceArray(URLs.FIND_USER_BY_EMAIL_WS + email);
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            throw ex;
        }
        return jsonObject;
    }

    public static JSONArray findByUsername(String username) throws IOException, JSONException {
        JSONArray jsonObject = new JSONArray();
        try {
            jsonObject = GeneralWebService.requestRESTServiceArray(URLs.FIND_CREDENTIAL_BY_USERNAME_WS + username);
        } catch (NullPointerException ex){
            return null;
        } catch (Exception ex) {
            throw ex;
        }
        return jsonObject;
    }

    public static List<User> findAllUsers() throws IOException, JSONException, ParseException {
        List<User> users = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = GeneralWebService.requestRESTServiceArray(URLs.GET_ALL_USERS);

            int position = 0;
            while (position < jsonArray.length()) {
                JSONObject jsonObj = jsonArray.getJSONObject(position);
                User user = new User(jsonObj);
                users.add(user);
                position ++;
            }
        } catch (Exception ex) {
            throw ex;
        }

        return users;
    }

    public static User findUserByUsernameAndPassword(String username, String password) throws IOException, JSONException, ParseException {
        User result = null;
        JSONObject residentCredential = null;

        try {
            JSONArray jsonArray = GeneralWebService.requestRESTServiceArray(URLs.get_username_password + username + "/" + password);
            if (jsonArray.length() > 0)
                residentCredential = jsonArray.getJSONObject(0);
            else
                residentCredential = null;

            if (residentCredential != null) {
                JSONObject residentCredentialInfo = residentCredential.getJSONObject(URLs.RESIDKEY);
                result = new User(residentCredentialInfo);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    public static boolean registerResident(RegisterFunction.RegisterPageInfo registerPageInfo)
            throws JSONException, IOException, ParseException {
        boolean isSuccessSave = false;
        JSONObject jsonResident = new JSONObject();
        jsonResident.put(URLs.ADDRESSKEY, registerPageInfo.getAddress());
        SimpleDateFormat f = new SimpleDateFormat(URLs.DATE_FORMAT);
        SimpleDateFormat f1 = new SimpleDateFormat(URLs.SERVER_DATE_FORMAT);
        jsonResident.put(URLs.DOBKEY, registerPageInfo.getDob());
        jsonResident.put(URLs.EMAILKEY, registerPageInfo.getEmail());
        jsonResident.put(URLs.PROVIDERKEY, registerPageInfo.getEnergyProvider());
        jsonResident.put(URLs.NAMEKEY, registerPageInfo.getFirstName());
        jsonResident.put(URLs.LASTNAMEKEY, registerPageInfo.getLastName());
        jsonResident.put(URLs.MOBILEKEY, registerPageInfo.getMobile());
        jsonResident.put(URLs.RESIDENTNUMBERKEY, registerPageInfo.getResidentNumber());
        jsonResident.put(URLs.POSTCODEKEY, Integer.parseInt(registerPageInfo.getPostcode()));

        String saveResidentResult = GeneralWebService.postRESTService(URLs.SAVE_RESIDENT_URL_2, jsonResident);

        if (URLs.SUCCESS_MSG.equals(saveResidentResult)) {
            JSONArray jsonArray = GeneralWebService.requestRESTServiceArray(URLs.FIND_USER_BY_EMAIL_WS + registerPageInfo.getEmail());
            JSONObject residJson = (JSONObject)jsonArray.get(0);
            JSONObject credentialJson = new JSONObject();
            credentialJson.put(URLs.WS_KEY_CREDENTIAL_PASSWORD, registerPageInfo.getPassword()/*Universal.encryptPwd(registerPageInfo.getPwd())*/);
            credentialJson.put(URLs.RESIDKEY, residJson);
            credentialJson.put(URLs.WS_KEY_CREDENTIAL_REGISTER_DATE, f1.format(Calendar.getInstance().getTime()));
            credentialJson.put(URLs.WS_KEY_CREDENTIAL_USERNAME, registerPageInfo.getUserName());
            String saveResidentCredentialResult = GeneralWebService.postRESTService(URLs.SAVE_RESIDENT_CREDENTIAL_URL, credentialJson);

            if (URLs.SUCCESS_MSG.equals(saveResidentCredentialResult)) {
                Universal.setResId(residJson.getInt(URLs.RESIDKEY));
                isSuccessSave = true;
            }
        }

        return isSuccessSave;
    }

}

