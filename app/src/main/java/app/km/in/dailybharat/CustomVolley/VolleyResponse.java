package app.km.in.dailybharat.CustomVolley;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Sexy_Virus on 08/03/18.
 */

public interface VolleyResponse {
    void onResponse(JSONObject object, String tag);

    void onError(VolleyError error, String tag);
}
