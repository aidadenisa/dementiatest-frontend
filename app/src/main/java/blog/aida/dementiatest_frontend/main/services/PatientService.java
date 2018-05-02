package blog.aida.dementiatest_frontend.main.services;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import blog.aida.dementiatest_frontend.main.activities.PersonalInformationTestActivity;
import blog.aida.dementiatest_frontend.main.activities.TestsBoardActivity;
import blog.aida.dementiatest_frontend.main.models.Patient;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.models.UserAccount;
import blog.aida.dementiatest_frontend.main.requests.GetRequest;
import blog.aida.dementiatest_frontend.main.requests.PostRequest;
import blog.aida.dementiatest_frontend.main.requests.VolleyCallback;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.REQUEST_URL;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_OBJECT;

/**
 * Created by aida on 29-Apr-18.
 */

public class PatientService {

    private Patient patient;

    public PatientService() {
        patient = new Patient();
    }

    public void managePatientLogin(final RequestQueue queue, final UserAccount userAccount, final Activity currentActivity) {

        GetRequest getPatientData = new GetRequest(
            REQUEST_URL + "/users/" + userAccount.getId() + "/patient",
            new Response.Listener<org.json.JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                        JSONObject responseHeaders, responseBody;

                        if(response != null) {
                            try {
                                responseBody = response.getJSONObject("data");

                                if(responseBody == null) {

                                    createNewPatient(queue, userAccount, currentActivity);

                                    Intent internalTestIntent = new Intent(currentActivity.getApplicationContext(), PersonalInformationTestActivity.class);
                                    internalTestIntent.putExtra("PATIENT_ID", responseBody.get("id").toString());
//                                    internalTestIntent.putExtra("USER_ID", userAccount.getId());
                                    currentActivity.startActivity(internalTestIntent);

                                } else {
//                                    // GO TO THE TESTS PAGE

                                    Intent testsBoardIntent = new Intent(currentActivity, TestsBoardActivity.class);
                                    currentActivity.startActivity(testsBoardIntent);

                                }

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
                }
            },
                currentActivity,
                RESPONSE_TYPE_OBJECT
        );

        queue.add(getPatientData);
    }

    public void createNewPatient(RequestQueue queue, UserAccount loggedInUser, Activity currentActivity) {

        PostRequest createNewPatientWithAccount = new PostRequest(
                null,
                REQUEST_URL + "/users/" + loggedInUser.getId() + "/patient",
                null,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("aida request response " + error.getMessage());
                        error.printStackTrace();
                    }
                }, currentActivity
        );

        queue.add(createNewPatientWithAccount);

    }

    public void getPatientData(final RequestQueue queue, final int userId, final Activity currentActivity, final VolleyCallback callback) {

        GetRequest getPatientDataAfterLogin = new GetRequest(
                REQUEST_URL + "/users/" + userId + "/patient",
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject responseHeaders, responseBody;

                        if(response != null) {
                            try {
                                responseBody = response.getJSONObject("data");

                                Patient patient = new Gson().fromJson(responseBody.toString(), Patient.class);

                                callback.onSuccess(patient);

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
                    }
                },
                currentActivity,
                RESPONSE_TYPE_OBJECT
        );

        queue.add(getPatientDataAfterLogin);
    }
}
