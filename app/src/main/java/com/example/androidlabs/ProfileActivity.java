package com.example.androidlabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> dispatchTakePictureIntent());

        Intent intent = getIntent();
        EditText email = findViewById(R.id.editText);
        email.setText(intent.getStringExtra("EMAIL"));

        Log.e(ACTIVITY_NAME, "In function: onCreate");

        // Button transfer to ChatroomActivity
        final Button button = findViewById(R.id.goToChat);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatRoomActivity();
            }
        });

        final Button goToWeatherForecast = findViewById(R.id.goToWeatherForecast);
        goToWeatherForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWeatherForecastActivity();
            }
        });


        final Button gotoToolbarPage = findViewById(R.id.gotoToolbar);
        gotoToolbarPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGotoToolbarActivity();
            }
        });
    }

    private void openChatRoomActivity() {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        startActivity(intent);

    }

    private void openWeatherForecastActivity() {
        Intent intent = new Intent(this, WeatherForecast.class);
        startActivity(intent);

    }

    private void openGotoToolbarActivity() {
        Intent intent = new Intent(this, TestToolbar.class);
        startActivityForResult(intent, 4563);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        }

        if (requestCode == 4563 && resultCode == 500) {
            finish();
        }
        Log.e(ACTIVITY_NAME, "In function: onActivityResult");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In function: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy");
    }
}