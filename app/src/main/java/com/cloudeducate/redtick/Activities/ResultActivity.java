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
import com.android.volley.toolbox.StringRequest;
import com.cloudeducate.redtick.Adapters.ResultRecyclerviewAdapter;
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

public class ResultActivity extends AppCompatActivity {

    String metadata;
    SharedPreferences sharedpref;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Result> list = new ArrayList<Result>();
    private RecyclerView mRecyclerView;
    private ResultRecyclerviewAdapter resultRecyclerviewAdapter;
    private final String TAG = "MyApp";
    String course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        metadata = sharedpref.getString(getString(R.string.metavalue), "null");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resulttask();

        mRecyclerView = (RecyclerView) findViewById(R.id.rvresult);
        mRecyclerView.setLayoutManager(new com.cloudeducate.redtick.Utils.LinearLayoutManager(ResultActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
    }

    void resulttask() {
        Set<String> defaultval = new HashSet<String>();
        defaultval.add("English");
        defaultval.add("Mathmatics");
        Set<String> values = sharedpref.getStringSet(getString(R.string.courses), defaultval);
        Spinner spinner = (Spinner) findViewById(R.id.courses_spinner);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, values.toArray());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course_id = Integer.toString(position);
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
        if (course_id == null){
            course_id = "0";
        }
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, URL.getResultRequestURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                list = parseJson(response);
                resultRecyclerviewAdapter = new ResultRecyclerviewAdapter(ResultActivity.this, list);
                mRecyclerView.setAdapter(resultRecyclerviewAdapter);
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

    public List<Result> parseJson(String jsonString) {
        List<Result> resultList = new ArrayList<Result>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String
                JSONObject jsonObjMain = new JSONObject(jsonString);

                // Creating JSONArray from JSONObject
                JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.RESULT);


                // JSONArray has x JSONObject
                for (int i = 0; i < jsonArray.length(); i++) {

                    Result result = new Result();

                    // Creating JSONObject from JSONArray
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    result.setExam(jsonObject.getString(Constants.TYPE));
                    result.setyear(jsonObject.getString(Constants.YEAR));
                    result.setmarks(jsonObject.getString(Constants.MARKS));
                    result.setHighest(jsonObject.getString(Constants.HIGHEST));
                    result.setaverage(jsonObject.getString(Constants.AVERAGE));

                    Log.v(TAG, "test = " + String.valueOf(jsonObject.getString(Constants.TYPE)));

                    resultList.add(result);

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.v(TAG, "List = " + String.valueOf(resultList));

        return resultList;

    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Results");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }


}