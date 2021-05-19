package com.example.restauranthelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    EditText login, password, code;
    SharedPreferences sharedPreferences;
    CheckBox logincheckbox;
    CheckBox passcheckbox;
    private static final String ip = "192.168.0.115";
    private static final String port = "1433";
    private static final String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static final String database = "RestDatabase";
    private static final String username = "admin";
    private static final String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;

    private Connection connection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_login);
        init();
        getInnerSettings();
    }

    private void getInnerSettings() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void init() {
        login = findViewById(R.id.etLogin);
        password = findViewById(R.id.etPassword);
        login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password.setText("");
            }
        });
        code = findViewById(R.id.etCode);
        logincheckbox = findViewById(R.id.checkBoxLogin);
        passcheckbox = findViewById(R.id.checkBoxCode);
        sharedPreferences = getSharedPreferences("innerlogin", MODE_PRIVATE);
        if ( sharedPreferences!=null ) {
            if (sharedPreferences.getBoolean("toogle1", false)) logincheckbox.setChecked(true);
            if (sharedPreferences.getBoolean("toogle2", false)) passcheckbox.setChecked(true);
            login.setText(sharedPreferences.getString("login", ""));
            if (logincheckbox.isChecked())
                password.setText(sharedPreferences.getString("password", ""));
            if (passcheckbox.isChecked()) code.setText(sharedPreferences.getString("code", ""));
        }

    }

    public void connect(View view){
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, code.getEditableText().toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.connectionError), Toast.LENGTH_LONG).show();
        }

        if (connection!=null){
            Statement statement;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("Select * from Users WHERE Login = '" + login.getEditableText().toString() + "' AND Password = '"+ password.getEditableText().toString() +"';");
                if (resultSet.next()) {
                    SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                    prefEditor.putInt("id", resultSet.getInt("ID"));
                    Runnable inlogin = () ->
                     {
                        Intent intent = new Intent(this, MainActivity.class);
                        if (logincheckbox.isChecked()) {
                            prefEditor.putString("password", password.getEditableText().toString());
                            prefEditor.putBoolean("toogle1", true);
                        }
                        if (passcheckbox.isChecked()) {
                            prefEditor.putString("code", code.getEditableText().toString());
                            prefEditor.putBoolean("toogle2", true);
                        }
                        prefEditor.putString("login", login.getEditableText().toString());

                        prefEditor.apply();
                        intent.putExtra("url", url);
                        intent.putExtra("password", code.getEditableText().toString());
                        startActivity(intent);
                    };
                    Toast.makeText(this, R.string.youveentered, Toast.LENGTH_LONG).show();
                    Thread thread = new Thread(inlogin);
                    thread.start();
                }
                else Toast.makeText(this, R.string.nonlogin, Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}