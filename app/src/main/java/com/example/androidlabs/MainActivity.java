package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
//                Toast.makeText(MainActivity.this, R.string.toast_message, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
        });

        Switch mySwitch = findViewById(R.id.switch5);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar sb = Snackbar.make(MainActivity.this.findViewById(R.id.rootLayout), getResources().getString(R.string.switch_on), Snackbar.LENGTH_LONG);
                    sb.setAction("Undo", v -> mySwitch.setChecked(false));
                    sb.show();
                } else {
                    Snackbar sb = Snackbar.make(MainActivity.this.findViewById(R.id.rootLayout), "The switch is off now", Snackbar.LENGTH_LONG);
                    sb.setAction("Undo", v -> mySwitch.setChecked(true));
                    sb.show();
                }
            }
        });


        //EditText theText = findViewById(R.id.editText);
        //String userTyped = theText.getText().toString();

    }
}