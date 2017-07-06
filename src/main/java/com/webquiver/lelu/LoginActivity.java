package com.webquiver.lelu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.attr.left;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET,passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET=(EditText)findViewById(R.id.username_id);
        passwordET=(EditText)findViewById(R.id.password_id);



        usernameET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                usernameET.setFocusable(true);
                usernameET.setCursorVisible(true);
                (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.test, 0, 0, 0);
                (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                return false;

            }
        });

      passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passwordET.setFocusable(true);
                 passwordET.setCursorVisible(true);
                (usernameET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                (passwordET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.test, 0, 0, 0);
                return false;


            }
        });



        usernameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                passwordET.setCursorVisible(true);
                (usernameET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                (passwordET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.test, 0, 0, 0);
                return false;



            }
        });

        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                passwordET.setCursorVisible(false);
                (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                return false;



            }
        });












    }









}
