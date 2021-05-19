package com.example.restauranthelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.restauranthelper.DatabaseConnectionAdapter;
import com.example.restauranthelper.R;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class FragmentProfile extends Fragment {
    Connection connection;
    DatabaseConnectionAdapter databaseConnectionAdapter;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return view;
    }
    public FragmentProfile(Connection connection) {
        this.connection = connection;
        databaseConnectionAdapter = new DatabaseConnectionAdapter(connection);
    }

    private void init() {
        TextView name, status, statisticsDaily, statisticsOver;
        name = view.findViewById(R.id.textViewPersonaName);
        status = view.findViewById(R.id.textViewPersonaStatus);
        statisticsDaily = view.findViewById(R.id.textViewDayOverview);
        statisticsOver = view.findViewById(R.id.textViewAllTimeOverview);
        SharedPreferences getter = getContext().getSharedPreferences("innerlogin", MODE_PRIVATE);
        int person = getter.getInt("id", -1);
        statisticsDaily.setText(String.format(getString(R.string.statisticsL), getString(R.string.stshort1), getString(R.string.loading), getString(R.string.loading)));
        statisticsOver.setText(String.format(getString(R.string.statisticsL), getString(R.string.stshort2), getString(R.string.loading), getString(R.string.loading)));
        Thread thread = new Thread(()-> {
            name.post(()-> name.setText(databaseConnectionAdapter.getPersonString(person)));
            int statusInt = databaseConnectionAdapter.getPersonStatus(person);
            String st = "";
            if (statusInt == 5) st = getString(R.string.pstatus5);
            if (statusInt == 4) st = getString(R.string.pstatus4);
            if (statusInt == 2) st = getString(R.string.pstatus2);
            String finalSt = st;
            status.post(()-> status.setText(finalSt));
            String sds = String.format(getString(R.string.statistics), getString(R.string.stshort1), databaseConnectionAdapter.getOrdersCount(new Date(Calendar.getInstance().getTime().getTime()), person), databaseConnectionAdapter.getPredCount(new Date(Calendar.getInstance().getTime().getTime()), person));
            statisticsDaily.post(()-> statisticsDaily.setText(sds));
            if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("statistics", true)) {
                String ads = String.format(getString(R.string.statistics), getString(R.string.stshort2), databaseConnectionAdapter.getOrdersCount(null, person), databaseConnectionAdapter.getPredCount(null, person));
                statisticsOver.post(() -> statisticsOver.setText(ads));
            }
            else statisticsOver.post(()-> statisticsOver.setVisibility(View.GONE));
        });
        thread.start();

    }
}

