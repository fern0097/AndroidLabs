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


        mySharedPreferences = getSharedPreferences("MyEmailPassword", MODE_PRIVATE);

        inputText.setText(mySharedPreferences.getString("key_email", ""));

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileActivity();
            }
        });

        }

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

        SharedPreferences.Editor edit = mySharedPreferences.edit();
        edit.putString("key_email", inputText.getText().toString());
        edit.commit();

    }
}