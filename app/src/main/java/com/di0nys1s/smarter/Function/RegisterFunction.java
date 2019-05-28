package com.di0nys1s.smarter.Function;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.di0nys1s.smarter.Activity.MainActivity;
import com.di0nys1s.smarter.JavaClass.Universal;
import com.di0nys1s.smarter.R;
import com.di0nys1s.smarter.Service.UserService;

import org.json.JSONArray;

public class RegisterFunction  extends AsyncTask<Void, Void, Void> {
    private String username;
    private String email;
    private TextView tvUserName;
    private TextView tvEmail;
    private Activity activity;

    private RegisterFunction.RegisterPageInfo registerPageInfo;
    boolean isSuccess;

    public RegisterFunction(Activity activity, TextView tvUserName, TextView tvEmail, RegisterFunction.RegisterPageInfo registerPageInfo) {
        this.registerPageInfo = registerPageInfo;
        this.activity = activity;
        this.tvUserName = tvUserName;
        this.tvEmail = tvEmail;
        this.username = tvUserName.getText().toString();
        this.email = tvEmail.getText().toString();
        isSuccess = false;
    }

    protected Void doInBackground(Void... params) {
        try {
            JSONArray userWithSameEmail = UserService.findByEmail(email);
            JSONArray userWithSameUsername = UserService.findByUsername(username);

            if (userWithSameEmail.length() > 0) {
                handler.sendEmptyMessage(1);
            } else if (userWithSameUsername.length() > 0) {
                handler.sendEmptyMessage(2);
            } else if (userWithSameEmail.length() == 0 && userWithSameUsername.length() == 0) {
                // user creation
                isSuccess = UserService.registerResident(registerPageInfo);
                handler.sendEmptyMessage(0);
            }
        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message message){
            if(message.what == 0) {
                try {
                    if (isSuccess) {
                        Universal.setAddress(registerPageInfo.getAddress());
                        Universal.setFirstName(registerPageInfo.getFirstName());
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivityForResult(intent, 1);
                    } else {
                        Toast.makeText(activity, "Error occurs in the page. Please try again.", Toast.LENGTH_LONG);
                    }
                } catch (Exception ex) {
                    Toast.makeText(activity, "Error occurs in the page. Please try again.", Toast.LENGTH_LONG);
                }
            } else if(message.what == 1) {
                tvEmail.setBackgroundColor(activity.getResources().getColor(R.color.errorBackground));
                ((TextView)activity.findViewById(R.id.textEmailErrorMsg)).setText(activity.getResources().getString(R.string.register_email_exist_msg));
            } else {
                tvUserName.setBackgroundColor(activity.getResources().getColor(R.color.errorBackground));
                ((TextView)activity.findViewById(R.id.textUserNameErrorMsg)).setText(activity.getResources().getString(R.string.register_username_exist_msg));
            }

        }
    };

    public static class RegisterPageInfo {
        private String firstName;
        private String lastName;
        private String dob;
        private String address;
        private String postcode;
        private String mobile;
        private String email;
        private String residentNumber;
        private String energyProvider;
        private String userName;
        private String password;

        public RegisterPageInfo(String firstName, String lastName, String dob, String address, String postcode, String mobile, String email, String residentNumber, String energyProvider, String userName, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.address = address;
            this.postcode = postcode;
            this.mobile = mobile;
            this.email = email;
            this.residentNumber = residentNumber;
            this.energyProvider = energyProvider;
            this.userName = userName;
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getDob() {
            return dob;
        }

        public String getAddress() {
            return address;
        }

        public String getPostcode() {
            return postcode;
        }

        public String getMobile() {
            return mobile;
        }

        public String getEmail() {
            return email;
        }

        public String getResidentNumber() {
            return residentNumber;
        }

        public String getEnergyProvider() {
            return energyProvider;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setSurName(String lastName) {
            this.lastName = lastName;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setResidentNumber(String residentNumber) {
            this.residentNumber = residentNumber;
        }

        public void setEnergyProvider(String energyProvider) {
            this.energyProvider = energyProvider;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
