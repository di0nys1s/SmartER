package com.di0nys1s.smarter.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.di0nys1s.smarter.Fragment.LineFragment;
import com.di0nys1s.smarter.Fragment.MainFragment;
import com.di0nys1s.smarter.Fragment.MapFragment;
import com.di0nys1s.smarter.Fragment.ReportFragment;
import com.di0nys1s.smarter.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //static boolean isLogin = false;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("SmartER");
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        if (id == R.id.nav_home) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment nextFragment = null;
            nextFragment = new MainFragment();

            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    nextFragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.nav_reports) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment nextFragment = null;
            nextFragment = new ReportFragment();

            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    nextFragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        if (id == R.id.nav_map) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment nextFragment = null;
            nextFragment = new MapFragment();

            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    nextFragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        if (id == R.id.nav_bar_chart) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment nextFragment = null;
            nextFragment = new ReportFragment();

            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    nextFragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        if (id == R.id.nav_line_graph) {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment nextFragment = null;
            nextFragment = new LineFragment();

            fragmentManager.beginTransaction().replace(R.id.content_frame,
                    nextFragment).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;

    }
}
