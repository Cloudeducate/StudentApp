package com.cloudeducate.redtick.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {


    private String jsonFromDashboard;
    private TextView name, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        jsonFromDashboard = intent.getStringExtra(Constants.PROFILE);
        Log.v("yahoo", jsonFromDashboard);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);

        parsejsondata(jsonFromDashboard);

    }

    public void parsejsondata(String jsonstring) {
        try {
            JSONObject jsonobj = new JSONObject(jsonstring);
            JSONObject user = jsonobj.getJSONObject(Constants.USER);
            String nameData = user.getString(Constants.NAME);
            String emailData = user.getString(Constants.EMAIL);
            String phoneData = user.getString(Constants.PHONE);
            if (nameData != null && emailData != null && phoneData != null){
                name.setText(nameData);
                email.setText(emailData);
                phone.setText(phoneData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
