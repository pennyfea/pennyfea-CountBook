/*
 *  Copyright (c)  2017 Austin Pennyfeather. CMPUT301, University of Alberta -- All rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the Code of Student Behaviour at University of Alberta. You can find a copy of the license in this project, otherwise please contact pennyfea@ualberta.ca.
 */

package com.example.pennyfea.countbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class EditValue extends AppCompatActivity {
    Button sendVal;
    EditText initalVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_value);
        sendVal = (Button) findViewById(R.id.intial_button);
        initalVal = (EditText) findViewById(R.id.initialValue);


        sendVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditValue.this, Counter.class);
                intent.putExtra("yourmessage", initalVal.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
