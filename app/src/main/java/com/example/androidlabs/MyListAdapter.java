package com.example.androidlabs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

class MyListAdapter extends BaseAdapter {

    private ChatRoomActivity chatRoomActivity;
    private List<Message> chatMessagesList;

    public MyListAdapter(ChatRoomActivity chatRoomActivity, List<Message> chatMessagesList) {
        this.chatRoomActivity = chatRoomActivity;

        this.chatMessagesList = chatMessagesList;
    }

    @Override
    public int getCount() {
        return chatMessagesList.size();
    }

    @Override
    public Message getItem(int position) {
        return chatMessagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        Message m = chatMessagesList.get(position);
        return m.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        Message m = getItem(position);
        if (m.isReceived()) {
            v = chatRoomActivity.getLayoutInflater().inflate(R.layout.receiver_layout, parent, false);
        } else {
            v = chatRoomActivity.getLayoutInflater().inflate(R.layout.sender_layout, parent, false);
        }

        TextView chatMessage = v.findViewById(R.id.chatMessageTV);
        chatMessage.setText(m.getChatMessage());

        return v;
    }
}