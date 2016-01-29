package com.cloudeducate.redtick.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cloudeducate.redtick.Model.PerformanceTracker;
import com.cloudeducate.redtick.Model.Result;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL;
import com.cloudeducate.redtick.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerformanceActivity extends AppCompatActivity {

    String metadata;
    SharedPreferences sharedpref;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Result> list = new ArrayList<Result>();
    private final String TAG = "yoge";
    String course_id;
    private TextView teacherName, week, grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedpref = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        metadata = sharedpref.getString(getString(R.string.metavalue), "null");

        teacherName = (TextView) findViewById(R.id.teacher_name);
        week = (TextView) findViewById(R.id.week);
        grade = (TextView) findViewById(R.id.grade);

        getPerformance();

    }

    void getPerformance() {
        Set<String> defaultval = new HashSet<String>();
        defaultval.add("English");
        defaultval.add("Hindi");
        defaultval.add("Mathematics");
        defaultval.add("Science");
        //Set<String> values = sharedpref.getStringSet(getString(R.string.courses), defaultval);
        Spinner spinner = (Spinner) findViewById(R.id.courses_spinner);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, defaultval.toArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course_id = Integer.toString(position + 1);
                fetchResult();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fetchResult();
            }
        });
    }

    void fetchResult() {
        Log.v(TAG, "fetchData is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        if (course_id == null) {
            course_id = "0";
        }
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL.getPerformanceRequestURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                try {
                    parseJson(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v(TAG, "response = " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.v(TAG, "Response = " + "timeOut");
                } else if (error instanceof AuthFailureError) {
                    Log.v(TAG, "Response = " + "AuthFail");
                } else if (error instanceof ServerError) {
                    Log.v(TAG, "Response = " + "ServerError");
                } else if (error instanceof NetworkError) {
                    Log.v(TAG, "Response = " + "NetworkError");
                } else if (error instanceof ParseError) {
                    Log.v(TAG, "Response = " + "ParseError");
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.v("MyApp", "post parameter " + course_id);
                params.put("course", course_id);
                return params;
            }

            ;

            @Override
            public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-App", "student");
                params.put("X-Access-Token", metadata);
                return params;
            }

            ;
        };

        requestQueue.add(jsonObjectRequest);
    }

    public void parseJson(String json) throws JSONException {

        JSONObject jsonObjectMain = new JSONObject(json);
        JSONObject jsonObject = jsonObjectMain.getJSONObject(Constants.PERFORMANCE);
        String teacher_name = jsonObject.getString(Constants.TEACHER);
        JSONArray trackingArray = jsonObject.getJSONArray(Constants.TRACKING);
        teacherName.setText(teacher_name);

// JSONArray has x JSONObject
        for (int i = 0; i < trackingArray.length(); i++) {

            PerformanceTracker performanceTracker = new PerformanceTracker();

            // Creating JSONObject from JSONArray
            JSONObject object = trackingArray.getJSONObject(i);

            performanceTracker.setGrade(Integer.valueOf(object.getString(Constants.GRADE)));
            performanceTracker.setWeek(Integer.valueOf(object.getString(Constants.WEEK)));
            grade.setText("Grade : " + object.getString(Constants.GRADE));
            week.setText("Week : " + object.getString(Constants.WEEK));

            Log.v(TAG, "test = " + String.valueOf(object.getString(Constants.GRADE)));
        }

    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

}
