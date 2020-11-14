package com.example.androidlabs;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private MyListAdapter myAdapter;
    private List<Message> chatMessagesList = new ArrayList<>();
    private Button sendButton, receiveButton;
    private EditText sdRVEditText;
    private SQLiteDatabase db;
    private boolean isTablet;

    private DetailsFragment dFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView messageList = findViewById(R.id.listView);
        myAdapter = new MyListAdapter(ChatRoomActivity.this, chatMessagesList);
        messageList.setAdapter(myAdapter);

        // load messages from database
        loadDataFromDatabase();
        myAdapter.notifyDataSetChanged();
        sendButton = findViewById(R.id.sendButton);
        receiveButton = findViewById(R.id.receiveButton);
        sdRVEditText = findViewById(R.id.sdRVEditText);

        // Tablet view by Id
        isTablet = findViewById(R.id.fragmentContainer) != null;

        sendButton.setOnClickListener(v -> {
            Message m = new Message(sdRVEditText.getText().toString(), false);

            // Add message to database
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, m.getChatMessage());
            newRowValues.put(MyOpener.COL_IS_RECEIVED, 0);

            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            m.setId(newId);

            chatMessagesList.add(m);

            myAdapter.notifyDataSetChanged();
            sdRVEditText.setText("");
        });

        receiveButton.setOnClickListener(v -> {
            Message m = new Message(sdRVEditText.getText().toString(), true);

            // Add message to database
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, m.getChatMessage());
            newRowValues.put(MyOpener.COL_IS_RECEIVED, 1);

            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            m.setId(newId);

            chatMessagesList.add(m);

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

                        // Remove message from database
                        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[]{Long.toString(id)});

                        chatMessagesList.remove(position);
                        myAdapter.notifyDataSetChanged();

                        // remove the fragment if it is tablet
                        if (isTablet && dFragment != null) {
                            getSupportFragmentManager().beginTransaction().remove(dFragment).commit();
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton(R.string.n, null);
                alertDialogBuilder.show();

                return true;
            }
        });

        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Message message = chatMessagesList.get(position);
                //Create a bundle to pass data to the new fragment
                Bundle dataToPass = new Bundle();
                dataToPass.putLong("ID", message.getId());
                dataToPass.putString("MSG", message.getChatMessage());
                dataToPass.putBoolean("RECEIVED", message.isReceived());

                if (isTablet) {
                    dFragment = new DetailsFragment(); //add a DetailFragment
                    dFragment.setArguments(dataToPass); //pass it a bundle for information
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, dFragment) //Add the fragment in FrameLayout
                            .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
                } else {//isPhone

                    Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivity(nextActivity); //make the transition
                }
            }
        });
    }

    private void loadDataFromDatabase() {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_IS_RECEIVED};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);
        int msgColumnIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int isReceivedColIndex = results.getColumnIndex(MyOpener.COL_IS_RECEIVED);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            long id = results.getLong(idColIndex);
            String msg = results.getString(msgColumnIndex);
            int isREceivedInt = results.getInt(isReceivedColIndex);

            Message m = new Message(id, msg, isREceivedInt == 1);

            //add the new Contact to the array list:
            chatMessagesList.add(m);
        }

        printCursor(results, MyOpener.VERSION_NUM);

        //At this point, the contactsList array has loaded every row from the cursor.
    }

    private void printCursor(Cursor c, int version) {
//        •	The database version number, use db.getVersion() for the version number.
//        •	The number of columns in the cursor.
//        •	The name of the columns in the cursor.
//        •	The number of rows in the cursor
//        •	Print out each row of results in the cursor.

        Log.d("printCursor", "version number: " + version);
        Log.d("printCursor", "The number of columns in the cursor: " + c.getColumnCount());

        for (String columnName : c.getColumnNames()) {
            Log.d("printCursor", "Column: " + columnName);
        }

        Log.d("printCursor", "The number of rows in the cursor: " + c.getCount());

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = c.getColumnIndex(MyOpener.COL_ID);
        int msgColumnIndex = c.getColumnIndex(MyOpener.COL_MESSAGE);
        int isReceivedColIndex = c.getColumnIndex(MyOpener.COL_IS_RECEIVED);

        //iterate over the results, return true if there is a next item:
        c.moveToPosition(-1);
        while (c.moveToNext()) {
            long id = c.getLong(idColIndex);
            String msg = c.getString(msgColumnIndex);
            int isREceivedInt = c.getInt(isReceivedColIndex);

            Message m = new Message(id, msg, isREceivedInt == 1);

            Log.d("printCursor", "ID: " + id + ", MESSAGE: " + msg + ", IS_RECEIVED: " + isREceivedInt);
        }

    }

}