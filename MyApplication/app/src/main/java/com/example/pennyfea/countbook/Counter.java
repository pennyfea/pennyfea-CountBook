/*
 *
 * Counter
 *
 * October 1 2017
 *
 * This counter class is used  for creating and storing the counter's information.
 * The outstanding issues are as follows: Cannot save counter information effectively,
 * Each counter when created displays the same comments
 * Initial value when entered not saved.
 *
 * Resources: StackOverFlow, Youtube, lonelyTwiiter
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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

public class Counter extends AppCompatActivity {

    ArrayList<String> commentList;
    ArrayAdapter<String> commentadapter;
    EditText commentText;
    ListView listv;
    TextView counterText;
    Button addCmmt;
    Button resetBtn;
    Button editBtn;
    private int counter;
    private static final String FILENAME = "file2.save";
    static final int PICK_CONTACT_REQUEST = 1;

    /**
     *
     * @param savedInstanceState
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        counterText = (TextView) findViewById(R.id.counterText);
        commentText = (EditText) findViewById(R.id.edit_comment);
        listv = (ListView) findViewById(R.id.commentListView);
        Button minusBtn = (Button) findViewById(R.id.minusButton);
        minusBtn.setOnClickListener(clickListener);
        Button addBtn = (Button) findViewById(R.id.addButton);
        addBtn.setOnClickListener(clickListener);
        resetBtn = (Button) findViewById(R.id.resetButton);
        resetBtn.setOnClickListener(clickListener);
        addCmmt = (Button)findViewById(R.id.add_comment);
        editBtn = (Button) findViewById(R.id.edit_value);
        commentList = new ArrayList<String>();
        commentadapter = new ArrayAdapter<String>(Counter.this, android.R.layout.simple_list_item_1, commentList);
        listv.setAdapter(commentadapter);
        editCounter();
        addComment();
    }

    /**
     * Creates an comment button, adds the comment entered by the user
     * along with the date to the comment section
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addComment() {
        SimpleDateFormat formating = new SimpleDateFormat("yyyy-MM-dd");
        final String newdate = formating.format(new Date());
        addCmmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = commentText.getText().toString();
                commentList.add(newdate + "| " + newItem);
                commentadapter.notifyDataSetChanged();
            }
        });
    }

    public void editCounter(){
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Counter.this, EditValue.class);
                Counter.this.startActivity(intent);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);
            }
        });
    }


    /**
     *
     * Return resutlts from EditValue
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String strCounter = getIntent().getStringExtra("yourmessage");
            int counter = Integer.parseInt(strCounter);
            counterText.setText(counter);
        }
    }

    /**
     * Initialise counter and sets initial value entered by the user.
     */

    private void initCounter(){

        int val = Integer.parseInt(counterText.getText().toString());
        counter = val;
        counterText.setText(counter + "");

    }

    /**
     * Increments counter
     */
    private void plusCounter(){
        counter++;
        counterText.setText(counter + "");
    }

    /**
     * Decrements counter.
     * Counter cannot be decremented to negative numbers
     */
    private void minusCounter(){
        if(counter <= 0 )
            counter = 0;
        counter--;
        counterText.setText(counter + "");

    }

    /**
     * Calls function according to which button is clicked
     */
    private View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.minusButton:
                    minusCounter();
                    break;
                case R.id.addButton:
                    plusCounter();
                    break;
                case R.id.resetButton:
                    initCounter();
                    break;
            }
           saveInfile();
        }
    };

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
            commentList = gson.fromJson(line, listType);
        } catch (FileNotFoundException e) {
            commentList = new ArrayList<>();
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
            gson.toJson(commentList, writer);
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
        commentadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commentList);
        listv.setAdapter(commentadapter);
    }





}
