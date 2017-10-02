/*
 *
 * MainActivity
 *
 * October 1 2017
 *
 * The MainActivity class is used to create and store the names of the counters in an Array list,
 * Displays the contents of the Array list in list view.
 * User can delete and set counter information accordingly.
 * All counter names are successfully store when app is closed.
 *
 * Resources: StackOverFlow, Youtube
 *
 * Copyright (c)  2017 Austin Pennyfeather. CMPUT301, University of Alberta -- All rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta. You can find a copy of the license in this project, otherwise please contact pennyfea@ualberta.ca.
 */

package com.example.pennyfea.countbook;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    EditText inputText;
    Button createCounters;
    ListView listView;
    Button deleteCounters;
    Button editCounterName;
    Date date;

    private static final String FILENAME = "file.save";

    /**
     * @param savedInstanceState
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listV);
        createCounters = (Button) findViewById(R.id.Addbutton);
        deleteCounters = (Button) findViewById(R.id.Delebutton);
        editCounterName = (Button) findViewById(R.id.Editbutton);
        inputText = (EditText) findViewById(R.id.edit_message);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_multiple_choice, arrayList);
        listView.setAdapter(adapter);
        listView.getAdapter().getCount();
        editCounters();
        dCounters();
        cCounters();
    }

    /**
     * Creates a counter button, adds counter name to an array list
     * Displays the number of items in Array list after being created.
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void cCounters() {
        SimpleDateFormat formating = new SimpleDateFormat("yyyy-MM-dd");
        final String newdate = formating.format(new Date());
        createCounters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = inputText.getText().toString();
                arrayList.add(newdate + "| " + newItem);
                adapter.notifyDataSetChanged();
                saveInfile();
                Toast.makeText(getApplicationContext(), "Total number of counters are: " + listView.getAdapter().getCount(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Creates a delete button and deletes counter name from an array list
     * Displays the number of items in Array list after being removed
     */
    public void dCounters() {
        deleteCounters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkposition = listView.getCheckedItemPositions();
                int itemCount = listView.getCount();
                for (int i = itemCount - 1; i >= 0; i--) {
                    if (checkposition.get(i)) {
                        adapter.remove(arrayList.get(i));
                    }
                }
                checkposition.clear();
                adapter.notifyDataSetChanged();
                saveInfile();
                Toast.makeText(getApplicationContext(), "Total number of counters are: " + listView.getAdapter().getCount(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Creates an edit counter button.
     * When selected creates a new intent where the user can edit the counter information
     */

    public void editCounters() {
        editCounterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkposition = listView.getCheckedItemPositions();
                int itemCount = listView.getCount();
                for (int i = itemCount - 1; i >= 0; i--) {
                    if (checkposition.get(i)) {
                        Intent intent = new Intent(MainActivity.this, Counter.class);
                        MainActivity.this.startActivity(intent);
                    }
                }
                ;
            }
        });
    }

    /**
     * Loads a file
     */


    private void loadFromFile() {

        try {
            FileInputStream ffs = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(ffs));
            String line = in.readLine();
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            arrayList = gson.fromJson(line, listType);
        } catch (FileNotFoundException e) {
            arrayList = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Saved a file
     */

    private void saveInfile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(arrayList, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Loads information from previous session
     */

    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayList);
        listView.setAdapter(adapter);
    }

}
