package com.example.restauranthelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restauranthelper.objects.CMessage;

import java.util.List;

public class RAChat extends RecyclerView.Adapter<RAChat.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<CMessage> messages;

    RAChat(Context context, List<CMessage> messages) {
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public RAChat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.adapter_chat, parent, false);
        return new RAChat.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RAChat.ViewHolder holder, int position) {
        CMessage message = messages.get(position);
        holder.name.setText(message.getName());
        holder.message.setText(message.getMessage());
        boolean showdate = PreferenceManager.getDefaultSharedPreferences(inflater.getContext()).getBoolean("date", true);
        if (showdate) holder.datetime.setText(String.format(inflater.getContext().getString(R.string.datetime), message.getTime(), message.getDate()));
            else holder.datetime.setText(message.getTime().toString());

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, message, datetime;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.textViewChatName);
            message = view.findViewById(R.id.textViewChatMessage);
            datetime = view.findViewById(R.id.textViewChatDateTime);
        }
    }

}
