package com.cloudeducate.redtick.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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
import com.cloudeducate.redtick.Adapters.CourseRecyclerviewAdapter;
import com.cloudeducate.redtick.Adapters.ResultRecyclerviewAdapter;
import com.cloudeducate.redtick.Model.Courses;
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

public class CourseActivity extends AppCompatActivity {

    SharedPreferences sharedpref;
    String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Courses> list = new ArrayList<Courses>();
    private RecyclerView mRecyclerView;
    private CourseRecyclerviewAdapter courseRecyclerviewAdapter;
    private final String TAG="MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        metadata = sharedpref.getString(getString(R.string.metavalue), "null");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvcourses);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CourseActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        fetchCourses();
    }

    void fetchCourses(){

        Log.v(TAG, "fetchData fo Courses is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL.getCoursesURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                list = parseJson(response);
                courseRecyclerviewAdapter = new CourseRecyclerviewAdapter(CourseActivity.this, list);
                mRecyclerView.setAdapter(courseRecyclerviewAdapter);
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
        })
        {
            @Override
            public Map<String,String> getHeaders() throws com.android.volley.AuthFailureError{
                Map<String,String> params=new HashMap<String,String>();
                params.put("X-App","student");
                params.put("X-Access-Token",metadata);
                return params;
            };
        };

        requestQueue.add(jsonObjectRequest);
    }

    public List<Courses> parseJson(String jsonString) {
        List<Courses> courseList = new ArrayList<Courses>();
        Set<String> values=new HashSet<>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);

                // Creating JSONArray from JSONObject
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.COURSES);

                // JSONArray has x JSONObject
                for (int i = 0; i < jsonArray.length(); i++) {

                    Courses courses = new Courses();
                    // Creating JSONObject from JSONArray
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    courses.setcoursename(jsonObject.getString(Constants.TITLE));
                    Log.v(TAG, "test = " + String.valueOf(jsonObject.getString(Constants.TITLE)));

                    courseList.add(courses);
                    values.add(courses.getcoursename());

                }
                SharedPreferences sharedpref = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedpref.edit();
                editor.putStringSet(getString(R.string.courses),values);
                editor.commit();

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.v(TAG, "List = " + String.valueOf(courseList));

        return courseList;

    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Courses");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

}

