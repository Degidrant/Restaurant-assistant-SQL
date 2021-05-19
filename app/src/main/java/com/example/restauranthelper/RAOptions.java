package com.example.restauranthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.COption;

import java.util.List;

public class RAOptions extends RecyclerView.Adapter<RAOptions.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<COption> options;
    private final DatabaseConnectionAdapter adapter;
    private final DialogueOptions dialog;
    private final ClickListener clickListener;

    RAOptions(Context context, List<COption> options, DatabaseConnectionAdapter adapter, DialogueOptions dialog, ClickListener clickListener) {
        this.options = options;
        this.inflater = LayoutInflater.from(context);
        this.adapter = adapter;
        this.dialog = dialog;
        this.clickListener = clickListener;
    }
    @Override
    public RAOptions.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.adapter_dialogue_options, parent, false);
        return new RAOptions.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RAOptions.ViewHolder holder, int position) {
        COption option = options.get(position);
        holder.name.setText(option.getName());
        holder.price.setText(String.format(inflater.getContext().getString(R.string.addprice), option.getPrice(), inflater.getContext().getString(R.string.currency)));
        holder.itemView.setOnClickListener(v -> {
            if (clickListener!= null) clickListener.onOptionClick(adapter.getOptionParentID(option.getID()), option.getID(), dialog);
        });
        if (option.getPrice() == 0) holder.price.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return options.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, price;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.textViewOptionName);
            price = view.findViewById(R.id.textViewOptionPrice);
        }
    }

    interface ClickListener {
        void onOptionClick(int dishId, int optionId, DialogueOptions dialog);
    }
}
