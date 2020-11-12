package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_details, container, false);

        TextView messageTV = v.findViewById(R.id.messageTV);
        TextView idTV = v.findViewById(R.id.idTV);
        CheckBox checkBox = v.findViewById(R.id.checkBox);
        Button hideButton = v.findViewById(R.id.hideButton);


        Bundle bundle = getArguments();
        long id = bundle.getLong("ID");
        String message = bundle.getString("MSG");
        boolean received = bundle.getBoolean("RECEIVED");

        messageTV.setText("Message: " + message);
        idTV.setText("ID: " + id);
        checkBox.setChecked(!received);

        hideButton.setOnClickListener(v1 -> {
            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }
}