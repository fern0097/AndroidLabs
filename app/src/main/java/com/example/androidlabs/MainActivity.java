package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_relative);

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
//
            Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
        });

        final Switch mySwitch = findViewById(R.id.switch5);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Snackbar sb = Snackbar.make(MainActivity.this.findViewById(R.id.rootLayout), getResources().getString(R.string.switch_on), Snackbar.LENGTH_LONG);
                    sb.setAction(getResources().getString(R.string.undo), v -> mySwitch.setChecked(false));
                    sb.show();
                } else {
                    Snackbar sb = Snackbar.make(MainActivity.this.findViewById(R.id.rootLayout), getResources().getString(R.string.switch_off), Snackbar.LENGTH_LONG);
                    sb.setAction(getResources().getString(R.string.undo), v -> mySwitch.setChecked(true));
                    sb.show();
                }
            }
        });
    }
}