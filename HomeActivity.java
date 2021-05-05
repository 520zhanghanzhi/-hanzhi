package com.example.user.teamproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.user.teamproject.InitiateActivity.SERVICE_INSTANCE;
import static com.example.user.teamproject.InitiateActivity.SERVICE_REG_TYPE;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private FriendListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView HomeUUINFO, HomeUsername;
    String friendUsername, friendUUINFO;
    FloatingActionButton fab;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;

    String myUUINFO;
    String myUsername;

    Date time;

    private CameraDevice cameraDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Check if WiFi is already enabled
        if (wifiManager.isWifiEnabled()) {

        } else {
            wifiManager.setWifiEnabled(true);
        }

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserDiscovery.class);
                startActivity(intent);

            }
        });

        //navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //user's information
        Intent intent = getIntent();;

        HomeUUINFO = navigationView.getHeaderView(0).findViewById(R.id.NavHeaderUUINFO);
        HomeUUINFO.setText(intent.getStringExtra("UUINFO"));
        HomeUsername = navigationView.getHeaderView(0).findViewById(R.id.NavHeaderUsername);
        HomeUsername.setText(intent.getStringExtra("Username"));



        startRegistration();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //list menu in navigation drawer
        if (id == R.id.nav_logout) {
            Intent intent = new Intent(HomeActivity.this, LoginPageActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Registers a local service and then initiates a service discovery
     */
    private void startRegistration() {
        Intent intent = getIntent();
        myUUINFO = intent.getStringExtra("UUINFO");
        myUsername = intent.getStringExtra("Username");

        final Map<String, String> record = new HashMap<String, String>();
        record.put("available", "visible");
        record.put("myUUINFO", myUUINFO);

        record.put("myUsername", myUsername);

        WifiP2pDnsSdServiceInfo service = WifiP2pDnsSdServiceInfo.newInstance(
                SERVICE_INSTANCE, SERVICE_REG_TYPE, record);

        mManager.addLocalService(mChannel, service, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Successfully added " + myUUINFO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int error) {
                Toast.makeText(getApplicationContext(), "Failed to add service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    

    @Override
    protected void onResume() {
        super.onResume();
    }
}
