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
import android.widget.LinearLayout;
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
    RecyclerView[] recycler;
    Button[] button;
    RequestQueue mQueue;
    LinearLayout[] layout;
    JSONArray[] array;
    JSONArray main;
    int[] buttonId = {R.id.entButton, R.id.busBtn, R.id.newsBtn, R.id.healthBtn, R.id.scienceBtn, R.id.sportsBtn, R.id.technologyBtn};
    int[] layoutId = {R.id.lay1, R.id.lay2, R.id.lay3, R.id.lay4, R.id.lay5, R.id.lay6, R.id.lay7};
    int[] recyclerId = {R.id.entertainment_recycler, R.id.business_recycler, R.id.news_recycler, R.id.health_recycler, R.id.science_recycler, R.id.sports_recycler, R.id.technology_recycler};
    String[] type = {"entertainment", "business", "news", "health", "science", "sports", "technology"};
    String[] newsUrl = {
            "https://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=1e2be97ff1724f6e883cecb345e1e65a",
            "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=1e2be97ff1724f6e883cecb345e1e65a",
            "https://newsapi.org/v2/top-headlines?country=in&apiKey=27e55646d2a24b8eb1c8d26457d6d318",
            "https://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=69cfcd1cca3f44a9b65a57137645d1fe",
            "https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=69cfcd1cca3f44a9b65a57137645d1fe",
            "https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=27e55646d2a24b8eb1c8d26457d6d318",
            "https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=27e55646d2a24b8eb1c8d26457d6d318"
    };

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
        getDataFromServer();



    }

    private void getDataFromServer() {
        for (int i = 0; i < type.length; i++) {
            getData(type[i]);
        }
    }


    private void getData(String newsType) {
        String url = null;
        for (int i = 0; i < type.length; i++) {
            if (newsType.equalsIgnoreCase(type[i])) {
                url = newsUrl[i];
            }
        }
        networkAction(url, newsType);


    }

    private void setRecyclerView(RecyclerView view, ArrayList<String> images, ArrayList<String> title) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        view.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(getApplicationContext(), getData(images, title));
        view.setAdapter(adapter);

    }

    private ArrayList<ImagesModel> getData(ArrayList<String> url, ArrayList<String> title) {
        ArrayList<ImagesModel> Data = new ArrayList<>();
        for (int i = 0; i < url.size(); i++) {
            ImagesModel image = new ImagesModel(url.get(i), title.get(i));
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
        recycler = new RecyclerView[recyclerId.length];
        button = new Button[buttonId.length];
        layout = new LinearLayout[layoutId.length];
        array = new JSONArray[recyclerId.length];
        for (int i = 0; i < recyclerId.length; i++) {
            recycler[i] = findViewById(recyclerId[i]);
            button[i] = findViewById(buttonId[i]);
            layout[i] = findViewById(layoutId[i]);
            layout[i].setVisibility(View.GONE);
            final int finalI = i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intentData(finalI);
                }
            });
        }


        // Auto start of viewpager

    }

    private void intentData(int i) {
        Intent intent = new Intent(StartingActivity.this, MainActivity.class);
        intent.putExtra("Data", array[i].toString());
        intent.putExtra("type", type[i]);
        startActivity(intent);
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
            main = new JSONArray(D);
            for (int i = 0; i < main.length(); i++) {
                JSONObject jsonObject = main.getJSONObject(i);
                bannerImages.add(jsonObject.getString("urlToImage"));
                bannerTitle.add(jsonObject.getString("title"));
            }
        }
    }

    @Override
    public void onResponse(JSONObject jsonRes, String tag) {
        final ArrayList<String> imagesUrl = new ArrayList<>();
        final ArrayList<String> newsTitle = new ArrayList<>();
        try {
            String status = jsonRes.getString("status");
            if (status.equals("ok")) {
                JSONArray ja = jsonRes.getJSONArray("articles");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jsonObject = ja.getJSONObject(i);
                    imagesUrl.add(jsonObject.getString("urlToImage"));
                    newsTitle.add(jsonObject.getString("title"));
                    int id = 0;
                    if (tag.equalsIgnoreCase("entertainment")) {
                        id = 0;
                    } else if (tag.equalsIgnoreCase("business")) {
                        id = 1;
                    } else if (tag.equalsIgnoreCase("news")) {
                        id = 2;
                    } else if (tag.equalsIgnoreCase("health")) {
                        id = 3;
                    } else if (tag.equalsIgnoreCase("science")) {
                        id = 4;
                    } else if (tag.equalsIgnoreCase("sports")) {
                        id = 5;
                    } else if (tag.equalsIgnoreCase("technology")) {
                        id = 6;
                    }
                    setUp(id, ja, imagesUrl, newsTitle);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setUp(int i, JSONArray ja, ArrayList<String> imagesUrl, ArrayList<String> newsTitle) {
        setRecyclerView(recycler[i], imagesUrl, newsTitle);
        layout[i].setVisibility(View.VISIBLE);
        array[i] = ja;
    }

    @Override
    public void onError(VolleyError error, String tag) {
        Toast.makeText(getApplicationContext(), "Something Wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }
    /*public void EntertainmentButton(View view) {
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
    }*/
}
