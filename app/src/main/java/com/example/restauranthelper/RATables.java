package com.example.restauranthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.CTable;

import java.sql.Connection;
import java.util.List;

public class RATables extends RecyclerView.Adapter<RATables.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<CTable> tables;
    private final DatabaseConnectionAdapter adapter;

    RATables(Context context, List<CTable> tables, Connection connection) {
        this.tables = tables;
        this.inflater = LayoutInflater.from(context);
        adapter = new DatabaseConnectionAdapter(connection);
    }
    @Override
    public RATables.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.adapter_tables, parent, false);
        return new RATables.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RATables.ViewHolder holder, int position) {
        CTable table = tables.get(position);
        holder.num.setText(String.valueOf(table.getID()));
        switch (table.getStatus()){
            case 2: holder.table.setColorFilter(ContextCompat.getColor(inflater.getContext(), R.color.mint)); break;
            case 1: holder.table.setColorFilter(ContextCompat.getColor(inflater.getContext(), R.color.mainorange)); break;
            case 0: holder.table.setColorFilter(ContextCompat.getColor(inflater.getContext(), R.color.gray)); break;
        }
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView num;
        final ImageView table;
        ViewHolder(View view){
            super(view);
            num = view.findViewById(R.id.textViewTableNum);
            table = view.findViewById(R.id.imageViewTable);
        }
    }
}
