package app.km.in.dailybharat.CustomVolley;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by Sexy_Virus on 08/03/18.
 */

public class CustomJSONObjectRequest implements Response.Listener<JSONObject>, Response.ErrorListener {

    private VolleyResponse volleyResponse;
    private String tag;
    private JsonObjectRequest jsonObjectRequest;


    public CustomJSONObjectRequest(int method, String url, JSONObject jsonObject, String tag, VolleyResponse volleyResponse) {
        this.volleyResponse = volleyResponse;
        this.tag= tag;
        jsonObjectRequest = new JsonObjectRequest(method, url, jsonObject, this, this);
    }

    @Override
    public void onResponse(JSONObject response) {
        volleyResponse.onResponse(response, tag);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        volleyResponse.onError(error, tag);
    }

    public JsonObjectRequest getJsonObjectRequest() {
        return jsonObjectRequest;
    }
}
