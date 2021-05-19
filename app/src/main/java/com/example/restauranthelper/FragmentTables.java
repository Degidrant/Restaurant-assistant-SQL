package com.example.restauranthelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.CTable;

import java.sql.Connection;
import java.util.ArrayList;


public class FragmentTables extends Fragment {
    Connection connection;
    DatabaseConnectionAdapter databaseConnectionAdapter;
    View view;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_only_rv, container, false);
        init();
        return view;
    }


    public FragmentTables(Connection connection) {
        this.connection = connection;
        databaseConnectionAdapter = new DatabaseConnectionAdapter(connection);
    }

    private void init() {
        recyclerView = view.findViewById(R.id.rvUni);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        recyclerView.setAdapter(null);
        Thread thread = new Thread(()-> {
            int tablecount = databaseConnectionAdapter.howmuchtables();
            ArrayList<CTable> tables = databaseConnectionAdapter.getTables(tablecount);
            recyclerView.post(() -> recyclerView.setAdapter(new RATables(getContext(), tables , connection)));
        });
        thread.start();
    }
}
