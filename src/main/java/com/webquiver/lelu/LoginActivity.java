package com.webquiver.lelu;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
              //  (usernameET).setCompoundDrawablesWithIntrinsicBounds(R.drawable.txtovr, 0, 0, 0);
              //  (passwordET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                usernameET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_selected));
               passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_rounded));
                return false;

            }
        });

      passwordET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                passwordET.setFocusable(true);
                 passwordET.setCursorVisible(true);
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_selected));
                usernameET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_rounded));
                return false;


            }
        });



        usernameET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                passwordET.setCursorVisible(true);
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_selected));
                usernameET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_rounded));
                return false;



            }
        });

        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                passwordET.setCursorVisible(false);
                passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.edittext_shape_rounded));
                return false;



            }
        });












    }









}
