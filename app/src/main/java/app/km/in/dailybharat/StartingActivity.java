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

public class StartingActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> bannerImages = new ArrayList<String>();
    private ArrayList<String> bannerTitle = new ArrayList<String>();

    RecyclerAdapter adapter;
    RecyclerView entertainmentRecycler, newsRecycler, businessRecycler;
    LinearLayoutManager layoutManager
            = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    JSONArray ja, jaE;
    Button ent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp= getSharedPreferences("NightMode", MODE_PRIVATE);
        String nightValue= sp.getString("value","off");
        if (nightValue.equalsIgnoreCase("off")){
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }else  if (nightValue.equalsIgnoreCase("on")){
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        }else if (nightValue.equalsIgnoreCase("auto")){
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
       // getData("Entertainment");
     //   getData("Business");
      //  getData("News");

       getEntertainmentData();

    }


    private void getBusinessData() {
        final ArrayList<String> imagesUrl= new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url="https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=1e2be97ff1724f6e883cecb345e1e65a";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonRes)
                    {

                        try {
                            String status = jsonRes.getString("status");
                            int total= jsonRes.getInt("totalResults");
                            if (status.equals("ok")) {
                                JSONArray ja = jsonRes.getJSONArray("articles");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jsonObject = ja.getJSONObject(i);
                                    imagesUrl.add(jsonObject.getString("urlToImage"));
                                    setRecyclerView(businessRecycler,imagesUrl);
                                }

                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //addedToAdapter(newsDetailsArrayList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Something Wrong: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // add it to the RequestQueue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest,"business");
        getRequest.setRetryPolicy(
                new DefaultRetryPolicy(2 * 1000, 1, 1.0f));
    }

    private void getEntertainmentData() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url="https://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=1e2be97ff1724f6e883cecb345e1e65a";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonRes)
                    {

                        try {
                            String status = jsonRes.getString("status");
                            int total= jsonRes.getInt("totalResults");
                            final ArrayList<String> imagesUrl= new ArrayList<>();
                            if (status.equals("ok")) {
                                JSONArray ja = jsonRes.getJSONArray("articles");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jsonObject = ja.getJSONObject(i);
                                    imagesUrl.add(jsonObject.getString("urlToImage"));
                                    setRecyclerView(entertainmentRecycler,imagesUrl);
                                    ent.setVisibility(View.VISIBLE);
                                    jaE=ja;
                                }

                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //addedToAdapter(newsDetailsArrayList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Something Wrong: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // add it to the RequestQueue
        queue.add(getRequest);
       /* AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest,"ent");
        getRequest.setRetryPolicy(
                new DefaultRetryPolicy(2 * 1000, 1, 1.0f));*/
    }

    private void getData(String type) {
        String url = null;
        switch (type){
            case "Entertainment":
                url="https://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=1e2be97ff1724f6e883cecb345e1e65a";
                break;
            case "Business":
                url="https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=1e2be97ff1724f6e883cecb345e1e65a";
                break;
            case "News":
                url="https://newsapi.org/v2/top-headlines?country=in&apiKey=27e55646d2a24b8eb1c8d26457d6d318";
                break;
        }
        networkAction(url,type);


    }

    private void setRecyclerView(RecyclerView view, ArrayList<String> images) {
        view.setLayoutManager(layoutManager);
        adapter= new RecyclerAdapter(getApplicationContext(),getData(images));
        view.setAdapter(adapter);

    }

    private ArrayList<ImagesModel> getData(ArrayList<String> url) {
        ArrayList<ImagesModel> Data= new ArrayList<>();
        for (int i=0; i<url.size();i++){
            ImagesModel image= new ImagesModel(url.get(i));
            Data.add(image);
        }
        return Data;
    }

    private void networkAction(String url, final String type) {
        final ArrayList<String> imagesUrl= new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonRes)
                    {

                        try {
                            String status = jsonRes.getString("status");
                            int total= jsonRes.getInt("totalResults");
                            if (status.equals("ok")) {
                                JSONArray ja = jsonRes.getJSONArray("articles");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jsonObject = ja.getJSONObject(i);
                                    imagesUrl.add(jsonObject.getString("urlToImage"));
                                    switch (type){
                                        case "Entertainment":
                                            setRecyclerView(entertainmentRecycler,imagesUrl);
                                        case "Business":
                                            setRecyclerView(businessRecycler,imagesUrl);
                                        case "News":
                                            setRecyclerView(newsRecycler,imagesUrl);
                                    }

                                }

                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //addedToAdapter(newsDetailsArrayList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Something Wrong: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // add it to the RequestQueue
        //queue.add(getRequest);
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest,type);
        getRequest.setRetryPolicy(
                new DefaultRetryPolicy(2 * 1000, 1, 1.0f));
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new BannerAdapter(StartingActivity.this,bannerImages,bannerTitle));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        entertainmentRecycler= findViewById(R.id.entertainment_recycler);
        newsRecycler= findViewById(R.id.news_recycler);
        businessRecycler= findViewById(R.id.business_recycler);
        ent= findViewById(R.id.entButton);
        ent.setVisibility(View.GONE);

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
        Bundle b= getIntent().getExtras();
        if (b!=null){
            String D= b.getString("Data");
            ja = new JSONArray(D);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                bannerImages.add(jsonObject.getString("urlToImage"));
                bannerTitle.add(jsonObject.getString("title"));
            }
        }
    }

    public void EntertainmentButton(View view) {
        Intent intent = new Intent(StartingActivity.this, MainActivity.class);
        intent.putExtra("Data",jaE.toString());
        startActivity(intent);
    }
}
