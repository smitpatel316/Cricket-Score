package net.smitpatel.cricketscore;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class matchInfo extends AppCompatActivity implements GestureDetector.OnGestureListener {
    public String matchId;
    private RequestQueue rq;
    private static String url;
    private TextView series_name;
    private TextView venue;
    private TextView toss;
    private TextView team1Name;
    private ListView team1;
    private ArrayList<String> arrayList1;
    private TextView team2Name;
    private ListView team2;
    private ArrayList<String> arrayList2;
    private GestureDetectorCompat detector;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);
        Intent intent = getIntent();
        matchId = intent.getStringExtra("matchId");
        url = "https://mapps.cricbuzz.com/cbzios/match/" + matchId;
        rq = Volley.newRequestQueue(this);
        series_name = findViewById(R.id.series_name);
        venue = findViewById(R.id.venue);
        toss = findViewById(R.id.toss);

        team1Name = findViewById(R.id.team1Name);
        team1 = findViewById(R.id.team1);
        arrayList1 = new ArrayList<>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList1);
        team1.setAdapter(arrayAdapter);

        team2Name = findViewById(R.id.team2Name);
        team2 = findViewById(R.id.team2);
        arrayList2 = new ArrayList<>();
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList2);
        team2.setAdapter(arrayAdapter2);

        detector = new GestureDetectorCompat(getApplicationContext(), this);

        jsonParse();
        this.mHandler = new Handler();
        m_Runnable.run();
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            matchInfo.this.mHandler.postDelayed(m_Runnable, 10000);
        }
    };

    private void jsonParse() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Match Info...");
        progressDialog.show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    String seriesName = "Series: " + response.getString("series_name");
                    series_name.setText(seriesName);
                    JSONObject venueObj = response.getJSONObject("venue");
                    String venueInfo = "Venue: "
                            + venueObj.getString("name")
                            + ", "
                            + venueObj.getString("location");
                    venue.setText(venueInfo);
                    String tossInfo = "Toss: " +
                            response.getJSONObject("header").getString("toss");
                    toss.setText(tossInfo);
                    String team1NameInfo = response.getJSONObject("team1").getString("name") +
                            " Line up: ";
                    team1Name.setText(team1NameInfo);

                    JSONArray players = response.getJSONArray("players");
                    JSONArray team1 = response.getJSONObject("team1").getJSONArray("squad");
                    for (int i = 0; i < team1.length(); i++) {
                        for (int j = 0; j < players.length(); j++) {
                            int teamPlayerId = team1.getInt(i);
                            int playerId = parseInt(players.getJSONObject(j).getString("id"));
                            if (teamPlayerId == playerId) {
                                arrayList1.add(players.getJSONObject(j).getString("f_name"));
                            }
                        }
                    }
                    String team2NameInfo = response.getJSONObject("team2").getString("name") +
                            " Line up: ";
                    team2Name.setText(team2NameInfo);
                    JSONArray team2 = response.getJSONObject("team2").getJSONArray("squad");
                    for (int i = 0; i < team2.length(); i++) {
                        for (int j = 0; j < players.length(); j++) {
                            int teamPlayerId = team2.getInt(i);
                            int playerId = parseInt(players.getJSONObject(j).getString("id"));
                            if (teamPlayerId == playerId) {
                                arrayList2.add(players.getJSONObject(j).getString("f_name"));
                            }
                        }
                    }
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

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 5 && Math.abs(velocityX) > 5) {
            Intent intent = new Intent(getApplicationContext(), scorecard.class);
            intent.putExtra("matchId", this.matchId);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void view_scorecard(View v) {
        Intent intent = new Intent(getApplicationContext(), scorecard.class);
        intent.putExtra("matchId", this.matchId);
        startActivity(intent);
    }
}
