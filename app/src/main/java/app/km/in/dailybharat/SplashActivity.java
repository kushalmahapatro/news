package app.km.in.dailybharat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Downloader;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sexy_Virus on 23/02/18.
 */

@SuppressLint("Registered")
public class SplashActivity extends AppCompatActivity {
    private final static String LOG_TAG = SplashActivity.class.getSimpleName();

    // User Session Manager Class

  //  ImageView splashImage;
    //   private ProgressBar spinner;
    AnimationDrawable loadingProgressAnimation;
    FrameLayout statusBar;

    private Handler mHandler = new Handler();

    public static String CompanyName;
    private static final String PREFER_NAME = "AndroidExamplePref";
    public static final String KEY_NAME = "name";
    public static final String KEY_COMPANYNAME = "CompanyName";
    SharedPreferences sp;
    TextView UserNumber;
    List<NewsDetailsModel> newsDetailsArrayList;
    public static JSONArray ja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setContentView(R.layout.activity_splash);

       /* sp = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        String number = sp.getString(KEY_NAME, null);
        location = new ArrayList<String>();
        Department = new ArrayList<String>();
        EmpName = new ArrayList<String>();
        Designation = new ArrayList<String>();
        Phone_number = new ArrayList<String>();
        Emp_EmailID = new ArrayList<String>();
        Reporting_MailID = new ArrayList<String>();*/

        //  getSupportActionBar().getThemedContext();

        /*spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);*/
       /* splashImage = (ImageView)findViewById(R.id.splashImage);
        loadingProgressAnimation = (AnimationDrawable) splashImage.getBackground();

        // Session class instance
       // session = new UserSessionManager(getApplicationContext());
//        spinner.setVisibility(View.VISIBLE);
        splashImage.setVisibility(View.VISIBLE);
        loadingProgressAnimation.start();*/
        mHandler.postDelayed(new Runnable() {
            public void run() {
                doStuff();

            }
        }, 4000);
    }

    private void doStuff() {


            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            // final String url = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=7da36c6cc7ee4a5d886bb6a2abd03faa";
        String myUrl="https://newsapi.org/v2/top-headlines?country=in&apiKey=27e55646d2a24b8eb1c8d26457d6d318";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonRes)
                        {
                            NewsDetailsModel newsModel;
                            // Toast.makeText(getActivity(), ""+jsonRes.toString(), Toast.LENGTH_SHORT).show();
                            //System.out.println("the responce is..."+jsonRes.toString());

                            newsDetailsArrayList = new ArrayList<NewsDetailsModel>();
                            try {
                                String status = jsonRes.getString("status");
                                int total= jsonRes.getInt("totalResults");
                                if (status.equals("ok")) {
                                    ja = jsonRes.getJSONArray("articles");
                                    Intent intent = new Intent(SplashActivity.this, StartingActivity.class);
                                    intent.putExtra("Data",ja.toString());
                                    startActivity(intent);

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

        }


    // Check user login
   /* private void doStuff() {

        // If User is not logged in , This will redirect user to LoginActivity.
        if (session.isUserLoggedIn()) {
            // spinner.setVisibility(View.INVISIBLE);
            //  new MyTask().execute();
            splashImage.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            // spinner.setVisibility(View.INVISIBLE);
            splashImage.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(SplashActivity.this, ViewPagerDemo.class);
            startActivity(intent);
            finish();
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // do nothing.
        //for disable back button
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


}
