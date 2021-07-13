package com.example.simpletoddo;

import org.apache.commons.io.FileUtils;
//import android.os.FileUtils;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Define Keys
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;     // Create list of items

    // Adding/creating reference to each view/layer (getting a handler for each layer)
    // Member variables:
    Button btnAdd;      // Button is a class and btnAdd is an instance of the class (object)
    EditText etItem;    // edit text
    RecyclerView rvItems;   // todoList
    ItemAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main);

        // Get reference of layers
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        // Each view has different methods that can be called
        //etItem.setText("This text was made from java");
        /*
        items = new ArrayList<>();      // List of type String using ArrayList
        //Adding elements to the list
        items.add("Buy milk");
        items.add("Go to the gym");
        items.add("Have fun!"); */
        loadItems();    // call function and load todoList

        ItemAdapter.onLongClickListener onLongClickListener = new ItemAdapter.onLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);

                // notify adapter which position will be deleted
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was deleted", Toast.LENGTH_SHORT).show();
                saveItems(); // save items into a data file
            }
        };
        // onClickListener
        ItemAdapter.onClickListener onClickListener = new ItemAdapter.onClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "single click at position" + position);
                // Create a new activity
                // Using intents (like a request to the android system) for opening
                // a new activity defined in our application=
                // MainActivity.this: the current instance of MainActivity and then go to EditClass
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // Pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // Display the Activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        // Construct adapter
        itemsAdapter = new ItemAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Add implementation
        // onClick button add item to the list
        // onClick handler

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Grab context that it's on the etItem
                String todoItem = etItem.getText().toString();
                // Add item to the model
                items.add(todoItem);
                // Notify adapter that an item is inserted. Inserted at the end of the list
                itemsAdapter.notifyItemInserted(items.size() - 1);
                // Clear text
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems(); // save changes into the data file
            }
        });
    }
    // Handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            // Retrieve the update text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract the original position of edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // Update the model at the right position with new item text
            items.set(position, itemText);
            // notify the adapter
            itemsAdapter.notifyItemChanged(position);
            // persist changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();

        }
        else{
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    // Store list of item in a file
    private File getDataFile() {
        // return the file in which well store
        return new File(getFilesDir(), "data.txt"); // File(directory, name of file)
    }
    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main Activity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // This function save items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main Activity", "Error writing items", e);
        }
    }
}