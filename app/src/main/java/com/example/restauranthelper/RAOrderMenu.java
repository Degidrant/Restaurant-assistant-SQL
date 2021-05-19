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
import com.example.restauranthelper.objects.COrder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class RAOrderMenu extends RecyclerView.Adapter<RAOrderMenu.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<CDish> dishes;
    private final DatabaseConnectionAdapter adapter;
    private final Fragment fragment;
    private final RAOptions.ClickListener clickListener;
    private final ArrayList<COrder> order;
    private final CallbackOne callbackOne;

    RAOrderMenu(Context context, List<CDish> dishes, Connection connection, Fragment fragment, RAOptions.ClickListener clickListener, ArrayList<COrder> orderClasses) {
        this.dishes = dishes;
        this.inflater = LayoutInflater.from(context);
        adapter = new DatabaseConnectionAdapter(connection);
        this.fragment = fragment;
        this.clickListener = clickListener;
        this.order = orderClasses;
        callbackOne = (CallbackOne) fragment;
    }
    @Override
    public RAOrderMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_menu, parent, false);
        return new RAOrderMenu.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RAOrderMenu.ViewHolder holder, int position) {
        CDish dish = dishes.get(position);
        holder.name.setText(dish.getName());
        holder.price.setText(dish.getPrice() + " " + inflater.getContext().getString(R.string.currency));
        holder.weight.setText(dish.getWeight() + " " + inflater.getContext().getString(R.string.mass));
        holder.add.setOnClickListener(v -> {
            DialogueOptions dialog = new DialogueOptions(adapter.connection, dish.getID(), clickListener);
            if (dialog.shouldBeCreated()) dialog.show(fragment.getActivity().getSupportFragmentManager(), "order");
            else commitOrderOne(dish);
        });
        AnimationDrawable animation = (AnimationDrawable) holder.imageView.getDrawable();
        animation.start();
        Runnable runnable = () -> {
            Bitmap bitmap = getBitmap(adapter.getMyBase(dish.getID()));
            holder.imageView.post(
                    () -> holder.imageView.setImageBitmap(bitmap)
            );
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void commitOrderOne(CDish dish) {
        for (COrder i : order)
            if (i.getDishId() == dish.getID()) {
                i.add();
                callbackOne.createCheckOrder();
                return;
            }
        order.add(new COrder(dish.getID(), -1, 1));
        callbackOne.createCheckOrder();
    }

    private Bitmap getBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public int getItemCount() {
       if (dishes!=null)  return  dishes.size();
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

    public interface CallbackOne{
        void createCheckOrder();
    }

}

