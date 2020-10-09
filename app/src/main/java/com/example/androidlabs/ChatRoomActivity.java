package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private MyListAdapter myAdapter;
    private List<Message> chatMessagesList = new ArrayList<>();
    private Button sendButton, receiveButton;
    private EditText sdRVEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView messageList = findViewById(R.id.listView);
        myAdapter = new MyListAdapter();
        messageList.setAdapter(myAdapter);


        sendButton = findViewById(R.id.sendButton);
        receiveButton = findViewById(R.id.receiveButton);
        sdRVEditText = findViewById(R.id.sdRVEditText);

        sendButton.setOnClickListener(v -> {
            chatMessagesList.add(new Message(sdRVEditText.getText().toString(), false));
            myAdapter.notifyDataSetChanged();
            sdRVEditText.setText("");
        });

        receiveButton.setOnClickListener(v -> {
            chatMessagesList.add(new Message(sdRVEditText.getText().toString(), true));
            myAdapter.notifyDataSetChanged();
            sdRVEditText.setText("");
        });

        messageList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatRoomActivity.this);
                alertDialogBuilder.setTitle(R.string.are_you_sure_delete);
                alertDialogBuilder.setMessage(getString(R.string.selected_row) + position + "\n" + getString(R.string.database_id_is) + id);
                alertDialogBuilder.setPositiveButton(R.string.y, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chatMessagesList.remove(position);
                        myAdapter.notifyDataSetChanged();
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.n, null);
                alertDialogBuilder.show();

                return false;
            }
        });

    }

    class Message {

        private String chatMessage;
        private boolean isReceived;

        public Message(String chatMessage, boolean isReceived) {
            this.chatMessage = chatMessage;
            this.isReceived = isReceived;
        }

        public String getChatMessage() {
            return chatMessage;
        }

        public void setChatMessage(String chatMessage) {
            this.chatMessage = chatMessage;
        }

        public boolean isReceived() {
            return isReceived;
        }

        public void setReceived(boolean received) {
            isReceived = received;
        }
    }

    class MyListAdapter extends BaseAdapter {

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
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            Message m = getItem(position);
            if (m.isReceived()) {
                v = getLayoutInflater().inflate(R.layout.receiver_layout, parent, false);
            } else {
                v = getLayoutInflater().inflate(R.layout.sender_layout, parent, false);
            }

            TextView chatMessage = v.findViewById(R.id.chatMessageTV);
            chatMessage.setText(m.getChatMessage());

            return v;
        }
    }
}