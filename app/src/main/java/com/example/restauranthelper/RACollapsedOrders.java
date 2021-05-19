package com.example.restauranthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.COrderMain;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RACollapsedOrders extends RecyclerView.Adapter<RACollapsedOrders.ViewHolder>{

    private final LayoutInflater inflater;
    private final DatabaseConnectionAdapter adapter;
    private final List<COrderMain> orders;
    private final CallbackOrders callbackOrders;
    private final Fragment fragment;

    RACollapsedOrders(Context context, List<COrderMain> orders, Connection connection, Fragment fragment) {
        this.inflater = LayoutInflater.from(context);
        this.orders = orders;
        this.fragment = fragment;
        adapter = new DatabaseConnectionAdapter(connection);
        callbackOrders = (CallbackOrders) fragment;
    }
    @Override
    public RACollapsedOrders.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.adapter_order_collapsed, parent, false);
        return new RACollapsedOrders.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RACollapsedOrders.ViewHolder holder, int position) {
        COrderMain order = orders.get(position);
        holder.name.setText(String.format(inflater.getContext().getString(R.string.orderexpand), order.getID()));
        holder.makeBill.setVisibility(View.GONE);
        int privateAccess = adapter.getPersonStatus(inflater.getContext().getSharedPreferences("innerlogin", Context.MODE_PRIVATE).getInt("id", -1));
        if (privateAccess == 1) holder.next.setVisibility(View.GONE);
        if (order.getStatus() == -1) holder.status.setText(inflater.getContext().getString(R.string.status00));
        if (order.getStatus() == 0) {
            holder.status.setText(inflater.getContext().getString(R.string.status1));
            if (privateAccess == 2) holder.next.setVisibility(View.GONE);
            if (privateAccess == 1) holder.next.setVisibility(View.VISIBLE);
        }
        if (order.getStatus() == 1) {
            if (privateAccess == 1) holder.next.setVisibility(View.GONE);
            holder.status.setText(inflater.getContext().getString(R.string.status2));
        }
        if (order.getStatus() == 2) holder.status.setText(inflater.getContext().getString(R.string.status3));
        if (order.getStatus() == 3) {
            holder.status.setText(inflater.getContext().getString(R.string.status4));
            holder.next.setVisibility(View.GONE);
            if (privateAccess != 1) holder.makeBill.setVisibility(View.VISIBLE);
        }
        holder.table.setText(String.valueOf(order.getTable()));
        holder.next.setOnClickListener(v -> {
            adapter.addOneStep(order.getID());
            callbackOrders.createAdapter();
        });
        holder.expand.setOnClickListener(v -> {
                DialogueExpand dialog = new DialogueExpand(adapter.connection, order);
                dialog.show(fragment.getActivity().getSupportFragmentManager(), "expand");
        });
        holder.makeBill.setOnClickListener(v -> {
            ArrayList<String> arrayList;
            if (adapter.isOrderFinished(order.getTable())) {
                arrayList = adapter.getTableOrders(order.getTable());
                DialogueAlertCommitOrder alertCommitOrder = new DialogueAlertCommitOrder(arrayList, adapter, order.getTable(), fragment);
                alertCommitOrder.show(fragment.getParentFragmentManager(), "confirm");
            }
            else Toast.makeText(fragment.getContext(), inflater.getContext().getString(R.string.notallorders), Toast.LENGTH_SHORT).show();

        });

    }


    @Override
    public int getItemCount() {
        return orders.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, status, table;
        final ImageView makeBill, next, expand;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.textViewOrderNum);
            status = view.findViewById(R.id.textViewOrderStatus);
            makeBill = view.findViewById(R.id.imageButtonBill);
            next = view.findViewById(R.id.imageButtonNext);
            expand = view.findViewById(R.id.imageButtonExpand);
            table = view.findViewById(R.id.textViewOrderTable);
        }
    }

    interface CallbackOrders{
        void createAdapter();
    }
}
