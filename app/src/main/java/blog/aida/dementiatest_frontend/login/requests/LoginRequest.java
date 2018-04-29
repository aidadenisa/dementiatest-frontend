package blog.aida.dementiatest_frontend.login.requests;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import blog.aida.dementiatest_frontend.main.models.UserAccount;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.REQUEST_URL;

/**
 * Created by aida on 15-Apr-18.
 */

public class LoginRequest extends JsonObjectRequest {

    private static final String LOGIN_URL = REQUEST_URL + "/login";
    private String body;
    private Gson gson;

    public LoginRequest(UserAccount userAccount, JSONObject jsonObject, Response.Listener<org.json.JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_URL, jsonObject , listener, errorListener);
        gson = new GsonBuilder().create();
        this.body = gson.toJson(userAccount);
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
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("data", new JSONObject(jsonString));
            jsonResponse.put("headers", new JSONObject(response.headers));

            return Response.success(jsonResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

//    @Override
//    public String getBodyContentType() {
//        return "application/json; charset=utf-8";
//    }
//
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }



}
