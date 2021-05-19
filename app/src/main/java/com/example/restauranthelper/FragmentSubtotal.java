package com.example.restauranthelper;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSubtotal extends Fragment {
    Connection connection;
    DatabaseConnectionAdapter databaseConnectionAdapter;
    View view;
    Calendar calendar;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_subtotal, container, false);
        calendar = Calendar.getInstance();
        init();
        Button button = view.findViewById(R.id.buttonDataSubtotal);
        button.setOnClickListener(v -> new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            remakeadapter();
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show());
        return view;
    }

    private void remakeadapter() {
        int maxstatus = databaseConnectionAdapter.getPersonStatus(getContext().getSharedPreferences("innerlogin", MODE_PRIVATE).getInt("id", -1));;
        int maxstatusf;
        if (maxstatus == 5) maxstatusf = 5;
        else if (maxstatus == 4) maxstatusf = 3;
        else maxstatusf = 0;
        Thread thread = new Thread(()->
                recyclerView.post(()-> recyclerView.setAdapter(new RAPersona(getContext(), databaseConnectionAdapter.getPersonasData(new Date(calendar.getTime().getTime()), maxstatusf), connection, new Date(calendar.getTime().getTime())))));
        thread.start();
    }

    public FragmentSubtotal(Connection connection) {
        this.connection = connection;
        databaseConnectionAdapter = new DatabaseConnectionAdapter(connection);
    }

    private void init() {
        recyclerView = view.findViewById(R.id.rvSubtotal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        remakeadapter();
    }
}