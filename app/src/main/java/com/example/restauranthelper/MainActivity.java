package com.example.restauranthelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Connection connection;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connect();
        init(savedInstanceState);
        privateSet();
        Thread thread = new Thread(() -> toolbar.post(()->toolbar.setTitle(getString(R.string.menuPreview))));
        thread.start();

    }

    private void privateSet() {
        DatabaseConnectionAdapter adapter = new DatabaseConnectionAdapter(connection);
        int privateAccess = adapter.getPersonStatus(getSharedPreferences("innerlogin", Context.MODE_PRIVATE).getInt("id", -1));
        if (privateAccess <= 2) {
            navigationView.getMenu().findItem(R.id.nav_money).setVisible(false);
        }
        if (privateAccess == 1) {
            navigationView.getMenu().findItem(R.id.nav_makeOrder).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_checkBills).setVisible(false);
        }
    }

    private void init(Bundle savedInstanceState) {
        toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentMenu(connection)).commit();
            navigationView.setCheckedItem(R.id.nav_menuPreview);
        }

    }

    public void connect() {
        try {
            String classes = "net.sourceforge.jtds.jdbc.Driver";
            Class.forName(classes);
            String username = "admin";
            connection = DriverManager.getConnection(getIntent().getExtras().getString("url"), username, getIntent().getExtras().getString("password"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.connectionError, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isOpen()) drawer.close();
        else super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.close();
        switch (item.getItemId()){
            case R.id.nav_menuPreview :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentMenu(connection)).commit();
                    toolbar.setTitle(item.getTitle());
                break;
            case R.id.nav_makeOrder :
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentMenuOrder(connection)).commit();
                    toolbar.setTitle(item.getTitle());
                    break;
            case R.id.nav_cookOrder :
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentOrders(connection)).commit();
                toolbar.setTitle(item.getTitle());
                break;
            case R.id.nav_checkBills:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentBills(connection)).commit();
                toolbar.setTitle(item.getTitle());
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentProfile(connection)).commit();
                toolbar.setTitle(item.getTitle());
                break;
            case  R.id.nav_money:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentSubtotal(connection)).commit();
                toolbar.setTitle(item.getTitle());
                break;
            case R.id.nav_checkStatus:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentTables(connection)).commit();
                toolbar.setTitle(item.getTitle());
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentChat(connection)).commit();
                toolbar.setTitle(item.getTitle());
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new SettingsFragment()).commit();
                toolbar.setTitle(item.getTitle());
                break;
        }
        return true;
    }
}