package blog.aida.dementiatest_frontend.main.services;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import blog.aida.dementiatest_frontend.main.activities.PersonalInformationTestActivity;
import blog.aida.dementiatest_frontend.main.models.Patient;
import blog.aida.dementiatest_frontend.main.models.UserAccount;
import blog.aida.dementiatest_frontend.main.requests.GetRequest;
import blog.aida.dementiatest_frontend.main.requests.PostRequest;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.REQUEST_URL;

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

                                //TODO: UNCOMMENT THIS AFTER FINISHING WITH THE PERSONAL TEST DEVELOPMENT
//                                if(responseBody == null) {

//                                    createNewPatient(queue, userAccount, currentActivity);

                                    Intent internalTestIntent = new Intent(currentActivity.getApplicationContext(), PersonalInformationTestActivity.class);
                                    currentActivity.startActivity(internalTestIntent);

//                                } else {
//                                    //TODO: GO TO THE TESTS PAGE
//                                }

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
            }, currentActivity
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
}
