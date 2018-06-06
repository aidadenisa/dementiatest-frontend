package blog.aida.dementiatest_frontend.main.services;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import blog.aida.dementiatest_frontend.main.activities.PersonalInformationTestActivity;
import blog.aida.dementiatest_frontend.main.activities.TestsBoardActivity;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.Doctor;
import blog.aida.dementiatest_frontend.main.models.Patient;
import blog.aida.dementiatest_frontend.main.models.UserAccount;
import blog.aida.dementiatest_frontend.main.requests.GetRequest;
import blog.aida.dementiatest_frontend.main.requests.PostRequest;
import blog.aida.dementiatest_frontend.main.requests.VolleyCallback;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.REQUEST_URL;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_ARRAY;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_OBJECT;

/**
 * Created by aida on 29-Apr-18.
 */

public class PatientService {

    private Patient patient;
    private GsonService gson;

    public PatientService() {
        patient = new Patient();
        gson = new GsonService();
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

                                } else {
//                                    // GO TO THE TESTS PAGE

                                    JSONArray takenTests = (JSONArray) responseBody.get("takenTests");

                                    if(takenTests.length() < 1) {

                                        Intent internalTestIntent = new Intent(currentActivity, PersonalInformationTestActivity.class);
                                        internalTestIntent.putExtra("PATIENT_ID", responseBody.get("id")+ "");
                                        currentActivity.startActivity(internalTestIntent);

                                    } else {

                                        Intent testsBoardIntent = new Intent(currentActivity, TestsBoardActivity.class);
                                        testsBoardIntent.putExtra("PATIENT_ID", responseBody.get("id")+ "");
                                        currentActivity.startActivity(testsBoardIntent);

                                    }
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
                    //TODO: CHECK IF THIS SHOULD BE UN-COMMENTED
                    if(error.getClass().equals(ParseError.class)) {
                        createNewPatient(queue, userAccount, currentActivity);
                    }
                }
            },
                currentActivity,
                RESPONSE_TYPE_OBJECT
        );

        queue.add(getPatientData);
    }

    public void createNewPatient(final RequestQueue queue, final UserAccount loggedInUser, final Activity currentActivity) {

        PostRequest createNewPatientWithAccount = new PostRequest(
                null,
                REQUEST_URL + "/users/" + loggedInUser.getId() + "/patient",
                null,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        getPatientData(queue, loggedInUser.getId(), currentActivity, new VolleyCallback() {
                            @Override
                            public void onSuccess(Object result) {

                                Patient patientData = new Gson().fromJson(new Gson().toJson(result).toString(), Patient.class);

                                Intent internalTestIntent = new Intent(currentActivity.getApplicationContext(), PersonalInformationTestActivity.class);
                                internalTestIntent.putExtra("PATIENT_ID", patientData.getId()+ "");
////                                    internalTestIntent.putExtra("USER_ID", userAccount.getId());
                                currentActivity.startActivity(internalTestIntent);

                            }
                        });

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

        queue.add(createNewPatientWithAccount);

    }

    public void getPatientData(final RequestQueue queue, final long userId, final Activity currentActivity, final VolleyCallback callback) {

        GetRequest getPatientDataAfterLogin = new GetRequest(
                REQUEST_URL + "/users/" + userId + "/patient",
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject responseHeaders, responseBody;

                        if(response != null) {
                            try {
                                responseBody = response.getJSONObject("data");

                                final Gson builder = new GsonBuilder()
                                        .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                            public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
                                                return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
                                            }
                                        })
                                        .create();
                                Patient patient = builder.fromJson(responseBody.toString(), Patient.class);

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

    public void savePatientOfDoctor(final RequestQueue queue, String patientId, Doctor doctor, Activity currentActivity, final VolleyCallback callback) {

        PostRequest saveExistingPatientToDoctor = new PostRequest(
                null,
                REQUEST_URL + "/doctors/" + doctor.getId() + "/patients/" + patientId,
                null,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Gson builder = gson.getBuilder();
                            callback.onSuccess(builder.fromJson(builder.toJson(response.get("data")),Patient.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
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

        queue.add(saveExistingPatientToDoctor);

    }

    public void getAnswersOfTakenTest(RequestQueue queue, long patientId, long testId, Activity currentActivity, final VolleyCallback callback) {

        // /patient/{patientId}/test/{testId}/answers
        GetRequest getPatientDataAfterLogin = new GetRequest(
                REQUEST_URL + "/patient/" + patientId + "/test/" + testId + "/answers",
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONArray responseBody;

                        if(response != null) {
                            try {
                                responseBody = response.getJSONArray("data");

                                Type listType = new TypeToken<ArrayList<Answer>>(){}.getType();
                                List<Answer> answers = gson.getBuilder().fromJson(responseBody.toString(), listType);

                                callback.onSuccess(answers);

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

        queue.add(getPatientDataAfterLogin);

    }
}
