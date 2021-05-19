package com.example.restauranthelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.COrder;
import com.example.restauranthelper.objects.COrderMain;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.ArrayList;

public class DialogueExpand extends DialogFragment {
    private final DatabaseConnectionAdapter adapter;
    private View view;
    private final COrderMain orderData;
    private TextView date, time, person, comment;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    public DialogueExpand(Connection connection, COrderMain orderData) {
        adapter = new DatabaseConnectionAdapter(connection);
        this.orderData = orderData;
    }

    public void onAttach(@NotNull Context context){
        super.onAttach(context);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.dialogue_order, null);
        builder.setView(view);
        init();
        setValues();
        setRecycler();
        return builder.create();
    }

    @SuppressLint("SetTextI18n")
    private void setValues() {
        date.setText(orderData.getDate().toString());
        time.setText(orderData.getTime().toString());
        person.setText(String.format(getString(R.string.of1), adapter.getPersonString(orderData.getPerson())));
        if (orderData.getComment().matches("")) comment.setVisibility(View.GONE);
            else comment.setText(orderData.getComment());
        toolbar.setTitle(String.format(getString(R.string.orderexpand), orderData.getID()));
    }

    private void init() {
        date = view.findViewById(R.id.textViewDateExtended);
        time = view.findViewById(R.id.textViewTimeExtended);
        person = view.findViewById(R.id.textViewPersonExtended);
        toolbar = view.findViewById(R.id.toolbarOrderExpanded);
        comment = view.findViewById(R.id.textViewComExpanded);
        recyclerView = view.findViewById(R.id.rvOrderDataExtended);
    }

    private void setRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<COrder> orderMains = adapter.getSubsOrders(orderData.getID());
        recyclerView.setAdapter(new RAOrderCheck(getContext(), orderMains, adapter, null));
    }
}
