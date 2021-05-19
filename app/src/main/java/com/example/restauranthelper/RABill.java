package com.example.restauranthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.CBill;

import java.util.List;

public class RABill extends RecyclerView.Adapter<RABill.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<CBill> bills;
    private final DatabaseConnectionAdapter adapter;

    RABill(Context context, List<CBill> bills, DatabaseConnectionAdapter adapter) {
        this.bills = bills;
        this.inflater = LayoutInflater.from(context);
        this.adapter = adapter;
    }
    @Override
    public RABill.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_bill, parent, false);
        return new RABill.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RABill.ViewHolder holder, int position) {
        CBill bill = bills.get(position);
        holder.billnum.setText(String.format(inflater.getContext().getString(R.string.billnum), bill.getID()));
        holder.timetw.setText(bill.getTime().toString());
        holder.datetw.setText(bill.getDate().toString());
        holder.table.setText(String.valueOf(bill.getTableID()));
        Thread read = new Thread(()-> {
            holder.person.post(()-> holder.person.setText(adapter.getPersonString(bill.getPersonID())));
            holder.orders.post(()->holder.orders.setText(adapter.getOrdersString(bill.getID())));
        });
        read.start();
        holder.sum.setText(bill.getSum() + " " + inflater.getContext().getString(R.string.currency));
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView billnum, sum, person, table, orders, datetw, timetw;
        ViewHolder(View view){
            super(view);
            billnum = view.findViewById(R.id.textViewBillID);
            sum = view.findViewById(R.id.textViewBillSum);
            person = view.findViewById(R.id.textViewBillPersona);
            table = view.findViewById(R.id.textViewBillTable);
            orders = view.findViewById(R.id.textViewBillOrders);
            datetw = view.findViewById(R.id.textViewBillDate);
            timetw = view.findViewById(R.id.textViewBillTime);
        }
    }
}