package com.example.restauranthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.COrder;

import java.util.List;

public class RAOrderCheck extends RecyclerView.Adapter<RAOrderCheck.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<COrder> orders;
    private final DatabaseConnectionAdapter adapter;
    private final Callback2 callback;

    RAOrderCheck(Context context, List<COrder> orders, DatabaseConnectionAdapter adapter, Fragment fragment) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        this.adapter = adapter;
        if (fragment!= null) callback = (Callback2) fragment;
            else callback = null;
    }
    @Override
    public RAOrderCheck.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.adapter_check_order, parent, false);
        return new RAOrderCheck.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RAOrderCheck.ViewHolder holder, int position) {
        COrder order = orders.get(position);
       holder.name.setText(adapter.getDishNameByID(order.getDishId()));
       holder.count.setText(String.valueOf(order.getCount()));
       holder.option.setText(adapter.getOptionNameByID(order.getOptionId(), inflater.getContext()));
       if (adapter.getOptionNameByID(order.getOptionId(), inflater.getContext()).matches("")) holder.option.setVisibility(View.GONE);
       holder.delete.setOnClickListener(v -> {
           orders.remove(order);
           if (callback!= null) callback.createCheckOrder();
       });
       if (callback == null) holder.delete.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, count, option;
        final ImageButton delete;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.textViewOrderCheckMain);
            count = view.findViewById(R.id.textViewOrderCheckCount);
            option = view.findViewById(R.id.textViewOrderCheckOption);
            delete = view.findViewById(R.id.imageButtonDeleteCheck);
        }
    }

    public interface Callback2{
        void createCheckOrder();
    }
}
