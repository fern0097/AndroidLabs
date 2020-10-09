package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mySharedPreferences;
    private EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);
        inputText = findViewById(R.id.emailText);
        mySharedPreferences = getSharedPreferences("MyEmail", MODE_PRIVATE); //file is been created
        inputText.setText(mySharedPreferences.getString("key_email", ""));

        // Button transfer to ProfileActivity
        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileActivity();
            }
        });

    }

    // This method saves the String from editText1 and transfer into ProfileActivity
    public void openProfileActivity(){
        EditText editText1 = (EditText) findViewById(R.id.emailText);
        String text = editText1.getText().toString();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("EMAIL", text);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor edit = mySharedPreferences.edit(); // Editing the file
        edit.putString("key_email", inputText.getText().toString()); // Inserting the String in the file
        edit.commit(); // Saving the String into the file

    }
}