package com.cloudeducate.redtick.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.cloudeducate.redtick.Adapters.PerformanceRecyclerAdapter;
import com.cloudeducate.redtick.Model.PerformanceTracker;
import com.cloudeducate.redtick.Model.Result;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL;
import com.cloudeducate.redtick.Volley.VolleySingleton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
    private RecyclerView mRecyclerView;
    private PerformanceRecyclerAdapter performanceRecyclerAdapter;
    private LineChart mChart;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.performanceRecyclerview);
        mRecyclerView.setLayoutManager(new com.cloudeducate.redtick.Utils.LinearLayoutManager(PerformanceActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mChart = (LineChart) findViewById(R.id.chart1);
        /*mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);*/

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // set the marker to the chart
        //mChart.setMarkerView(mv);

        mChart.animateX(2500);
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);

        // // restrain the maximum scale-out factor
        // mChart.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // mChart.centerViewPort(10, 50, AxisDependency.LEFT);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

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

        List<PerformanceTracker> performanceTrackerList = new ArrayList<PerformanceTracker>();
        List<Integer> integerList = new ArrayList<Integer>();

        JSONObject jsonObjectMain = new JSONObject(json);
        JSONObject jsonObject = jsonObjectMain.getJSONObject(Constants.PERFORMANCE);
        String teacher_name = jsonObject.getString(Constants.TEACHER);
        JSONArray trackingArray = jsonObject.getJSONArray(Constants.TRACKING);
        teacherName.setText("Teacher - " + teacher_name);

        // JSONArray has x JSONObject
        for (int i = 0; i < trackingArray.length(); i++) {

            PerformanceTracker performanceTracker = new PerformanceTracker();

            // Creating JSONObject from JSONArray
            JSONObject object = trackingArray.getJSONObject(i);

            performanceTracker.setGrade(Integer.valueOf(object.getString(Constants.GRADE)));
            performanceTracker.setWeek(Integer.valueOf(object.getString(Constants.WEEK)));
            /*grade.setText("Grade : " + object.getString(Constants.GRADE));
            week.setText("Week : " + object.getString(Constants.WEEK));*/

            performanceTrackerList.add(performanceTracker);
            Log.v(TAG, "test = " + String.valueOf(object.getString(Constants.GRADE)));
        }

        JSONArray jsonArray = jsonObjectMain.getJSONArray(Constants.MONTHLY);
        for (int i = 0; i < jsonArray.length(); i++) {
            integerList.add(jsonArray.getInt(i));
        }

        setData(integerList.size(), integerList);
        Log.v("array", String.valueOf(integerList));

        performanceRecyclerAdapter = new PerformanceRecyclerAdapter(this, performanceTrackerList);
        mRecyclerView.setAdapter(performanceRecyclerAdapter);

    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Data");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void setData(int count, List<Integer> list) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add("Week " + (i+1));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            int val = list.get(i);
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Month's Performance ");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.setFillColor(getResources().getColor(R.color.md_cyan_800));
        set1.setDrawFilled(true);
        set1.setHighlightEnabled(true);
        set1.setColor(R.color.md_grey_800);
        set1.setCircleColor(R.color.md_deep_orange_600);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        LimitLine ll1 = new LimitLine(130f, "Min grade");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTextColor(getResources().getColor(R.color.md_green_500));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.setAxisMaxValue(10f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(true);

        mChart.getAxisRight().setEnabled(false);

        // set data
        mChart.setData(data);
    }


}
