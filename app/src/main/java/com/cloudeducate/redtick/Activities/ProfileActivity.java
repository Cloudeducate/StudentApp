package com.cloudeducate.redtick.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {


    private String jsonFromDashboard;
    private TextView name, email, phone,school,schphone,rollno,section,remarks,courses;
    SharedPreferences sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        jsonFromDashboard = intent.getStringExtra(Constants.PROFILE);
        Log.v("yahoo", jsonFromDashboard);

        Set<String> defaultval = new HashSet<String>();
        defaultval.add("English");
        defaultval.add("Mathmatics");
        sharedpref = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        Set<String> values = sharedpref.getStringSet(getString(R.string.courses), defaultval);
        values.toArray();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        phone = (TextView) findViewById(R.id.phone);
        school = (TextView) findViewById(R.id.organisation);
        schphone = (TextView) findViewById(R.id.phoneno);
       section = (TextView) findViewById(R.id.section);
        remarks = (TextView) findViewById(R.id.remarks);
        rollno = (TextView) findViewById(R.id.rollno);
        courses = (TextView) findViewById(R.id.subjects);
        parsejsondata(jsonFromDashboard);
    }

    public void parsejsondata(String jsonstring) {
        try {
            JSONObject jsonobj = new JSONObject(jsonstring);
            JSONObject user = jsonobj.getJSONObject(Constants.USER);
            String nameData = user.getString(Constants.NAME);
            String emailData = user.getString(Constants.EMAIL);
            String phoneData = user.getString(Constants.PHONE);


            JSONObject scholar= jsonobj.getJSONObject(Constants.SCHOLAR);
            String rollnodata=scholar.getString(Constants.ROLL_NO);
            String dob=scholar.getString("_dob");



            if (nameData != null && emailData != null && phoneData != null){
                name.setText(nameData);
                email.setText("Email :"+emailData);
                phone.setText("Contact :"+phoneData);
                section.setText("Date of Birth :"+dob);
                school.setText("School Studying in : ");
                schphone.setText("School Phone No. :");
                remarks.setText("Remarks : ");
                rollno.setText("Student Roll No."+rollnodata);
                courses.setText("Subjects Studing on : ");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
