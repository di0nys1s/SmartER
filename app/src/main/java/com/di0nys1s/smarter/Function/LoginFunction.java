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
import com.di0nys1s.smarter.JavaClass.User;
import com.di0nys1s.smarter.R;
import com.di0nys1s.smarter.Service.UserService;

public class LoginFunction extends AsyncTask<Void, Void, Void> {
        private TextView tvUserName;
        private TextView tvPassword;
        private Activity loginActivity;
        private boolean isMatch;
        private boolean isProblem;
        User user;

        public LoginFunction(Activity loginActivity, TextView tvUserName, TextView tvPassword) {
            this.tvUserName = tvUserName;
            this.tvPassword = tvPassword;
            this.loginActivity = loginActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isMatch = false;
            isProblem = false;
            user = null;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String usernameText = tvUserName.getText().toString();
            String passwordText = tvPassword.getText().toString();
            
            try {
                user = UserService.findUserByUsernameAndPassword(usernameText, passwordText);
                isMatch = user != null;
                isProblem = false;
            } catch (NullPointerException ex) {
                isMatch = false;
                isProblem = false;
            } catch (Exception ex) {
                isMatch = false;
                isProblem = true;
            }

            if (isMatch) {
                Universal.setPostcode(Integer.parseInt(user.getPostCode()));
                Universal.setFirstName(user.getFirstName());
                Universal.setLastName(user.getLastName());
                Universal.setResId(user.getResId());
                Universal.setAddress(user.getAddress().replaceAll("[-+.^:,]",""));
                handler.sendEmptyMessage(2);
            } else {
                if (isProblem) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message){
                if(message.what == 0)
                    Toast.makeText(loginActivity, "Connection problem please try again later.", Toast.LENGTH_LONG).show();
                else if(message.what == 1) {
                    TextView tvMessage = loginActivity.findViewById(R.id.textErrorMessage);
                    tvMessage.setText("Invalid username and/or password.");
                } else {
                    Universal.setPostcode(Integer.parseInt(user.getPostCode()));
                    Universal.setFirstName(user.getFirstName());
                    Universal.setLastName(user.getLastName());
                    Universal.setResId(user.getResId());
                    Universal.setAddress(user.getAddress().replaceAll("[-+.^:,]",""));
                    TextView tvMessage = loginActivity.findViewById(R.id.textErrorMessage);
                    tvMessage.setText("");
                    Intent intent = new Intent(loginActivity, MainActivity.class);
                    loginActivity.startActivityForResult(intent, 1);
                }

            }
        };
}
