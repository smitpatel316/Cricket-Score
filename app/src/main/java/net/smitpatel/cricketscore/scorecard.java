package net.smitpatel.cricketscore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.List;

import static java.lang.Float.parseFloat;

public class scorecard extends AppCompatActivity {

    private String matchId;
    private String url;
    private String url2;
    private RequestQueue rq;
    private RequestQueue rq2;
    private TextView team1Name;
    private TextView team1Score;
    private TextView team2Name;
    private TextView team2Score;
    private TextView team1ScoreMain;
    private TextView team2ScoreMain;
    private Button team1Scorecard;
    private Button team2Scorecard;
    private Button team1Scorecard2;
    private Button team2Scorecard2;
    private RecyclerView recyclerView;
    private List<ListItem> listItems;
    private List<ListItem> listItems2;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter2;
    private List<ListItem> listItems3;
    private List<ListItem> listItems4;
    private RecyclerView.Adapter adapter3;
    private RecyclerView.Adapter adapter4;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorecard);
        Intent intent = getIntent();
        matchId = intent.getStringExtra("matchId");
        url = "http://mapps.cricbuzz.com/cbzios/match/" + matchId + "/scorecard.json";
        url2 = "https://mapps.cricbuzz.com/cbzios/match/" + matchId;
        rq = Volley.newRequestQueue(this);
        rq2 = Volley.newRequestQueue(this);
        team1Name = findViewById(R.id.team1Name);
        team1ScoreMain = findViewById(R.id.team1ScoreMain);
        team2ScoreMain = findViewById(R.id.team2ScoreMain);
        team1Score = findViewById(R.id.team1Score);
        team2Name = findViewById(R.id.team2Name);
        team2Score = findViewById(R.id.team2Score);
        team1Scorecard = findViewById(R.id.team1Scorecard);
        team2Scorecard = findViewById(R.id.team2Scorecard);
        team1Scorecard2 = findViewById(R.id.team1Scorecard2);
        team2Scorecard2 = findViewById(R.id.team2Scorecard2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();
        listItems2 = new ArrayList<>();
        listItems3 = new ArrayList<>();
        listItems4 = new ArrayList<>();
        recyclerView.setVisibility(View.INVISIBLE);
        jsonParse();
        this.mHandler = new Handler();
        m_Runnable.run();
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            scorecard.this.mHandler.postDelayed(m_Runnable, 10000);
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
                    JSONArray innings = response.getJSONArray("Innings");
                    JSONObject team1Stuff = innings.getJSONObject(0);
                    String team1Info = team1Stuff.getString("bat_team_name");
                    team1Name.setText(team1Info);
                    team1ScoreMain.setText(team1Info + " Scorecard: ");
                    team1Scorecard.setText("Batting");
                    team1Scorecard2.setText("Bowling");
                    String team1ScoreInfo = team1Stuff.getString("score") + "/" + team1Stuff.getString("wkts") + " " + parseFloat(team1Stuff.getString("ovr")) + " ov";
                    team1Score.setText(team1ScoreInfo);

                    JSONObject team2Stuff = innings.getJSONObject(1);
                    String team2Info = team2Stuff.getString("bat_team_name");
                    team2Name.setText(team2Info);
                    team2ScoreMain.setText(team2Info + " Scorecard: ");
                    team2Scorecard.setText("Batting");
                    team2Scorecard2.setText("Bowling");
                    String team2ScoreInfo = team2Stuff.getString("score") + "/" + team2Stuff.getString("wkts") + " " + parseFloat(team2Stuff.getString("ovr")) + " ov";
                    team2Score.setText(team2ScoreInfo);

                    Log.i("Scorecard", "Making the actual scorecard");
                    players(innings);
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

    private void players(final JSONArray innings) {
        final JSONArray matchInfo = innings;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray players = response.getJSONArray("players");
                    JSONArray batsmen = matchInfo.getJSONObject(0).getJSONArray("batsmen");
                    for (int j = 0; j < batsmen.length(); j++) {
                        JSONObject currBatsmen = batsmen.getJSONObject(j);
                        for (int k = 0; k < players.length(); k++) {
                            if (players.getJSONObject(k).getString("id").equals(currBatsmen.getString("id"))) {
                                listItems.add(new ListItem(players.getJSONObject(k).getString("f_name"),
                                        currBatsmen.getString("out_desc"), "",
                                        "Runs: " + currBatsmen.getString("r") + " Balls: " +
                                                currBatsmen.getString("b") + " 4s: " +
                                                currBatsmen.getString("4s") + " 6s: " +
                                                currBatsmen.getString("6s")));
                            }
                        }
                    }
                    adapter = new MyAdapter(listItems, getApplicationContext(), "scorecard");
                    recyclerView.setAdapter(adapter);
                    batsmen = matchInfo.getJSONObject(1).getJSONArray("batsmen");
                    for (int j = 0; j < batsmen.length(); j++) {
                        JSONObject currBatsmen = batsmen.getJSONObject(j);
                        for (int k = 0; k < players.length(); k++) {
                            if (players.getJSONObject(k).getString("id").equals(currBatsmen.getString("id"))) {
                                listItems2.add(new ListItem(players.getJSONObject(k).getString("f_name"),
                                        currBatsmen.getString("out_desc"), "",
                                        "Runs: " + currBatsmen.getString("r") + " Balls: " +
                                                currBatsmen.getString("b") + " 4s: " +
                                                currBatsmen.getString("4s") + " 6s: " +
                                                currBatsmen.getString("6s")));
                            }
                        }
                    }
                    adapter2 = new MyAdapter(listItems2, getApplicationContext(), "scorecard");
                    JSONArray bowler = matchInfo.getJSONObject(0).getJSONArray("bowlers");
                    for (int j = 0; j < bowler.length(); j++) {
                        JSONObject currBowler = bowler.getJSONObject(j);
                        for (int k = 0; k < players.length(); k++) {
                            if (players.getJSONObject(k).getString("id").equals(currBowler.getString("id"))) {
                                listItems3.add(new ListItem(players.getJSONObject(k).getString("f_name"),
                                        "Wickets: " + currBowler.getString("w") + " Runs: " + currBowler.getString("r"), "",
                                        " Overs: " + currBowler.getString("o") +
                                                " Maidens: " + currBowler.getString("m") +
                                                " Wides: " + currBowler.getString("wd") +
                                                " No Balls: " + currBowler.getString("n")));
                            }
                        }
                    }
                    adapter3 = new MyAdapter(listItems3, getApplicationContext(), "scorecard");
                    bowler = matchInfo.getJSONObject(1).getJSONArray("bowlers");
                    for (int j = 0; j < bowler.length(); j++) {
                        JSONObject currBowler = bowler.getJSONObject(j);
                        for (int k = 0; k < players.length(); k++) {
                            if (players.getJSONObject(k).getString("id").equals(currBowler.getString("id"))) {
                                listItems4.add(new ListItem(players.getJSONObject(k).getString("f_name"),
                                        "Wickets: " + currBowler.getString("w") + " Runs: " + currBowler.getString("r"), "",
                                        " Overs: " + currBowler.getString("o") +
                                                " Maidens: " + currBowler.getString("m") +
                                                " Wides: " + currBowler.getString("wd") +
                                                " No Balls: " + currBowler.getString("n")));
                            }
                        }
                    }
                    adapter4 = new MyAdapter(listItems4, getApplicationContext(), "scorecard");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        rq2.add(request);
    }

    public void team1OnClick(View v) {
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void team2OnClick(View v) {
        recyclerView.setAdapter(adapter2);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void team3OnClick(View v) {
        recyclerView.setAdapter(adapter4);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void team4OnClick(View v) {
        recyclerView.setAdapter(adapter3);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
