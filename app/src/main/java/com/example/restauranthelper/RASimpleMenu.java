package com.example.restauranthelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.CDish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.util.List;

public class RASimpleMenu extends RecyclerView.Adapter<RASimpleMenu.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<CDish> dishes;
    private final DatabaseConnectionAdapter adapter;
    private final Fragment fragment;

    RASimpleMenu(Context context, List<CDish> dishes, Connection connection, Fragment fragment) {
        this.dishes = dishes;
        this.inflater = LayoutInflater.from(context);
        adapter = new DatabaseConnectionAdapter(connection);
        this.fragment = fragment;
    }
    @Override
    public RASimpleMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.adapter_menu, parent, false);
        return new RASimpleMenu.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RASimpleMenu.ViewHolder holder, int position) {
        CDish dish = dishes.get(position);
        holder.name.setText(dish.getName());
        holder.add.setVisibility(View.GONE);
        holder.add.setImageDrawable(inflater.getContext().getDrawable(R.drawable.ic_more));
        holder.price.setText(dish.getPrice() + " " + inflater.getContext().getString(R.string.currency));
        holder.weight.setText(dish.getWeight() + " " + inflater.getContext().getString(R.string.mass));
        AnimationDrawable animation = (AnimationDrawable) holder.imageView.getDrawable();
        animation.start();

        holder.add.setOnClickListener(v -> {
            DialogueOptions dialog = new DialogueOptions(adapter.connection, dish.getID(), null);
            dialog.show(fragment.getActivity().getSupportFragmentManager(), "options");
        });
        Thread thread = new Thread(() -> {
            Bitmap bitmap = getBitmap(adapter.getMyBase(dish.getID()));
            holder.add.post(() -> {
                if (adapter.getOptions(dish.getID()).size() != 0) holder.add.setVisibility(View.VISIBLE);
            });
            holder.imageView.post(
                    () -> holder.imageView.setImageBitmap(bitmap)
            );
        });
        thread.start();
    }

    private Bitmap getBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public int getItemCount() {
        if (dishes!=null) return dishes.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView name, price, weight;
        final FloatingActionButton add;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.imageViewIcoSimple);
            name = view.findViewById(R.id.textViewSimpleName);
            price = view.findViewById(R.id.textViewPriceSimple);
            weight = view.findViewById(R.id.textViewWeightSimple);
            add = view.findViewById(R.id.fabAdd);
        }
    }
}
