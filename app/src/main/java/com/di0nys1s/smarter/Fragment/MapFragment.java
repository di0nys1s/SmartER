package com.di0nys1s.smarter.Fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.di0nys1s.smarter.JavaClass.URLs;
import com.di0nys1s.smarter.JavaClass.Universal;
import com.di0nys1s.smarter.R;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapquest.mapping.maps.MapView;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class MapFragment extends Fragment {
    View vMapFragment;
    private Double dailyTotal;
    private Double hourlyTotal;
    private MapboxMap mMapboxMap;
    private MapView mMapView;
    private LatLng userAddressLatLNG;

    mapApiTask mapTask = new mapApiTask();
    //residentServiceTask residentServiceTask = new residentServiceTask();

    private Spinner spinnerDailyHourly;
    String types[] = {"daily", "hourly"};
    ArrayAdapter<String> adapter;

    Date currentDate = new GregorianCalendar().getTime();
    DateFormat dfTime = new SimpleDateFormat("H");
    String time = dfTime.format(currentDate.getTime());
    int t = Integer.parseInt(time);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        vMapFragment = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) vMapFragment.findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                spinnerSelectionShowUsage();
                //residentServiceTask.execute(URLs.GET_ALL_USERS);
                mMapboxMap = mapboxMap;
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userAddressLatLNG, 10000));
            }});

        return vMapFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void addGreenMarker(MapboxMap mapboxMap) {
        IconFactory iconFactory = IconFactory.getInstance(Universal.getmContext());
        Icon iconGreen = iconFactory.fromResource(R.drawable.marker_green);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(Universal.getFirstName() + "'s Home");
        markerOptions.snippet("Daily Total Usage is " + dailyTotal + " kwh and " + "which is GOOD!!");
        markerOptions.setIcon(iconGreen);
        markerOptions.position(userAddressLatLNG);

        mapboxMap.addMarker(markerOptions);
    }

    private void addRedMarker(MapboxMap mapboxMap) {
        IconFactory iconFactory = IconFactory.getInstance(Universal.getmContext());
        Icon iconRed = iconFactory.fromResource(R.drawable.marker_red);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(Universal.getFirstName() + "'s Home");
        markerOptions.snippet("Daily Total Usage is " + dailyTotal + " kwh and " + "which is BAD!!");
        markerOptions.setIcon(iconRed);
        markerOptions.position(userAddressLatLNG);
        mapboxMap.addMarker(markerOptions);
    }

    public class mapApiTask extends AsyncTask<String, Void, String> {

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
                //JSONArray mapInfo = jsonObject.getJSONArray("results");

                JSONObject mapInfo = jsonObject.getJSONArray("results").getJSONObject(0);
                Log.i("Address content", mapInfo.toString());
                JSONObject locationInfo = mapInfo.getJSONArray(URLs.WS_KEY_MAP_LOCATION).getJSONObject(0);
                Log.i("Address content", locationInfo.toString());
                JSONObject latLngInfo = locationInfo.getJSONObject(URLs.WS_KEY_MAP_LATLNG);

                Double latInfo = latLngInfo.getDouble("lat");
                Log.i("Address content", latInfo.toString());
                Double lngInfo = latLngInfo.getDouble("lng");
                Log.i("Address content", lngInfo.toString());

                userAddressLatLNG = new LatLng(latInfo, lngInfo);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class residentServiceTask extends AsyncTask<String, Void, String> {

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
                    Double fridge = resInfo.getDouble("totalUsagekwh");
                    System.out.println(fridge);
               }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                Log.i("Daily Total Usage", result.toString());
                JSONArray jsonArray = new JSONArray(result);

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resInfo = jsonArray.getJSONObject(i);
                    dailyTotal = resInfo.getDouble("totalUsagekwh");
                    Log.i("Total Daily Content", dailyTotal.toString());

                    if (dailyTotal < 21)
                        addGreenMarker(mMapboxMap);
                    else
                        addRedMarker(mMapboxMap);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class hourlyUsageServiceTask extends AsyncTask<String, Void, String> {

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
                Log.i("Hourly Total Usage", result.toString());
                JSONArray jsonArray = new JSONArray(result);

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resInfo = jsonArray.getJSONObject(i);
                    int usageHour = resInfo.getInt("time");
                    //Log.i("Time", Integer.valueOf(usageHour).toString());
                    //Double totalHour= resInfo.getDouble("totalUsagekwh");
                    //Log.i("Total Hour Content", totalHour.toString());

                    if (t == usageHour) {
                        hourlyTotal= resInfo.getDouble("totalUsagekwh");
                        Log.i("Total Hour Content", hourlyTotal.toString());
                        break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void mapTaskExecute() {

        String address = Universal.getAddress();
        String[] addressParts = address.split(" ");

        ArrayList<StringBuilder> urlList = new ArrayList <>();
        StringBuilder mapURLBuilder;
        StringBuilder urlBuilder = new StringBuilder(URLs.MAP_WS_URL);
        for (int i = 0; i < addressParts.length; i++) {
            mapURLBuilder = urlBuilder.append(addressParts[i] + "+");
            urlList.add(mapURLBuilder);
        }
        System.out.println(urlList.get(urlList.size()-1));
        StringBuilder urlAddress = urlList.get(urlList.size()-1);

        mapTask.execute(urlAddress.toString());

    }

    public void spinnerSelectionShowUsage() {
        spinnerDailyHourly = vMapFragment.findViewById(R.id.spinnerDailyHourly);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, types);
        spinnerDailyHourly.setAdapter(adapter);

        spinnerDailyHourly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String itemPosition = adapter.getItem(position);
                if (URLs.MAP_VIEW_DAILY.equals(itemPosition)) {
                    mapTaskExecute();
                    dailyUsageServiceTask dailyUsageServiceTask = new dailyUsageServiceTask();
                    dailyUsageServiceTask.execute(URLs.FIND_HOURLY_USAGE_BY_RESID_DATE + "1" + "/" + "2018-03-18" + "/" + "daily");
                }
                else if(URLs.MAP_VIEW_HOURLY.equals(itemPosition))
                {
                    mapTaskExecute();
                    hourlyUsageServiceTask hourlyUsageServiceTask = new hourlyUsageServiceTask();
                    hourlyUsageServiceTask.execute(URLs.FIND_HOURLY_USAGE_BY_RESID_DATE + "1" + "/" + "2018-03-18" + "/" + "hourly");
                } else
                    {
                        mapTaskExecute();
                        dailyUsageServiceTask dailyUsageServiceTask = new dailyUsageServiceTask();
                        dailyUsageServiceTask.execute(URLs.FIND_HOURLY_USAGE_BY_RESID_DATE + "1" + "/" + "2018-03-18" + "/" + "daily");
                }

//                switch (position) {
//                    case 0:
//                        System.out.println(String.valueOf(spinnerDailyHourly.getItemAtPosition(0)));
//                        dailyUsageServiceTask.execute(URLs.FIND_HOURLY_USAGE_BY_RESID_DATE + "1" + "/" + "2018-03-18" + "/" + "daily");
//                    case 1:
//                        System.out.println(String.valueOf(spinnerDailyHourly.getItemAtPosition(1)));
//                        hourlyUsageServiceTask.execute(URLs.FIND_HOURLY_USAGE_BY_RESID_DATE + "1" + "/" + "2018-03-18" + "/" + "hourly");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
