package com.example.restauranthelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restauranthelper.objects.CMessage;

import java.sql.Connection;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FragmentChat extends Fragment {
    Connection connection;
    DatabaseConnectionAdapter databaseConnectionAdapter;
    View view;
    RecyclerView recyclerView;
    ImageButton send;
    EditText message;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        init();
        createRV();
        return view;
    }

    private void init() {
        recyclerView = view.findViewById(R.id.rvChat);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        message = view.findViewById(R.id.etChatSend);
        send = view.findViewById(R.id.ibSendChat);
        send.setOnClickListener(v -> {
            if (!message.getEditableText().toString().matches("")) databaseConnectionAdapter.uploadmessage(message.getEditableText().toString(), getContext().getSharedPreferences("innerlogin", MODE_PRIVATE).getInt("id", -1));
            message.setText("");
            createRV();
        });
    }

    public FragmentChat(Connection connection) {
        this.connection = connection;
        databaseConnectionAdapter = new DatabaseConnectionAdapter(connection);
    }

    private void createRV() {
        Thread thread = new Thread(() -> {
            ArrayList<CMessage> adapterMessages= databaseConnectionAdapter.getMessages(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("chatnum", "200")));
            recyclerView.post(()-> recyclerView.setAdapter((new RAChat(getContext(), adapterMessages))));
        });
        thread.start();
    }
}
