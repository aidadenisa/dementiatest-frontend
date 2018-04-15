package blog.aida.dementiatest_frontend.login.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import blog.aida.dementiatest_frontend.main.activities.models.UserAccount;

/**
 * Created by aida on 08-Apr-18.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_URL = "http://10.11.31.5:8090/register";
    private String body;
    private Gson gson;

    public RegisterRequest(UserAccount userAccount, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_URL, listener, errorListener);
        gson = new GsonBuilder().create();
        this.body = gson.toJson(userAccount);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
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
}
