package app.km.in.dailybharat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import java.util.Timer;
import java.util.TimerTask;

import app.km.in.dailybharat.CustomVolley.CustomJSONObjectRequest;
import app.km.in.dailybharat.CustomVolley.VolleyResponse;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Sexy_Virus on 08/03/18.
 */

public class StartingActivity extends AppCompatActivity implements VolleyResponse {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> bannerImages = new ArrayList<String>();
    private ArrayList<String> bannerTitle = new ArrayList<String>();

    RecyclerAdapter adapter;
    RecyclerView entertainmentRecycler, newsRecycler, businessRecycler;
    JSONArray ja, jaE, jaN, jaB;
    Button ent, bus, news;
    RequestQueue mQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("NightMode", MODE_PRIVATE);
        String nightValue = sp.getString("value", "off");
        if (nightValue.equalsIgnoreCase("off")) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (nightValue.equalsIgnoreCase("on")) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (nightValue.equalsIgnoreCase("auto")) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_AUTO);
        }
        setContentView(R.layout.activity_starting);
        try {
            getBundle();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        init();
        setBannerImages();
        mQueue = Volley.newRequestQueue(getApplicationContext());
        getData("entertainment");
        getData("business");
        getData("news");


    }


    private void getData(String type) {
        String url = null;
        switch (type) {
            case "entertainment":
                url = "https://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=1e2be97ff1724f6e883cecb345e1e65a";
                break;
            case "business":
                url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=1e2be97ff1724f6e883cecb345e1e65a";
                break;
            case "news":
                url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=27e55646d2a24b8eb1c8d26457d6d318";
                break;
        }
        networkAction(url, type);


    }

    private void setRecyclerView(RecyclerView view, ArrayList<String> images) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        view.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getApplicationContext(), getData(images));
        view.setAdapter(adapter);

    }

    private ArrayList<ImagesModel> getData(ArrayList<String> url) {
        ArrayList<ImagesModel> Data = new ArrayList<>();
        for (int i = 0; i < url.size(); i++) {
            ImagesModel image = new ImagesModel(url.get(i));
            Data.add(image);
        }
        return Data;
    }

    private void networkAction(String url, final String type) {
        CustomJSONObjectRequest request1 = new CustomJSONObjectRequest(Request.Method.GET, url,
                new JSONObject(), type, this);
        mQueue.add(request1.getJsonObjectRequest());
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new BannerAdapter(StartingActivity.this, bannerImages, bannerTitle));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        entertainmentRecycler = findViewById(R.id.entertainment_recycler);
        newsRecycler = findViewById(R.id.news_recycler);
        businessRecycler = findViewById(R.id.business_recycler);
        ent = findViewById(R.id.entButton);
        bus= findViewById(R.id.busBtn);
        news=findViewById(R.id.newsBtn);
        ent.setVisibility(View.GONE);
        bus.setVisibility(View.GONE);
        news.setVisibility(View.GONE);

        // Auto start of viewpager

    }

    private void setBannerImages() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == bannerImages.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }


    private void getBundle() throws JSONException {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String D = b.getString("Data");
            ja = new JSONArray(D);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                bannerImages.add(jsonObject.getString("urlToImage"));
                bannerTitle.add(jsonObject.getString("title"));
            }
        }
    }

    @Override
    public void onResponse(JSONObject jsonRes, String tag) {
        final ArrayList<String> imagesUrl = new ArrayList<>();
        try {
            String status = jsonRes.getString("status");
            int total = jsonRes.getInt("totalResults");
            if (status.equals("ok")) {
                JSONArray ja = jsonRes.getJSONArray("articles");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jsonObject = ja.getJSONObject(i);
                    imagesUrl.add(jsonObject.getString("urlToImage"));
                    if (tag.equalsIgnoreCase("entertainment")) {
                        setRecyclerView(entertainmentRecycler, imagesUrl);
                        ent.setVisibility(View.VISIBLE);
                        jaE = ja;
                    }
                    else if (tag.equalsIgnoreCase("business")) {
                        setRecyclerView(businessRecycler, imagesUrl);
                        bus.setVisibility(View.VISIBLE);
                        jaB = ja;
                    } else if (tag.equalsIgnoreCase("news")) {
                        setRecyclerView(newsRecycler, imagesUrl);
                        news.setVisibility(View.VISIBLE);
                        jaN = ja;
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(VolleyError error, String tag) {
        Toast.makeText(getApplicationContext(), "Something Wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }
    public void EntertainmentButton(View view) {
        Intent intent = new Intent(StartingActivity.this, MainActivity.class);
        intent.putExtra("Data", jaE.toString());
        intent.putExtra("type", "entertainment");
        startActivity(intent);
    }
    public void BusinessButton(View view) {
        Intent intent = new Intent(StartingActivity.this, MainActivity.class);
        intent.putExtra("Data", jaB.toString());
        intent.putExtra("type", "business");
        startActivity(intent);
    }

    public void NewsButton(View view) {
        Intent intent = new Intent(StartingActivity.this, MainActivity.class);
        intent.putExtra("Data", jaN.toString());
        intent.putExtra("type", "news");
        startActivity(intent);
    }
}
