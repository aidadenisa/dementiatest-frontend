package blog.aida.dementiatest_frontend.main.requests;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import blog.aida.dementiatest_frontend.main.models.UserAccount;

/**
 * Created by aida on 29-Apr-18.
 */

public class PostRequest extends JsonObjectRequest {

    private Activity activity;
    private String body;
    private Gson gson;

    public PostRequest(Object o, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Activity currentActivity) {
        super(Method.POST, url, null, listener, errorListener);
        this.activity = currentActivity;
        gson = new GsonBuilder().create();
        this.body = gson.toJson(o);
    }

    @Override
    public byte[] getBody() {
        try {
            return body == null ? null : body.getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", body, "utf-8");
            return null;
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer "+ getToken());
        return headers;
    }

    private String getToken() {
        SharedPreferences prefs = this.activity.getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        String tokennnn = prefs.getString("token","");

        return tokennnn;
    }
}
