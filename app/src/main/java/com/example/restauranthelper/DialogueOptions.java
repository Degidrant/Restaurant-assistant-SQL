package com.example.restauranthelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.COption;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.List;

public class DialogueOptions extends DialogFragment {
    private final DatabaseConnectionAdapter adapter;
    private View view;
    private final List<COption> optionClassList;
    private final RAOptions.ClickListener clickListener;

    public DialogueOptions(Connection connection, int dishId, RAOptions.ClickListener clickListener) {
        adapter = new DatabaseConnectionAdapter(connection);
        optionClassList = adapter.getOptions(dishId);
        this.clickListener = clickListener;
    }

    private void abort() {
        this.dismiss();
    }

    public void onAttach(@NotNull Context context){
        super.onAttach(context);
    }

    public boolean shouldBeCreated(){
        return optionClassList.size() != 0;
    }


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.dialogue_layout_options, null);
        builder.setView(view);
        setRecycler();
        return builder.create();
    }

    private void setRecycler() {
        RecyclerView main = view.findViewById(R.id.recyclerViewDialogueOptions);
        main.setLayoutManager(new LinearLayoutManager(getActivity()));
        RAOptions mainAdapter = new RAOptions(getActivity(), optionClassList, adapter, this, clickListener);
        main.setAdapter(mainAdapter);
    }
}
