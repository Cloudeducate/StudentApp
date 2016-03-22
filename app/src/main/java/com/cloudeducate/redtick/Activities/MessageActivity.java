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
import com.cloudeducate.redtick.Adapters.ConversationAdapter;
import com.cloudeducate.redtick.Model.Conversation;
import com.cloudeducate.redtick.R;
import com.cloudeducate.redtick.Utils.Constants;
import com.cloudeducate.redtick.Utils.URL;
import com.cloudeducate.redtick.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private SharedPreferences sharedpref;
    private String metadata;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private final String TAG = "msg";
    private ProgressDialog progressDialog;
    private ConversationAdapter conversationAdapter;
    private RecyclerView mRecyclerView;
    private List<Conversation> list = new ArrayList<Conversation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpref = this.getSharedPreferences(getString(R.string.preference), Context.MODE_PRIVATE);
        metadata = sharedpref.getString(getString(R.string.metavalue), "null");

        mRecyclerView = (RecyclerView) findViewById(R.id.conversation_rv);
        mRecyclerView.setLayoutManager(new com.cloudeducate.redtick.Utils.LinearLayoutManager(MessageActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FetchMessages();
    }

    public void FetchMessages() {
        Log.v(TAG, "fetchData fo Courses is called");
        volleySingleton = VolleySingleton.getMyInstance();
        requestQueue = volleySingleton.getRequestQueue();
        showProgressDialog();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL.getConversationStart(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v(TAG, "response = " + response);
                list = parseJson(response.toString());
                conversationAdapter = new ConversationAdapter(MessageActivity.this, list);
                mRecyclerView.setAdapter(conversationAdapter);

                if (response == null) {
                    Log.v(TAG, "fetchData is not giving a fuck");
                }
                progressDialog.dismiss();
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
            public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-App", "student");
                params.put("X-Access-Token", metadata);
                return params;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    public List<Conversation> parseJson(String jsonString) {
        List<Conversation> conversationList = new ArrayList<Conversation>();
        try {
            // Creating JSONObject from String
            JSONObject jsonObjMain = new JSONObject(jsonString);

            // Creating JSONArray from JSONObject
            JSONArray jsonArray = jsonObjMain.getJSONArray(Constants.CONVERSATION);

            // JSONArray has x JSONObject
            for (int i = 0; i < jsonArray.length(); i++) {

                Conversation conversation = new Conversation();

                // Creating JSONObject from JSONArray
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                conversation.setTeacher(jsonObject.getString(Constants.CONV_TEACHER_NAME));
                conversation.setId(jsonObject.getString(Constants.CONV_ID));

                //Log.v(TAG, "test = " + String.valueOf(jsonObject.getString(Constants.TYPE)));

                conversationList.add(conversation);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        Log.v(TAG, "List = " + String.valueOf(conversationList));

        return conversationList;

    }


    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Messages ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

}
