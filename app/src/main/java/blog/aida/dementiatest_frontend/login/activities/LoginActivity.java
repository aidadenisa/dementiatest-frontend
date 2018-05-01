package blog.aida.dementiatest_frontend.login.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.login.requests.LoginRequest;
import blog.aida.dementiatest_frontend.login.validators.DataValidator;
import blog.aida.dementiatest_frontend.main.activities.PersonalInformationTestActivity;
import blog.aida.dementiatest_frontend.main.models.Patient;
import blog.aida.dementiatest_frontend.main.models.UserAccount;
import blog.aida.dementiatest_frontend.main.services.PatientService;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPass;
    private Button btnLogin;
    private TextView registerLink;

    private TextInputLayout emailWrapper;
    private TextInputLayout passwordWrapper;

    private PatientService patientService;
    private RequestQueue queue;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        registerLink = findViewById(R.id.tvRegister);
        btnLogin = findViewById(R.id.btnLogin);

        emailWrapper = (TextInputLayout) findViewById(R.id.textInputEmail);
        passwordWrapper = (TextInputLayout) findViewById(R.id.textInputPass);

        patientService = new PatientService();
        queue = Volley.newRequestQueue(LoginActivity.this);

        gson = new Gson();

        registerLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean preventRegistration = false;

                if(!DataValidator.validateEmail(etEmail, emailWrapper)) {
                    preventRegistration = true;
                }

                if(!validatePassword()) {
                    preventRegistration = true;
                }

                if(preventRegistration) {
                    return;
                }

                UserAccount newUser = LoginActivity.this.createUserAccountObject();

                LoginActivity.this.loginUser(newUser);


            }
        });
    }

    private void loginUser(final UserAccount newUser) {

        Map<String,String> loginData = new HashMap<>();
        loginData.put("email", newUser.getEmail());
        loginData.put("hash", newUser.getHash());

        LoginRequest loginRequest = new LoginRequest(newUser, null, new Response.Listener<org.json.JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject responseHeaders, responseBody;

                if(response != null) {
                    try {
                        responseHeaders = response.getJSONObject("headers");
                        responseBody = response.getJSONObject("data");

                        String token = (responseHeaders.get("Authorization").toString().split(" "))[1];

                        storeToken(token);

                        logUserIntoApp(responseBody);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("aida request response " + error.getMessage());
                error.printStackTrace();
                LoginActivity.this.createErrorAlertDialog(error);
            }
        });

        queue.add(loginRequest);

    }

    private void logUserIntoApp(JSONObject responseData) {

        UserAccount loggedInUser = gson.fromJson(responseData.toString(), UserAccount.class);

        if(loggedInUser.getRole() == 0 ) {
            //this is a patient

            patientService.managePatientLogin(queue, loggedInUser, this);

        } else {
            //TODO: WHAT HAPPENS WHEN THIS IS A DOCTOR
        }



    }

    private void createErrorAlertDialog(VolleyError error) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Server Error");
        alertDialog.setMessage("There was a server error and your request could not be processed. Please retry.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private UserAccount createUserAccountObject() {

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(etEmail.getText().toString());
        userAccount.setHash(etPass.getText().toString());

        return userAccount;
    }

    private boolean validatePassword() {

        String pass1 = etPass.getText().toString();

        if(pass1.length() < 8) {
           return false;
        }

        return true;
    }

    private void storeToken(String token) {
        SharedPreferences settings = this.getSharedPreferences("userSettings", Context.MODE_PRIVATE);;
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("token",token);
        edit.apply();
    }

//    private String getToken() {
//        SharedPreferences prefs = getSharedPreferences("userSettings",Context.MODE_PRIVATE);
//        String tokennnn = prefs.getString("token","");
//
//        return tokennnn;
//    }
}
