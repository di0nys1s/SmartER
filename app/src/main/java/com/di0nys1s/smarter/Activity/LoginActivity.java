package com.di0nys1s.smarter.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.di0nys1s.smarter.Function.LoginFunction;
import com.di0nys1s.smarter.R;

public class LoginActivity extends AppCompatActivity {

    private Button bRegisterNavigate;
    private Button bLogin;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bRegisterNavigate = findViewById(R.id.b_register_navigate);
        bRegisterNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        bLogin = findViewById(R.id.b_login);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername = findViewById(R.id.editLoginUsername);
                etPassword = findViewById(R.id.editLoginPassword);
                LoginFunction loginFunction = new LoginFunction(LoginActivity.this, etUsername, etPassword);
                loginFunction.execute();
            }
        });
    }
}