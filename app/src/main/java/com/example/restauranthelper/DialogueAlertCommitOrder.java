package com.example.restauranthelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DialogueAlertCommitOrder extends DialogFragment {
    private final ArrayList<String> orders;
    private final DatabaseConnectionAdapter adapter;
    private final int table;
    private final CallbackCommit callbackCommit;
    private final Context context;

    public DialogueAlertCommitOrder(ArrayList<String> orders, DatabaseConnectionAdapter adapter, int table, Fragment fragment) {
        this.orders = orders;
        this.adapter = adapter;
        this.table = table;
        context = fragment.getContext();
        callbackCommit = (CallbackCommit) fragment;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.commitbill));
        StringBuilder message = new StringBuilder(getString(R.string.sureorders));
        for (String s : orders){
            message.append(s);
            message.append(", ");
        }
        message.delete(message.length()-2, message.length());
        message.append("?");
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> {
            int sum = 0;
            for (String s : orders) sum += adapter.countSum(Integer.parseInt(s));
            Toast.makeText(context, String.format(getString(R.string.billformed), adapter.assignBill(table, sum, getContext().getSharedPreferences("innerlogin", Context.MODE_PRIVATE).getInt("id", -1))), Toast.LENGTH_SHORT).show();
            for (String s : orders) adapter.addOneStep(Integer.parseInt(s));
            callbackCommit.createAdapter();
        });
        builder.setMessage(message);
        builder.setNegativeButton(getString(R.string.no), null);
        return builder.create();
    }

    interface CallbackCommit{
        void createAdapter();
    }

}
