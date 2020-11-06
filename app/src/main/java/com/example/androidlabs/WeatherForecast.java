package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    ImageView weatherImage;
    TextView textViewCurrentTemperature, textViewMinTemperature, textViewMaxTemperature, textViewUV;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherImage = findViewById(R.id.weatherImage);
        textViewCurrentTemperature = findViewById(R.id.textViewCurrentTemperature);
        textViewMinTemperature = findViewById(R.id.textViewMinTemperature);
        textViewMaxTemperature = findViewById(R.id.textViewMaxTemperature);
        textViewUV = findViewById(R.id.textViewUV);
        loading = findViewById(R.id.loading);

        loading.setVisibility(View.VISIBLE);

        new ForecastQuery().execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric", "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
    }

    class ForecastQuery extends AsyncTask<String, Integer, String> {

        String uv, min, max, currentTemp;
        Bitmap image;

        @Override
        protected String doInBackground(String... strings) { // var-args

            try {

                //create a URL object of what server to contact:
                URL url = new URL(strings[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();


                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8"); //response is data from the server

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if (xpp.getName().equals("temperature")) {
                            //If you get here, then you are pointing to a <Weather> start tag
                            currentTemp = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        } else if (xpp.getName().equals("weather")) {
                            String iconName = xpp.getAttributeValue(null, "icon");
                            String urlString = "http://openweathermap.org/img/w/" + iconName + ".png";

                            Log.i("WeatherImage", "Checking for file: " + iconName + ".png");
                            if (fileExistance(iconName + ".png")) {

                                Log.i("WeatherImage", "Found file on storage: " + iconName + ".png");

                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(iconName + ".png");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                image = BitmapFactory.decodeStream(fis);

                            } else {

                                Log.i("WeatherImage", "Downloading file: " + urlString);
                                downloadImage(urlString, iconName);
                            }


                            publishProgress(100);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }


            } catch (Exception e) {

            }

            try {

                //create a URL object of what server to contact:
                URL url = new URL(strings[1]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                uv = uvReport.getString("value");

            } catch (Exception e) {

            }

            return null;
        }

        private void saveImageToStorage(String iconName) throws IOException {
            FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        }

        private void downloadImage(String urlString, String iconName) throws IOException {
            URL url1 = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                image = BitmapFactory.decodeStream(connection.getInputStream());
            }
            saveImageToStorage(iconName);
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            textViewCurrentTemperature.setText(getString(R.string.current_temp) + currentTemp);
            textViewMinTemperature.setText(getString(R.string.min_temp) + min);
            textViewMaxTemperature.setText(getString(R.string.max_temp) + max);
            textViewUV.setText(getString(R.string.uv) + uv);

            weatherImage.setImageBitmap(image);

            loading.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            loading.setProgress(values[0]);
        }
    }
}