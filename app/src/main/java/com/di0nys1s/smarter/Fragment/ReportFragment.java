package com.di0nys1s.smarter.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.di0nys1s.smarter.Function.PieReportFunction;
import com.di0nys1s.smarter.JavaClass.URLs;
import com.di0nys1s.smarter.JavaClass.Universal;
import com.di0nys1s.smarter.R;
import com.github.mikephil.charting.charts.PieChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportFragment extends Fragment {
    private View vReport;
    private Button bReport;
    private TextView tvReport;
    private PieChart pieChart;
    private Calendar myCalendar;
    private EditText editText;
    private DatePickerDialog.OnDateSetListener date;

    dailyUsageServiceTask dailyUsageServiceTask = new dailyUsageServiceTask();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vReport = inflater.inflate(R.layout.fragment_report, container, false);
        tvReport = vReport.findViewById(R.id.tv_report);

        pieChart =(PieChart) vReport.findViewById(R.id.piechart);
        myCalendar = Calendar.getInstance();
        editText = (EditText)vReport.findViewById(R.id.piechart_selectdate);

        final Context mContext = vReport.getContext();

        dailyUsageServiceTask.execute(URLs.FIND_APP_USAGE_BY_RESID_DATE_URL + Universal.getResId() + "/" + "2018-03-18");

        datePickerDialog();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String selectedDate = editText.getText().toString();
                PieReportFunction pieReportFunction = new PieReportFunction(mContext, pieChart, selectedDate);
                pieReportFunction.execute();
            }
        });

        return vReport;
    }

    public void datePickerDialog() {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String format = URLs.DATE_FORMAT;
                SimpleDateFormat sdf = new SimpleDateFormat(format);

                editText.setText(sdf.format(myCalendar.getTime()));
            }
        };
    }

    public class dailyUsageServiceTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject resInfo = jsonArray.getJSONObject(0);

                for(int i = 0; i < resInfo.length(); i++) {
                    Double fridge = resInfo.getDouble("fridgeusagekwh");
                    Double ac = resInfo.getDouble("airconditionusagekwh");
                    Double wm = resInfo.getDouble("washingmacgineusagekwh");
                    Double totalUsage = fridge + ac + wm;

                    //tvReport.setText(totalUsage.toString());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}