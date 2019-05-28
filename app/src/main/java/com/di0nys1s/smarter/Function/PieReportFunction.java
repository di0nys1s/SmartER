package com.di0nys1s.smarter.Function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.di0nys1s.smarter.JavaClass.URLs;
import com.di0nys1s.smarter.JavaClass.Universal;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class PieReportFunction extends AsyncTask<Void, Void, Void> {
    private PieChart chart;
    private Context context;
    private String selectedDate;
    private JSONObject json;
    private Typeface mTfLight;
    dailyUsageServiceTask dailyUsageServiceTask = new dailyUsageServiceTask();
    Double totalUsage;
    Double fridge;
    Double ac;
    Double wm;

    public PieReportFunction(Context context, PieChart chart, String selectedDate) {
        this.context = context;
        this.chart = chart;
        this.selectedDate = selectedDate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            dailyUsageServiceTask.execute(URLs.FIND_APP_USAGE_BY_RESID_DATE_URL + Universal.getResId() + "/" + "2018-03-18");
        } catch (Exception e) {
            Log.e("Exception", Universal.getExceptionInfo(e));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        handler.sendEmptyMessage(0);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                try {
                    runPieChart(json);
                } catch (Exception e) {
                    Log.e("Exception", Universal.getExceptionInfo(e));
                }
            } else {

            }
        }
    };

    private void runPieChart(JSONObject json) throws JSONException {
        mTfLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setCenterTextTypeface(mTfLight);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        setDataToPieChart(json,100);
        chart.animateY(1000, Easing.EasingOption.EaseInCubic);
    }

    private void setDataToPieChart(JSONObject json, float range) throws JSONException {
        float mult = range;
        ArrayList<PieEntry> entries = new ArrayList<>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        double[] appUsage = new double[3];
        appUsage[0] = fridge;
        appUsage[1] = ac;
        appUsage[2] = wm;
        double total = appUsage[0] + appUsage[1] + appUsage[2];

        for (int i = 0; i < appUsage.length ; i++) {
            // set label for partition
            String label = "";
            switch (i) {
                case 0:
                    label = "Fridge";
                    break;
                case 1:
                    label = "AC";
                    break;
                case 2:
                    label = "WM";
                    break;
                default:
                    break;
            }
            entries.add(new PieEntry((float)(mult * (appUsage[i] / total)), label, appUsage[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Usage Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();

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
                json = jsonArray.getJSONObject(0);

                for(int i = 0; i < json.length(); i++) {
                    fridge = json.getDouble("fridgeusagekwh");
                    ac = json.getDouble("airconditionusagekwh");
                    wm = json.getDouble("washingmacgineusagekwh");
                    totalUsage = fridge + ac + wm;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
