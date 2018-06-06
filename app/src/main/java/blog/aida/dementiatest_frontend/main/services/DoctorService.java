package blog.aida.dementiatest_frontend.main.services;
import android.app.Activity;
import android.content.Intent;

import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.main.activities.PatientsListActivity;
import blog.aida.dementiatest_frontend.main.models.Doctor;
import blog.aida.dementiatest_frontend.main.models.UserAccount;
import blog.aida.dementiatest_frontend.main.requests.GetRequest;
import blog.aida.dementiatest_frontend.main.requests.PostRequest;
import blog.aida.dementiatest_frontend.main.requests.VolleyCallback;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.REQUEST_URL;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_ARRAY;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_OBJECT;

/**
 * Created by aida on 04-Jun-18.
 */

public class DoctorService {

    private Doctor doctor;
    private GsonService gson;

    public DoctorService() {
        doctor = new Doctor();
        gson = new GsonService();
    }

    public void manageDoctorLogin(final RequestQueue queue, final UserAccount userAccount, final Activity currentActivity) {

        GetRequest getDoctorData = new GetRequest(
                REQUEST_URL + "/users/" + userAccount.getId() + "/doctor",
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject responseHeaders, responseBody;

                        if(response != null) {
                            try {
                                responseBody = response.getJSONObject("data");

                                if(responseBody == null) {

                                    createNewDoctor(queue, userAccount, currentActivity);

                                } else {

                                    doctor = gson.getBuilder().fromJson(responseBody.toString(), Doctor.class);

                                    Intent patientListIntent = new Intent(currentActivity, PatientsListActivity.class);
                                    patientListIntent.putExtra("DOCTOR_INFO", doctor);
                                    currentActivity.startActivity(patientListIntent);

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
                if(error.getClass().equals(ParseError.class)) {
                    //TODO: CHECK IF THIS SHOULD BE UN-COMMENTED
                    createNewDoctor(queue, userAccount, currentActivity);
                }
            }
        },
                currentActivity,
                RESPONSE_TYPE_OBJECT
        );

        queue.add(getDoctorData);

    }

    public void createNewDoctor(final RequestQueue queue, final UserAccount loggedInUser, final Activity currentActivity) {

        PostRequest createNewDoctorWithAccount = new PostRequest(
                null,
                REQUEST_URL + "/users/" + loggedInUser.getId() + "/doctor",
                null,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject responseHeaders, responseBody;

                        if(response != null) {
                            try {
                                responseBody = response.getJSONObject("data");

                                if (responseBody != null) {

                                    Doctor doctor = new Gson().fromJson(responseBody.toString(), Doctor.class);
                                    doctor.setUserAccount(loggedInUser);

                                    Intent patientListIntent = new Intent(currentActivity.getApplicationContext(), PatientsListActivity.class);
                                    patientListIntent.putExtra("DOCTOR_INFO", (Serializable) doctor);
                                    currentActivity.startActivity(patientListIntent);

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
            }, currentActivity,
                RESPONSE_TYPE_OBJECT
        );

        queue.add(createNewDoctorWithAccount);

    }

    public void getAllDoctors(final RequestQueue queue, final Activity currentActivity, final VolleyCallback callback) {

        GetRequest getAllDoctors = new GetRequest(
                REQUEST_URL + "/doctors",
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray responseBody;

                        if(response != null) {
                            try {
                                responseBody = response.getJSONArray("data");

                                if(responseBody != null) {

                                    Type listType = new TypeToken<ArrayList<Doctor>>(){}.getType();
                                    List<Doctor> doctors = gson.getBuilder().fromJson(responseBody.toString(), listType);

                                    callback.onSuccess(doctors);
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
                RESPONSE_TYPE_ARRAY
        );

        queue.add(getAllDoctors);
    }
}
