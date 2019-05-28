package com.di0nys1s.smarter.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.di0nys1s.smarter.Function.RegisterFunction;
import com.di0nys1s.smarter.JavaClass.URLs;
import com.di0nys1s.smarter.JavaClass.Universal;
import com.di0nys1s.smarter.R;
import com.di0nys1s.smarter.Service.UserService;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private RegisterFunction.RegisterPageInfo registerPageInfo;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etDOB;
    private EditText etAddress;
    private EditText etPostcode;
    private EditText etMobile;
    private EditText etNoOfResidents;
    private Spinner residentNumberSpinner;
    private EditText etProvider;
    private Spinner providerSpinner;
    private Button bRegisterSubmit;
    private Button bCancel;


    private TextView msgFirstName;
    private TextView msgLastName;
    private TextView msgDOB;
    private TextView msgAddress;
    private TextView msgPostcode;
    private TextView msgPhone;
    private TextView msgEmail;
    private TextView msgNoOfResident;
    private TextView msgEnergyProvider;
    private TextView msgUsername;
    private TextView msgPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialVariable();

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDatePickerDialog();
            }
        });


        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        bRegisterSubmit.setOnClickListener(new View.OnClickListener() { @Override
        public void onClick(View v) {


                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                        String firstName = etFirstName.getText().toString();
                        String surName = etLastName.getText().toString();
                        String dob = etDOB.getText().toString();
                        String address = etAddress.getText().toString();
                        String postcode = etPostcode.getText().toString();
                        String mobile = etMobile.getText().toString();
                        String email = etEmail.getText().toString();
                        String residentNumber = etNoOfResidents.getText().toString();
                        String energyProvider = etProvider.getText().toString();
                        String userName = etUsername.getText().toString();
                        String password = etPassword.getText().toString();

                        RegisterFunction.RegisterPageInfo registerPageInfo = new RegisterFunction.RegisterPageInfo(
                            firstName, surName, dob, address, postcode, mobile, email, residentNumber, energyProvider, userName, password);
                            UserService.registerResident(registerPageInfo);

                            validateRegister(registerPageInfo);

                            RegisterFunction registerFunction = new RegisterFunction(RegisterActivity.this,
                                    (TextView)findViewById(R.id.editRegisterUsername),
                                    (TextView)findViewById(R.id.editRegisterPassword), registerPageInfo);
                            registerFunction.execute();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }
                });

                thread.start();

//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });

    }
    
    private boolean validateRegister(RegisterFunction.RegisterPageInfo entity) {
        boolean result = false;
        result = isEmpty(entity.getFirstName(), getResources().getString(R.string.register_first_name_empty_msg), msgFirstName);
        result = isEmpty(entity.getLastName(), getResources().getString(R.string.register_surname_empty_msg), msgLastName) && result;
        result = isEmpty(entity.getDob(), getResources().getString(R.string.register_dob_empty_msg), msgDOB) && result;
        result = checkDOB(entity.getDob(), getResources().getString(R.string.register_dob_format_msg)) && result;
        result = isEmpty(entity.getAddress(), getResources().getString(R.string.register_address_empty_msg), msgAddress) && result;
        result = isEmpty(entity.getPostcode(), getResources().getString(R.string.register_postcode_empty_msg), msgPostcode) && result;
        result = isEmpty(entity.getMobile(), getResources().getString(R.string.register_mobile_empty_msg), msgPhone) && result;
        result = isEmpty(entity.getEmail(), getResources().getString(R.string.register_email_empty_msg), msgEmail) && result;
        result = isEmailFormat(entity.getEmail(), getResources().getString(R.string.register_email_format_msg)) && result;
        result = isEmpty(entity.getResidentNumber(), getResources().getString(R.string.register_resident_number_empty_msg), msgNoOfResident) && result;
        result = isEmpty(entity.getEnergyProvider(), getResources().getString(R.string.register_energy_provider_empty_msg), msgEnergyProvider) && result;
        result = isUserNameNullOrEmpty(entity.getUserName(), getResources().getString(R.string.register_username_empty_msg)) && result;
        result = isEmpty(entity.getPassword(), getResources().getString(R.string.register_pwd_empty_msg), msgPwd) && result;
        result = isPasswordFormat(entity.getPassword(), getResources().getString(R.string.register_pwd_format_msg)) && result;

        return result;

    }

    private boolean isEmpty(String str, String message, TextView msgView) {
            boolean result;
            if (Universal.isEmptyOrNull(str)) {
                result = false;
                msgView.setText(message);
            } else {
                result = true;
                msgView.setText("");
            }
            return result;
        }

    private boolean isUserNameNullOrEmpty(String str, String message) {
            boolean result;
            if (Universal.isEmptyOrNull(str)) {
                result = false;
                msgUsername.setText(message);
            } else {
                result = true;
                msgUsername.setText("");
                findViewById(R.id.editRegisterUsername).setBackgroundColor(getResources().getColor(R.color.whiteBg));
            }
            return result;
        }
            
    private boolean isEmailFormat(String str, String message) {
            boolean result = false;
            if (!str.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
                result = false;
                msgEmail.setText(message);
            } else {
                result = true;
                findViewById(R.id.editEmail).setBackgroundColor(getResources().getColor(R.color.whiteBg));
            }
            return result;
        }

    private boolean isPasswordFormat(String str, String message) {
            boolean result = false;
            // check if the string is a strong password. At least 8 length. Contains at least 1 special character, 1 lower&upper letter, and 1 number
            if (!str.trim().matches("^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8}$")) {
                result = false;
                msgPwd.setText(message);
            } else {
                result = true;
            }
            return result;
        }

    private boolean checkDOB(String date, String message) {
            boolean result = false;
            SimpleDateFormat dateFormat = new SimpleDateFormat(URLs.DATE_FORMAT);
            try {
                Date dateFromUI = dateFormat.parse(date);
                if (dateFromUI.after(Calendar.getInstance().getTime())) {
                    result = false;
                    msgDOB.setText(message);
                } else {
                    result = true;
                }
            } catch (ParseException ex) {
                result = false;
                msgDOB.setText("Please select a valid birthday.");
            }

                return result;
                }

    private void setDatePickerDialog() {
    final Calendar cal = Calendar.getInstance();

    DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            cal.set(year, monthOfYear, dayOfMonth);
            etDOB.setText(dateFormat.format(cal.getTime()));
        }
    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    dialog.show();
    }

    public void initialVariable() {

        etFirstName = findViewById(R.id.editTextFirstName);
        etLastName = findViewById((R.id.editTextLastName));
        etEmail= findViewById((R.id.editEmail));
        etUsername = findViewById(R.id.editRegisterUsername);etPassword = findViewById(R.id.editRegisterPassword);
        etDOB = findViewById((R.id.editDateOfBirth));
        etLastName = findViewById((R.id.editTextLastName));
        etAddress = findViewById((R.id.editTextAddress));
        etPostcode = findViewById((R.id.editTextPostCode));
        etMobile = findViewById((R.id.editTextMobile));
        residentNumberSpinner = findViewById(R.id.spinnerResidentNumber);
        providerSpinner = findViewById(R.id.spinnerProvider);
        bRegisterSubmit = findViewById(R.id.b_register_submit);
        bCancel = findViewById(R.id.b_cancel);

        msgFirstName = findViewById(R.id.textFirstErrorMsg);
        msgLastName = findViewById(R.id.textSurErrorMsg);
        msgDOB = findViewById(R.id.textDOBErrorMsg);
        msgAddress = findViewById(R.id.textAddressErrorMsg);
        msgPostcode = findViewById(R.id.textPostcodeErrorMsg);
        msgPhone = findViewById(R.id.textPhoneErrorMsg);
        msgEmail = findViewById(R.id.textEmailErrorMsg);

        msgUsername = findViewById(R.id.textUserNameErrorMsg);
        msgPwd = findViewById(R.id.textPwdErrorMsg);

        String[] arraySpinner = new String[] {
                "1", "2", "3", "4", "5"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        residentNumberSpinner.setAdapter(adapter);

        String[] arraySpinner2 = new String[] {
                "Blue", "Red", "White", "Black", "Yellow"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        providerSpinner.setAdapter(adapter2);

    }
}


