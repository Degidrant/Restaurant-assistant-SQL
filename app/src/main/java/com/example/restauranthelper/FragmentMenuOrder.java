package com.example.restauranthelper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.CDish;
import com.example.restauranthelper.objects.COrder;

import java.sql.Connection;
import java.util.ArrayList;

public class FragmentMenuOrder extends Fragment implements RAOrderMenu.CallbackOne, RAOrderCheck.Callback2 {
    Connection connection;
    DatabaseConnectionAdapter databaseConnectionAdapter;
    View view;
    ArrayList<COrder> order = new ArrayList<>();
    EditText comment, tableID;
    Button commit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        setRecyclers();
        init();
        return view;
    }

    private void init() {
        comment = view.findViewById(R.id.etComment);
        commit = view.findViewById(R.id.buttonFinalizeOrder);
        commit.setOnClickListener(v -> {
            if (!tableID.getEditableText().toString().matches("")) {
                databaseConnectionAdapter.commitOrder(order, comment.getEditableText().toString(), Integer.parseInt(tableID.getEditableText().toString()),
                        getContext().getSharedPreferences("innerlogin", Context.MODE_PRIVATE).getInt("id", -1), getContext());
                order.clear();
                tableID.clearFocus();
                tableID.setText("");
                comment.setText("");
                comment.clearFocus();
                createCheckOrder();
                HardController.hideKeyboardFrom(getContext(), getView());
            }
            else Toast.makeText(getContext(), getString(R.string.tablealert), Toast.LENGTH_SHORT).show();
        });
        tableID = view.findViewById(R.id.etTable);
    }

    public FragmentMenuOrder(Connection connection) {
        this.connection = connection;
        databaseConnectionAdapter = new DatabaseConnectionAdapter(connection);
    }

    private void setRecyclers() {
        //слушатель на нажатия RV для обработки заказа
        RAOptions.ClickListener clickListener = (dishID, optionID, dialog) -> {
            createCheckOrder();
           for (COrder i : order)
                if (i.getDishId() == dishID && i.getOptionId() == optionID) {
                    i.add();
                    dialog.dismiss();
                    return;
                }
            order.add(new COrder(dishID, optionID, 1));
            dialog.dismiss();
            createCheckOrder();
        };
        flexTreads(clickListener);
    }

    private void flexTreads(RAOptions.ClickListener clickListener) {
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
            RAOrderMenu DrinkAdapter = new RAOrderMenu(getContext(), dishes, connection, this, clickListener, order);
            drinks.post(()-> drinks.setAdapter(DrinkAdapter));
        });
        snackThread = new Thread(()->{
            ArrayList<CDish> dishes = databaseConnectionAdapter.getDishesByGroup(getString(R.string.group3));
            RAOrderMenu SnacksAdapter = new RAOrderMenu(getContext(), dishes, connection, this, clickListener, order);
            snacks.post(()-> snacks.setAdapter(SnacksAdapter));
            drinkThread.start();
        });
        desertThread = new Thread(()->{
            ArrayList<CDish> dishes = databaseConnectionAdapter.getDishesByGroup(getString(R.string.group2));
            RAOrderMenu DesertAdapter = new RAOrderMenu(getContext(), dishes, connection, this, clickListener, order);
            desert.post(()-> desert.setAdapter(DesertAdapter));
            snackThread.start();
        });
        mainThread = new Thread(()->{
            ArrayList<CDish> dishes = databaseConnectionAdapter.getDishesByGroup(getString(R.string.group1));
            RAOrderMenu MainAdapter = new RAOrderMenu(getContext(), dishes, connection, this, clickListener, order);
            main.post(()-> main.setAdapter(MainAdapter));
            desertThread.start();
        });
        mainThread.start();
    }

    public void createCheckOrder() {
        RecyclerView checkOrder = view.findViewById(R.id.recyclerViewCheckOrder);
        checkOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        checkOrder.setAdapter(new RAOrderCheck(getContext(), order, databaseConnectionAdapter, this));
    }
}