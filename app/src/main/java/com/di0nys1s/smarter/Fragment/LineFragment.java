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
import android.widget.DatePicker;
import android.widget.EditText;

import com.di0nys1s.smarter.JavaClass.URLs;
import com.di0nys1s.smarter.JavaClass.Universal;
import com.di0nys1s.smarter.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LineFragment extends Fragment {

    private View vReport;
    Calendar myCalendar;
    EditText editText;
    private DatePickerDialog.OnDateSetListener date;
    private LineChart chart;
    Double totalUsage;


    List<Entry> entries = new ArrayList<Entry>();

    dailyUsageServiceTask dailyUsageServiceTask = new dailyUsageServiceTask();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vReport = inflater.inflate(R.layout.fragment_line_chart, container, false);

        chart =(LineChart) vReport.findViewById(R.id.chart);

        myCalendar = Calendar.getInstance();
        editText = (EditText)vReport.findViewById(R.id.linechart_selectdate);

        final Context mContext = vReport.getContext();

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

                float[] yAxis = {1, 2, 4};

                float[] xAxis = {7, 14, 21};

                dailyUsageServiceTask.execute(URLs.FIND_USAGE_BY_RESID + Universal.getResId());

                for (int i=0; i < xAxis.length; i++){
                    entries.add(new Entry(xAxis[i], yAxis[i]));
                }

                //implementing IAxisValueFormatter interface to show year values not as float/decimal
                final String[] app = new String[] { "Fridge", "AC", "WM" };
                IAxisValueFormatter formatter = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return app[(int)value];
                    } };


                LineDataSet dataSet = new LineDataSet(entries, "This is Demo");
                dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                LineData lineData = new LineData(dataSet); chart.setData(lineData);
                XAxis xAxisFromChart = chart.getXAxis();
                xAxisFromChart.setDrawAxisLine(true);
                xAxisFromChart.setValueFormatter(formatter);
                //minimum axis-step (interval) is 1,if no, the same value will be displayed multiple times
                xAxisFromChart.setGranularity(1f); xAxisFromChart.setPosition(XAxis.XAxisPosition.BOTTOM);

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
                    //JSONObject jsonPart2 = resInfo.getJSONObject("fridgeusagekwh");
                    Double fridge = resInfo.getDouble("fridgeusagekwh");
                    Double ac = resInfo.getDouble("airconditionerusagekwh");
                    Double wm = resInfo.getDouble("washingmachineusagekwh");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
