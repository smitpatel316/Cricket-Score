package net.smitpatel.cricketscore;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private static final String url = "https://mapps.cricbuzz.com/cbzios/match/livematches.json";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private RequestQueue rq;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        rq = Volley.newRequestQueue(this);
        jsonParse();
        this.mHandler = new Handler();
        m_Runnable.run();
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            MainActivity.this.mHandler.postDelayed(m_Runnable, 10000);
        }
    };

    private void jsonParse() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Current Series...");
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("matches");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject match = jsonArray.getJSONObject(i);
                        String title = match.getJSONObject("header").getString("match_desc")
                                + " "
                                + match.getJSONObject("team1").getString("name")
                                + " vs "
                                + match.getJSONObject("team2").getString("name");
                        String status = match.getJSONObject("header").getString("status");
                        String matchId = match.getString("match_id");
                        String details = "";
                        if (match.has("batsman")) {
                            JSONArray batsmens = match.getJSONArray("batsman");
                            Log.d("MyApp", batsmens.toString());

                            for (int j = 0; j < batsmens.length(); j++) {
                                details += batsmens.getJSONObject(j).getString("name") +
                                        " Runs: " + batsmens.getJSONObject(j).getString("r") +
                                        " Balls: " + batsmens.getJSONObject(j).getString("b") +
                                        " 4s: " + batsmens.getJSONObject(j).getString("4s") +
                                        " 6s: " + batsmens.getJSONObject(j).getString("6s") + "\n";
                            }
                        }
                        if (match.has("bowler")) {
                            JSONObject bowler = match.getJSONArray("bowler").getJSONObject(0);
                            details += bowler.getString("name") +
                                    " Overs: " + bowler.getString("o") +
                                    " Maidens: " + bowler.getString("m") +
                                    " Runs: " + bowler.getString("r") +
                                    " Wickets: " + bowler.getString("w");
                        }
                        ListItem item = new ListItem(title, status, matchId, details);
                        listItems.add(item);
                    }
                    adapter = new MyAdapter(listItems, getApplicationContext(), "MainActivity");
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        rq.add(request);
    }
}
