package com.example.restauranthelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restauranthelper.objects.CDish;
import java.sql.Connection;
import java.util.ArrayList;

public class FragmentMenu extends Fragment {
    Connection connection;
    DatabaseConnectionAdapter databaseConnectionAdapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        setRecyclers();
        return view;
    }

    public FragmentMenu(Connection connection) {
        this.connection = connection;
        databaseConnectionAdapter = new DatabaseConnectionAdapter(connection);
    }

    private void setRecyclers() {
        ConstraintLayout order = view.findViewById(R.id.cstOrder);
        order.setVisibility(View.GONE);
        flexTreads();
        goneForSimpleMenu();
    }

    private void goneForSimpleMenu() {
        TextView textView = view.findViewById(R.id.textViewOrderHint);
        EditText editText = view.findViewById(R.id.etComment);
        EditText editText1 = view.findViewById(R.id.etTable);
        Button button = view.findViewById(R.id.buttonFinalizeOrder);
        editText.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        editText1.setVisibility(View.GONE);
    }

    private void flexTreads() {
        Thread mainThread, desertThread, snackThread, drinkThread;
        RecyclerView main = view.findViewById(R.id.recyclerViewMainDishes);
        main.setLayoutManager(new GridLayoutManager(getContext(), 3));
        RecyclerView desert = view.findViewById(R.id.recyclerViewDeserts);
        desert.setLayoutManager(new GridLayoutManager(getContext(), 3));
        RecyclerView snacks = view.findViewById(R.id.recyclerViewSnacks);
        snacks.setLayoutManager(new GridLayoutManager(getContext(), 3));
        RecyclerView drinks = view.findViewById(R.id.recyclerViewDrinks);
        drinks.setLayoutManager(new GridLayoutManager(getContext(), 3));
        drinkThread = new Thread(()->{
            ArrayList<CDish> dishes = databaseConnectionAdapter.getDishesByGroup(getString(R.string.group4));
            RASimpleMenu DrinkAdapter = new RASimpleMenu(getContext(), dishes, connection, this);
            drinks.post(()-> drinks.setAdapter(DrinkAdapter));
        });
        snackThread = new Thread(()->{
            ArrayList<CDish> dishes = databaseConnectionAdapter.getDishesByGroup(getString(R.string.group3));
            RASimpleMenu SnacksAdapter = new RASimpleMenu(getContext(), dishes, connection, this);
            snacks.post(()-> snacks.setAdapter(SnacksAdapter));
            drinkThread.start();
        });
        desertThread = new Thread(()->{
            ArrayList<CDish> dishes = databaseConnectionAdapter.getDishesByGroup(getString(R.string.group2));
            RASimpleMenu DesertAdapter = new RASimpleMenu(getContext(), dishes, connection, this);
            desert.post(()-> desert.setAdapter(DesertAdapter));
            snackThread.start();
        });
        mainThread = new Thread(()->{
            ArrayList<CDish> dishes = databaseConnectionAdapter.getDishesByGroup(getString(R.string.group1));
            RASimpleMenu MainAdapter = new RASimpleMenu(getContext(), dishes, connection, this);
            main.post(()-> main.setAdapter(MainAdapter));
            desertThread.start();
        });
            mainThread.start();

    }
}
