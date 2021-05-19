package com.example.restauranthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.CPerson;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class RAPersona extends RecyclerView.Adapter<RAPersona.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<CPerson> personas;
    private final DatabaseConnectionAdapter adapter;
    private final Date date;

    RAPersona(Context context, List<CPerson> personas, Connection connection, Date date) {
        this.personas = personas;
        this.inflater = LayoutInflater.from(context);
        adapter = new DatabaseConnectionAdapter(connection);
        this.date = date;
    }
    @Override
    public RAPersona.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.adapter_subtotal, parent, false);
        return new RAPersona.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RAPersona.ViewHolder holder, int position) {
        CPerson persona = personas.get(position);
        holder.name.setText(persona.getName());
        holder.date.setText(date.toString());
        holder.sum.setText(persona.getIncome() + " " + inflater.getContext().getString(R.string.currency));
    }
    @Override
    public int getItemCount() {
        return personas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, sum, date;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.textViewSubtotalName);
            sum = view.findViewById(R.id.textViewSubtotalSum);
            date = view.findViewById(R.id.textViewSubtotalDate);
        }
    }
}
