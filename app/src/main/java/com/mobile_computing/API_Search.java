package com.mobile_computing;

import org.json.*;
import org.slf4j.LoggerFactory;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.RequestQueue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class API_Search extends Activity {


//    private static final String API_URL = "http://10.20.0.5:8080/api/s/";
    private static final String API_URL = "http://10.3.5.214:8080/api/s/";      //ip address
    private static final String LOG_TAG = "MOBILE COMPUTING";

    // enable logging
    private static Logger log = LoggerFactory.getLogger(API_Search.class);

    private RecyclerView m_recView;
    private RecyclerView.Adapter m_adapter;
    private RecyclerView.LayoutManager m_layout;
    private RequestQueue requestQ;

    // This is run when the intent is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Setup the different views
        m_recView = (RecyclerView) findViewById(R.id.recycler_view);
        m_layout  = new LinearLayoutManager(this);
        m_adapter = new DatumAdapter(this);

        m_recView.setHasFixedSize(true);
        m_recView.setLayoutManager(m_layout);
        m_recView.setAdapter(m_adapter);

        final Context self = this;
        ((DatumAdapter) m_adapter).setOnItemClickListener(new DatumAdapter.DatumClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                // todo - implement this result display activity
//                String msg = "Item with ID: " + ((DatumAdapter) m_adapter).getItem(position).id();
//                Toast toast = Toast.makeText(self, msg, Toast.LENGTH_SHORT);
//                toast.show();
                Datum item = ((DatumAdapter) m_adapter).getItem(position);
                int id = item.id();
                String date = item.date();
                String title = item.title();
                String text = item.text();
                String imageUrl = item.imageUrl();

                Datum datumApiSearch = new Datum();
                datumApiSearch.setM_id(id);
                datumApiSearch.setM_date(date);
                datumApiSearch.setM_title(title);
                datumApiSearch.setM_text(text);
                datumApiSearch.setM_imageUrl(imageUrl);

                Intent intent = new Intent(API_Search.this, ResultDisplayActivity.class);
                intent.putExtra("datumApiSearch", datumApiSearch);
                log.debug(":::" + datumApiSearch.id());

                startActivity(intent);
            }
        });

        // Setup the button
        final Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((DatumAdapter) m_adapter).clear();
                search();
            }
        });

        // Setup the request queue for network requests (using Volley)
        requestQ = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    // Initiate a search
    private void search() {
        // Make a request
        final EditText keywordEditText = (EditText) findViewById(R.id.keyWordEditText);
        final Context self = this;
        StringRequest res = new StringRequest(Request.Method.GET, API_URL + keywordEditText.getText(),
            new Response.Listener<String>() {
                private ImageLoader imgLoad = VolleySingleton.getInstance(self).getImageLoader();

                // On response
                @Override
                public void onResponse(String response) {
                    try {
                        // Get the response and convert it to JSON
                        JSONArray resultJsonArray = (new JSONObject(response)).getJSONArray("results");

                        // Iterate over all of the JSON responses
                        JSONObject obj = null;
                        for (int i = 0; i < resultJsonArray.length(); ++i) {
                            // Get the properties
                            obj = resultJsonArray.getJSONObject(i);
                            Datum datum = new Datum(obj.getInt("id"), obj.getString("title"),
                                    obj.getString("date"), obj.getString("text"), obj.getString("image"));

                            // Add the item to the view
                            ((DatumAdapter) m_adapter).addItem(datum, m_adapter.getItemCount());
                        }

                    } catch (Exception e) {
                        // Print to console on error
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                // Print to console on Error
                @Override
                public void onErrorResponse(VolleyError err) {
                    err.printStackTrace();
                }
            }
        );

        // Start the request
        requestQ.add(res);
    }
}