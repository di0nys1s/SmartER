package com.di0nys1s.smarter.Fragment;

import android.app.Fragment;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.di0nys1s.smarter.Database.DBManager;
import com.di0nys1s.smarter.JavaClass.ApplianceGenerator;
import com.di0nys1s.smarter.JavaClass.ElectricityUsage;
import com.di0nys1s.smarter.JavaClass.URLs;
import com.di0nys1s.smarter.JavaClass.Universal;
import com.di0nys1s.smarter.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainFragment extends Fragment implements View.OnClickListener {
    View vMain;
    private ImageView imgUsage;
    private TextView tvTemperature;
    private  TextView tvName;
    private  TextView tvTest;
    private  TextView tvAddress;
    private  TextView tvDay;
    private TextView tvNice;
    private Button bGenerate;
    private Button bPost;
    private double temperature;
    protected DBManager dbManager;
    private ApplianceGenerator generator;

    weatherApiTask weatherApiTask = new weatherApiTask();
    usageServiceTask serviceTask = new usageServiceTask();

    Date currentDate = new GregorianCalendar().getTime();
    DateFormat dfDay = new SimpleDateFormat("E");
    DateFormat dfTime = new SimpleDateFormat("HH:mm");
    DateFormat dfHour = new SimpleDateFormat("HH");

    //int hour = Integer.parseInt(dfHour.format(currentDate));
    String strCurrentDay = dfDay.format(currentDate);
    String strCurrentTime = dfTime.format(currentDate);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);

        dbManager = new DBManager(this.getActivity());

        findPageElement();

        weatherApiTask.execute(URLs.WEATHER_WS_URL + Universal.getPostcode() + "," + "AU" + URLs.WS_KEY_WEATHER_APPID);
        serviceTask.execute(URLs.FIND_USAGE_BY_RESID + Universal.getResId());

        pickCurrentDay();
        showUserInformation();

        bGenerate.setOnClickListener(this);
        bPost.setOnClickListener(this);

        return vMain;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.b_generateAppliance:
                insertApplianceUsageToLocalDB();
                break;

            case R.id.b_postAppliance:
                //readData();
                //postApplianceUsagetoREST();
                Log.i("DB", "Data is moved to REST DB");
                deleteDataById(Universal.getResId());
                break;
        }

    }

    public class usageServiceTask extends AsyncTask<String, Void, String> {

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
                    Double totalUsage = fridge + ac + wm;
                    tvNice.setText(totalUsage.toString());

                    mainUsageMessage(totalUsage);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class weatherApiTask extends AsyncTask<String, Void, String> {

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

                JSONObject jsonObject = new JSONObject(result);
                JSONArray weatherInfo = jsonObject.getJSONArray("weather");

                //Log.i("Weather content", weatherInfo.toString());

                for(int i = 0; i < weatherInfo.length(); i++) {
                    JSONObject jsonPart = weatherInfo.getJSONObject(i);
                    Log.i("main", jsonPart.getString("main"));
                    //Log.i("description", jsonPart.getString("description"));
                }

                JSONObject mainInfo = jsonObject.getJSONObject("main");
                //Log.i("temperature", mainInfo.getDouble("temp") + "");
                temperature = mainInfo.getDouble("temp");
                final int kelvinToCelcius = 273;
                temperature = Math.round((temperature - kelvinToCelcius)*100.00)/100.0;
                DecimalFormat df = new DecimalFormat("#.#");
                String temperatureProperFormat = df.format(temperature);
                tvTemperature.setText("Current weather is " + temperatureProperFormat + " ℃");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertApplianceUsageToLocalDB() {
        try {
            dbManager.open();
        }
        catch(SQLException e) {
            e.printStackTrace();
            tvNice.setText("Nice to REST DB!!!");
        }

        Date currentDate = new GregorianCalendar().getTime();
        DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfTime = new SimpleDateFormat("HH");
        String time = dfTime.format(currentDate.getTime());
        String strCurrentDay = dfDate.format(currentDate);
        int t = Integer.parseInt(time);

        generator = new ApplianceGenerator();
        //you can enter any data here
        dbManager.insertUsage(
                Universal.getResId(),
                strCurrentDay,
                t,
                generator.getRandomFridgeUsage()[t],
                generator.getRandomWmUsage()[t],
                generator.getRandomAcUsage(temperature)[t],
                Double.valueOf(temperature));

        dbManager.close();
        Log.i("DB", "Data generated and sent to local database");
    }

    public void postApplianceUsagetoREST() {

        String[] usages = new String[8];
        usages[0] = readData().getString(1);
        usages[1] = readData().getString(2);
        usages[2] = readData().getString(3);
        usages[3] = readData().getString(4);
        usages[4] = readData().getString(5);
        usages[5] = readData().getString(6);
        usages[6] = readData().getString(7);
        usages[7] = readData().getString(8);

        System.out.println(usages[0]);
        System.out.println(usages[1]);
        System.out.println(usages[2]);
        System.out.println(usages[3]);
        System.out.println(usages[4]);
        System.out.println(usages[5]);
        System.out.println(usages[6]);
        System.out.println(usages[7]);


        new AsyncTask<String, Void, String>() { @Override
        protected String doInBackground(String... params) {


            ElectricityUsage usage = new ElectricityUsage(Integer.valueOf(params[0]),
                    Integer.valueOf(params[1]), params[2], Integer.valueOf(params[3]),
                    Double.valueOf(params[4]), Double.valueOf(params[5]), Double.valueOf(params[6]),
                    Double.valueOf(params[7]));

            createUsage(usage);
            return "Usage is added";
        }
            @Override
            protected void onPostExecute(String response) {
                //Log.i("POST", "Data posted to the REST");
            }
        }.execute(readData().getColumnName(0), readData().getColumnName(1),
                readData().getColumnName(2), readData().getColumnName(3),
                readData().getColumnName(4), readData().getColumnName(5),
                readData().getColumnName(6), readData().getColumnName(7));
        Log.i("DB", "Data is moved to REST DB");
    }

    public Cursor readData() {
        try {
            dbManager.open();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        Cursor c = dbManager.getAllAppliances();
        //System.out.println(c.getInt(0));
        //dbManager.close();

        return c;
    }

    public void deleteDataById(Integer resid) {

        try {
            dbManager.open();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        dbManager.deleteUsageById(resid);
        dbManager.close();
        Log.i("DB", "Data is deleted in local database");
    }

    private void mainUsageMessage(Double hourlyUsage) {
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        double totalUsage = hourlyUsage;
        
        if (dayOfWeek <= 1 && dayOfWeek <= 7 && hour <= 9 && hour <= 22) {
            if (totalUsage > 1.5) {
                tvNice.setText(R.string.negative_msg);
                imgUsage.setImageResource(R.drawable.negative);
            } else {
                tvNice.setText(R.string.positive_msg);
                imgUsage.setImageResource(R.drawable.positive);
            }
        } else{
            tvNice.setText(R.string.positive_msg);
            imgUsage.setImageResource(R.drawable.positive);
        }
    }

    public void pickCurrentDay() {
        if (strCurrentDay.equals("Mon"))
            strCurrentDay = "Monday";
        else if (strCurrentDay.equals("Tue"))
            strCurrentDay = "Tuesday";
        else if (strCurrentDay.equals("Wed"))
            strCurrentDay = "Wednesday";
        else if (strCurrentDay.equals("Thu"))
            strCurrentDay = "Thursday";
        else if (strCurrentDay.equals("Fri"))
            strCurrentDay = "Friday";
        else if (strCurrentDay.equals("Sat"))
            strCurrentDay = "Saturday";
        else if (strCurrentDay.equals("Sun"))
            strCurrentDay = "Sunday";
    }

    public void showUserInformation() {
        tvDay.setText("Today is " + strCurrentDay + " and the time is " + strCurrentTime);
        tvName.setText("Hi " + Universal.getFirstName() + " " + Universal.getLastName() + "!");
        tvAddress.setText("Near " + Universal.getAddress() + ".");
        //tvTest.setText(String.valueOf(Universal.getLatitude() + Universal.getLongtitude() + Universal.getPostcode()));
    }

    public void findPageElement() {
        tvTemperature = vMain.findViewById(R.id.tv_temperature);
        tvName = vMain.findViewById(R.id.tv_name);
        tvAddress = vMain.findViewById(R.id.tv_address);
        tvDay = vMain.findViewById(R.id.tv_date);
        tvNice = vMain.findViewById(R.id.tv_nice);
        tvTest = vMain.findViewById(R.id.tv_test);
        bGenerate = vMain.findViewById(R.id.b_generateAppliance);
        bPost = vMain.findViewById(R.id.b_postAppliance);
        imgUsage = vMain.findViewById(R.id.imgUsage);

    }

    public static void createUsage(ElectricityUsage usage){
        //initialise
        URL url = null;
        HttpURLConnection conn = null;
        try {
            Gson gson =new Gson();
            String stringUsageJson=gson.toJson(usage);
            url = new URL(URLs.SMARTER_WS_electricityusage_URL);
            //open the connection
            conn = (HttpURLConnection) url.openConnection();
            //set the timeout
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            //set the connection method to POST
            conn.setRequestMethod("POST"); //set the output to true
            conn.setDoOutput(true);
            //set length of the data you want to send
            conn.setFixedLengthStreamingMode(stringUsageJson.getBytes().length); //add HTTP headers
            conn.setRequestProperty("Content-Type", "application/json");
            //Send the POST out
            PrintWriter out= new PrintWriter(conn.getOutputStream());
            out.print(stringUsageJson);
            out.close();
            Log.i("error",new Integer(conn.getResponseCode()).toString());
        }
        catch (Exception e) { e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

}
