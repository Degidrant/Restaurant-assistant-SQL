package com.example.restauranthelper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.CBill;

import java.sql.Connection;
import java.util.ArrayList;

public class FragmentBills extends Fragment {
    Connection connection;
    DatabaseConnectionAdapter databaseConnectionAdapter;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_only_rv, container, false);
        createRV();
        return view;
    }

    public FragmentBills(Connection connection) {
        this.connection = connection;
        databaseConnectionAdapter = new DatabaseConnectionAdapter(connection);
    }

    private void createRV() {
        RecyclerView main = view.findViewById(R.id.rvUni);
        main.setLayoutManager(new LinearLayoutManager(getContext()));
        Thread thread = new Thread(() -> {
            int privateAccess = databaseConnectionAdapter.getPersonStatus(getContext().getSharedPreferences("innerlogin", Context.MODE_PRIVATE).getInt("id", -1));
            ArrayList<CBill> bills;
            if (privateAccess > 3) bills = databaseConnectionAdapter.getBills();
            else bills = databaseConnectionAdapter.getBills(getContext().getSharedPreferences("innerlogin", Context.MODE_PRIVATE).getInt("id", -1));
            main.post(()-> main.setAdapter((new RABill(getContext(), bills , databaseConnectionAdapter))));
        });
       thread.start();

    }
}