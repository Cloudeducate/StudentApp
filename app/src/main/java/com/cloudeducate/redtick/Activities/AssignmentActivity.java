package com.cloudeducate.redtick.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudeducate.redtick.Adapters.AssignmentRecyclerviewAdapter;
import com.cloudeducate.redtick.Model.Assignment;
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

public class AssignmentActivity extends AppCompatActivity {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Assignment> list = new ArrayList<Assignment>();
    private RecyclerView mRecyclerView;
    private SharedPreferences sharedpref;
    private String course_id;
    private String metadata;
    private AssignmentRecyclerviewAdapter assignmentRecyclerviewAdapter;

    public static final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        metadata = sharedpref.getString(getString(R.string.metavalue), "null");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AssignmentActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        subjectspinnertask();
        fetchData();

    }

    public void subjectspinnertask()
    {
        Set<String> defaultval = new HashSet<String>();
        defaultval.add("English");
        defaultval.add("Mathmatics");
        Set<String> values= sharedpref.getStringSet(getString(R.string.courses),defaultval);
        Spinner spinner = (Spinner) findViewById(R.id.course_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,values.toArray());
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course_id = Integer.toString(position);
                fetchData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void fetchData() {
        Log.v(TAG, "fetchData is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL.getStudentAssignmentRequestURL(course_id), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                if (response.toString() == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response.toString());
                list = parseJson(response.toString());
                assignmentRecyclerviewAdapter = new AssignmentRecyclerviewAdapter(AssignmentActivity.this, list);
                mRecyclerView.setAdapter(assignmentRecyclerviewAdapter);
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

    public List<Assignment> parseJson(String jsonString) {
        List<Assignment> assignmentList = new ArrayList<Assignment>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);

                // Creating JSONArray from JSONObject
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.ASSIGNMENTS);


                // JSONArray has x JSONObject
                for (int i = 0; i < jsonArray.length(); i++) {

                    Assignment assignment = new Assignment();

                    // Creating JSONObject from JSONArray
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    assignment.setTitle(jsonObject.getString(Constants.ASSIGNMENT_TITLE));
                    assignment.setCourse(jsonObject.getString(Constants.ASSIGNMENT_COURSE));
                    assignment.setDeadline(jsonObject.getString(Constants.ASSIGNMENT_DEADLINE));
                    assignment.setDescription(jsonObject.getString(Constants.ASSIGNMENT_DESCRIPTION));
                    assignment.setId(jsonObject.getString(Constants.ASSIGNMENT_ID));
                    assignment.setFilename(jsonObject.getString(Constants.ASSIGNMENT_FILNAME));
                    assignment.setSubmitted(Boolean.valueOf(jsonObject.getString(Constants.ASSIGNMENT_STATUS)));

                    Log.v(TAG, "test = " + String.valueOf(jsonObject.getString(Constants.ASSIGNMENT_TITLE)));

                    assignmentList.add(assignment);

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.v(TAG, "List = " + String.valueOf(assignmentList));

        return assignmentList;

    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Assignments");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

}
