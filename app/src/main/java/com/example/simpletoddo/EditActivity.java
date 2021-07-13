package com.example.simpletoddo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    // References from Activity_edit
    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Grab the references of our layout
        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        // set title of the second View
        getSupportActionBar().setTitle("Edit Item");

        // Retrieve data from main activity and populate in the edit activity
        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        // When the user is done editing. they click the save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent which contain the result of which user was editing
                Intent intent = new Intent();
                // Pass the result of editing
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                // Set the result of the intent
                setResult(RESULT_OK, intent);
                // Close activity(edit text). close the screen and go back
                finish();
            }
        });

    }
}