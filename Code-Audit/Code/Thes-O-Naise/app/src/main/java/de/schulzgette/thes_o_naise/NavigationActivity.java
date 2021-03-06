package de.schulzgette.thes_o_naise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = getSupportFragmentManager();
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame_nav, new publishThesenFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Bitte noch einmal Zurück drücken", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_nav, new SettingsFragment())
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_publishThesen) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_nav, new publishThesenFragment())
                    .commit();
        } else if (id == R.id.nav_showthesen) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_nav, new getThesenFragment())
                    .commit();
        } else if (id == R.id.nav_matching) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_nav, new MatchingFragment())
                    .commit();
        } else if (id == R.id.nav_kandidaten) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame_nav, new KandidatenFragment())
                    .commit();
        } else if (id == R.id.nav_profil) {

            SharedPreferences sharedPreferences = getSharedPreferences("einstellungen", MODE_PRIVATE);
            String uid = sharedPreferences.getString("UID", "");
            String typ = sharedPreferences.getString("typ", "");
            if(typ.equals("waehler")) {
                Intent j = new Intent(this, MeinProfilWaehlerActivity.class);
                startActivity(j);
            }else {
                Intent i = new Intent(this, MeinProfilKandidatActivity.class);
                i.putExtra("KID", uid);
                i.putExtra("MODE", "MEINPROFIL");
                startActivity(i);
            }
        } else if (id == R.id.nav_ausloggen) {
            SharedPreferences sharedPreferences = getSharedPreferences("einstellungen", MODE_PRIVATE);
            sharedPreferences.edit().putString("token", "").apply();
            Intent i = new Intent (this, MainActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
