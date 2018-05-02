package blog.aida.dementiatest_frontend.main.requests;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_OBJECT;

/**
 * Created by aida on 25-Apr-18.
 */

public class GetRequest extends JsonObjectRequest {

    private Activity activity;
    private int typeOfResponseExpected;

    public GetRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener, Activity currentActivity, int typeOfResponseExpected) {
        super(Method.GET, url, null, listener, errorListener);
        this.activity = currentActivity;
        this.typeOfResponseExpected = typeOfResponseExpected;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {

//            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            String jsonString = new String(response.data);
            JSONObject jsonResponse = new JSONObject();

//            jsonResponse.put("data", new JSONArray(jsonString));
            if(this.typeOfResponseExpected == RESPONSE_TYPE_OBJECT) {
                jsonResponse.put("data", new JSONObject(jsonString));
            } else {
                jsonResponse.put("data", new JSONArray(jsonString));
            }

            jsonResponse.put("headers", new JSONObject(response.headers));
            return Response.success(jsonResponse, null);
//            return Response.success(jsonResponse, HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException je) {
            je.printStackTrace();
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer "+ getToken());
        return headers;
    }

//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("Authorization", "Bearer "+ yourToken);
//        return params;
//    }

    //    @Override
//    public byte[] getBody() {
//        try {
//            return body == null ? null : body.getBytes("utf-8");
//        } catch (UnsupportedEncodingException uee) {
//            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", body, "utf-8");
//            return null;
//        }
//    }

    private String getToken() {
        SharedPreferences prefs = this.activity.getSharedPreferences("userSettings", Context.MODE_PRIVATE);
        String tokennnn = prefs.getString("token","");

        return tokennnn;
    }

}
