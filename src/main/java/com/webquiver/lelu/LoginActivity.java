package com.webquiver.lelu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET,passwordET;
    ImageView loginbtn;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //apply font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/latoregular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        usernameET = (EditText) findViewById(R.id.username_id);
        passwordET = (EditText) findViewById(R.id.password_id);
        loginbtn = (ImageView) findViewById(R.id.loginbtn_id);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


        //code for the redlinr in edittext

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



//check email format correct or not
        usernameET.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){

                if (isEmailValid(usernameET.getText().toString()))
                {
                    (usernameET).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.smalltick, 0);


                }
                else
                    (usernameET).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            }
        });










    }



        public static boolean isEmailValid(String email) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
















}
