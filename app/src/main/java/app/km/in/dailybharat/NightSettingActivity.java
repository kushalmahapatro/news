package app.km.in.dailybharat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Sexy_Virus on 28/02/18.
 */

public class NightSettingActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.night_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Spinner night = findViewById(R.id.nightModeSpinner);
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Off");
        spinnerArray.add("On");
        spinnerArray.add("Auto");

        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        night.setAdapter(adapter);
        if (nightValue.equalsIgnoreCase("off")){
           night.setSelection(0);
        }else  if (nightValue.equalsIgnoreCase("on")){
            night.setSelection(1);
        }else if (nightValue.equalsIgnoreCase("auto")){
            night.setSelection(2);
        }

        night.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = adapterView.getSelectedItemPosition();
                SharedPreferences.Editor edit= sp.edit();
                switch (pos) {
                    case 0:
                        AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_NO);
                        setTheme( AppCompatDelegate.getDefaultNightMode());
                        edit.putString("value","off");
                        edit.apply();
                        edit.commit();
                        break;
                    case 1:
                        AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_YES);
                        setTheme( AppCompatDelegate.getDefaultNightMode());
                        edit.putString("value","on");
                        edit.apply();
                        edit.commit();

                        break;
                    case 2:
                        setTheme( AppCompatDelegate.getDefaultNightMode());
                        AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_AUTO);
                        edit.putString("value","on");
                        edit.apply();
                        edit.commit();

                        break;
                    default:
                        setTheme( AppCompatDelegate.getDefaultNightMode());
                        AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_NO);
                        edit.putString("value","off");
                        edit.apply();
                        edit.commit();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NightSettingActivity.this, MainActivity.class);
        intent.putExtra("Data",SplashActivity.ja.toString());
        startActivity(intent);
    }
}
