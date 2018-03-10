package app.km.in.dailybharat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    VerticalViewPagerMy verticalViewPager;
    List<NewsDetailsModel> newsDetailsArrayList;
    ProgressDialog pd;
    SharedPreferences sp;
    public static View rootView;
    String type;
    String[] typeNews = {"entertainment", "business", "news", "health", "science", "sports", "technology"};
    int[] navId = {R.id.nav_entertainment, R.id.nav_business, R.id.nav_news, R.id.nav_health, R.id.nav_science, R.id.nav_sports, R.id.nav_technology};
    ImageView toggelButton;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp= getSharedPreferences("NightMode", MODE_PRIVATE);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(0);
        //toolbar.setVisibility(View.GONE);
        try {
            getBundle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setProgressDialog();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggelButton= findViewById(R.id.toggleButton);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.openDrawer(GravityCompat.START);
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setSelectedNavigation(type, navigationView);
        setTitle("");
        initViewPager();
        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
    }

    private void setSelectedNavigation(String type, NavigationView navigationView) {
        for (int i=0; i<typeNews.length;i++){
            if (type.equalsIgnoreCase(typeNews[i])){
                navigationView.setCheckedItem(navId[i]);
            }
        }
    }

    private void getBundle() throws JSONException {
        Bundle b= getIntent().getExtras();
        if (b!=null){
            String D= b.getString("Data");
            type= b.getString("type");
            JSONArray ja = new JSONArray(D);
            NewsDetailsModel newsModel;
            newsDetailsArrayList = new ArrayList<NewsDetailsModel>();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                newsModel = new NewsDetailsModel();
                newsModel.setNews_author(jsonObject.getString("author"));
                newsModel.setNews_title(jsonObject.getString("title"));
                newsModel.setNews_description(jsonObject.getString("description"));
                newsModel.setNews_url(jsonObject.getString("url"));
                newsModel.setNews_urlToImage(jsonObject.getString("urlToImage"));
                newsModel.setNews_publishedAt(jsonObject.getString("publishedAt"));

                System.out.println("the " + jsonObject.getString("publishedAt"));
                newsDetailsArrayList.add(newsModel);

            }
        }
    }

    private void initViewPager() {
        //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        verticalViewPager = (VerticalViewPagerMy) findViewById(R.id.verticleViewPager);
        verticalViewPager.setAdapter(new VerticalPagerAdapter(this, newsDetailsArrayList,"news"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            String myUrl = "https://newsapi.org/v2/top-headlines?country=in&apiKey=27e55646d2a24b8eb1c8d26457d6d318";
            getNews(myUrl,"news");
            // Handle the camera action
        } else if (id == R.id.nav_entertainment) {
            String myUrl = "https://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=1e2be97ff1724f6e883cecb345e1e65a";
            getNews(myUrl,"entertainment");

        } else if (id == R.id.nav_business) {
            String myUrl = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=1e2be97ff1724f6e883cecb345e1e65a";
            getNews(myUrl,"buisiness");
        } else if (id == R.id.nav_health) {
            String myUrl = "https://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=69cfcd1cca3f44a9b65a57137645d1fe";
            getNews(myUrl,"health");

        } else if (id == R.id.nav_science) {
            String myUrl = "https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=69cfcd1cca3f44a9b65a57137645d1fe";
            getNews(myUrl,"science");

        } else if (id == R.id.nav_sports) {
            String myUrl = "https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=27e55646d2a24b8eb1c8d26457d6d318";
            getNews(myUrl,"sports");

        } else if (id == R.id.nav_technology) {
            String myUrl = "https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=27e55646d2a24b8eb1c8d26457d6d318";
            getNews(myUrl,"technology");

        }else if (id== R.id.nav_night){
            startActivity(new Intent(MainActivity.this, NightSettingActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getNews(String myUrl, final String category) {
        pd.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // final String url = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=7da36c6cc7ee4a5d886bb6a2abd03faa";

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonRes) {
                        NewsDetailsModel newsModel;
                        // Toast.makeText(getActivity(), ""+jsonRes.toString(), Toast.LENGTH_SHORT).show();
                        //System.out.println("the responce is..."+jsonRes.toString());

                        newsDetailsArrayList = new ArrayList<NewsDetailsModel>();
                        try {
                            String status = jsonRes.getString("status");
                            int total = jsonRes.getInt("totalResults");
                            if (status.equals("ok")) {
                                JSONArray ja = jsonRes.getJSONArray("articles");
                                newsDetailsArrayList = new ArrayList<NewsDetailsModel>();
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jsonObject = ja.getJSONObject(i);
                                    newsModel = new NewsDetailsModel();
                                    newsModel.setNews_author(jsonObject.getString("author"));
                                    newsModel.setNews_title(jsonObject.getString("title"));
                                    newsModel.setNews_description(jsonObject.getString("description"));
                                    newsModel.setNews_url(jsonObject.getString("url"));
                                    newsModel.setNews_urlToImage(jsonObject.getString("urlToImage"));
                                    newsModel.setNews_publishedAt(jsonObject.getString("publishedAt"));

                                    System.out.println("the " + jsonObject.getString("publishedAt"));
                                    newsDetailsArrayList.add(newsModel);
                                    pd.dismiss();
                                    verticalViewPager.setAdapter(new VerticalPagerAdapter(MainActivity.this, newsDetailsArrayList,category));

                                }


                            }
                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        } catch (ParseException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }

                        //addedToAdapter(newsDetailsArrayList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Something Wrong: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // add it to the RequestQueue
        queue.add(getRequest);
    }

    private void setProgressDialog() {
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.setMessage("Getting news please wait");
        pd.setTitle("Fetching..");
    }

}
