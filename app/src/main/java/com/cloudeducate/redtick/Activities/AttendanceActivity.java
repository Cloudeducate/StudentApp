package com.cloudeducate.redtick.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.cloudeducate.redtick.Adapters.CourseRecyclerviewAdapter;
import com.cloudeducate.redtick.Model.Courses;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL;
import com.cloudeducate.redtick.Utils.decorators.AbsentDecorator;
import com.cloudeducate.redtick.Utils.decorators.PresentDecorator;
import com.cloudeducate.redtick.Volley.VolleySingleton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttendanceActivity extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    MaterialCalendarView widget;
    TextView textView_date;
    SharedPreferences sharedpref;
    String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<Courses> list = new ArrayList<Courses>();
    private CourseRecyclerviewAdapter courseRecyclerviewAdapter;
    private final String TAG="MyApp";

    private AbsentDecorator absentDecorator;
    private PresentDecorator presentDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        metadata = sharedpref.getString(getString(R.string.metavalue), "null");

        presentDecorator = new PresentDecorator(AttendanceActivity.this);
        //for testing
        CalendarDay daytest=CalendarDay.from(2016,01,26);
        presentDecorator.shouldDecorate(daytest);

        absentDecorator = new AbsentDecorator(AttendanceActivity.this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        widget = (MaterialCalendarView) findViewById(R.id.calendarView);
        textView_date = (TextView) findViewById(R.id.textView_date);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);
        widget.addDecorators(
                new AbsentDecorator(this),
                new PresentDecorator(this),
                absentDecorator, presentDecorator
        );

        fetchattendance();
    }

    void fetchattendance()
    {
        Log.v(TAG, "fetchData fo Courses is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL.getAttendanceURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                Log.v(TAG, "response = " + response);
                parseJson(response);

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

    public void parseJson(String jsonString) {

        Set<String> values=new HashSet<>();
        if (jsonString != null) {

            try {
                // Creating JSONObject from String

                // Creating JSONArray from JSONObject
                JSONArray jsonArray = new JSONArray(jsonString);

                // JSONArray has x JSONObject
                for (int i = 0; i < jsonArray.length(); i++) {
                    String title,start,end;
                    CalendarDay day;
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    title=jsonObject.getString(Constants.Attendance_Title);
                    start=jsonObject.getString(Constants.Attendance_Start);
                    end=jsonObject.getString(Constants.Attendance_End);
                    String[] date=start.split("T");
                    String[] enddate=end.split("T");
                    Log.v(TAG,date[0]+enddate[0]);
                    if(date[0].equals(enddate[0]))
                    {
                        Log.v(TAG,date[0]+enddate[0]);
                        String[] onlydate=date[0].split("-");
                        CalendarDay setday=CalendarDay.today();
                        Log.v(TAG,onlydate[0]+onlydate[1]+onlydate[2]+" " +title);
                        setday=CalendarDay.from(Integer.parseInt(onlydate[0]),Integer.parseInt(onlydate[1]),Integer.parseInt(onlydate[2]));
                        if(title.equals("Present")) {
                         Log.v(TAG,setday.toString()+title);
                            presentDecorator.shouldDecorate(setday);
                        }
                            else if(title.equals("Absent"))
                            absentDecorator.shouldDecorate(setday);
                    }


                    Log.v(TAG, "test = "+ title+start+end);


                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Log.v(TAG, "List = " );
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Attendance");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }


    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        textView_date.setText(getSelectedDatesString());
        //If you change a decorate, you need to invalidate decorators
        presentDecorator.shouldDecorate(date);
        widget.invalidateDecorators();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        getSupportActionBar().setTitle(FORMATTER.format(date.getDate()));
    }

    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }

}
