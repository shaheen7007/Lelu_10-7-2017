package com.webquiver.lelu;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.webquiver.lelu.classes.SessionManagement;

import org.w3c.dom.Text;

public class MyAccountActivity extends AppCompatActivity {


    private SharedPreferences pref;
    SharedPreferences.Editor editor_num_pref;

    TextView NameTXT,PhoneTXT;

    ImageView backbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        NameTXT=(TextView)findViewById(R.id.nametxt_id);
        PhoneTXT=(TextView)findViewById(R.id.phonetxt_id);

        pref = this.getSharedPreferences(SessionManagement.PREF_NAME, Context.MODE_PRIVATE);


        String phone=pref.getString(SessionManagement.KEY_PHONE,"");
        String name=pref.getString(SessionManagement.KEY_NAME,"");


        NameTXT.setText(name);
        PhoneTXT.setText(phone);


        backbtn=(ImageView)findViewById(R.id.bk_id);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }
}
