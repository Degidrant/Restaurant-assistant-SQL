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

import com.example.restauranthelper.objects.COrderMain;

import java.sql.Connection;
import java.util.ArrayList;

public class FragmentOrders extends Fragment implements RACollapsedOrders.CallbackOrders, DialogueAlertCommitOrder.CallbackCommit {
    Connection connection;
    DatabaseConnectionAdapter adapter;
    View view;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_only_rv, container, false);
        recyclerView = view.findViewById(R.id.rvUni);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        createAdapter();
        return view;
    }

    public FragmentOrders(Connection connection) {
        this.connection = connection;
        adapter = new DatabaseConnectionAdapter(connection);
    }


    @Override
    public void createAdapter() {
        Thread thread = new Thread(()->{
            int privateAccess = adapter.getPersonStatus(getContext().getSharedPreferences("innerlogin", Context.MODE_PRIVATE).getInt("id", -1));
            ArrayList<COrderMain> arrayList;
            if (privateAccess != 2)  arrayList = adapter.getOrders();
            else arrayList = adapter.getOrders(getContext().getSharedPreferences("innerlogin", Context.MODE_PRIVATE).getInt("id", -1));
            RACollapsedOrders RA = new RACollapsedOrders(getContext(), arrayList, connection, this);
            recyclerView.post(()-> recyclerView.setAdapter(RA));
        });
        thread.start();


    }
}