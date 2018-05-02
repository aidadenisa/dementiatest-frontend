package blog.aida.dementiatest_frontend.main.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.login.activities.LoginActivity;
import blog.aida.dementiatest_frontend.main.adapters.PersonalInformationTestAdapter;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.Patient;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.models.Test;
import blog.aida.dementiatest_frontend.main.models.TestConfiguration;
import blog.aida.dementiatest_frontend.main.requests.GetRequest;
import blog.aida.dementiatest_frontend.main.requests.PostRequest;
import blog.aida.dementiatest_frontend.main.requests.VolleyCallback;
import blog.aida.dementiatest_frontend.main.services.PatientService;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.REQUEST_URL;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_ARRAY;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_OBJECT;

public class PersonalInformationTestActivity extends AppCompatActivity {

    private static final int PERSONAL_HISTORY_TEST_ID = 100;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Question> questions;

    private PatientService patientService;
    private String patientId;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_test);

        recyclerView = (RecyclerView) findViewById(R.id.personal_info_questions_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        for (int i = 0; i < 100; i++) {
//            Question question = new Question();
//            question.setText("tEST " + i);
//            questions.add(question);
//        }// define an adapter

        queue = Volley.newRequestQueue(PersonalInformationTestActivity.this);
        questions = new ArrayList<>();

        GetRequest getQuestions = new GetRequest(
                REQUEST_URL + "/testconfigs/" + PERSONAL_HISTORY_TEST_ID,
                new Response.Listener<org.json.JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response != null) {


                            try {
                                JSONObject data = response.getJSONObject("data");

                                JSONArray dataQuestions = data.getJSONArray("questions");
//
                                for (int i = 0; i < dataQuestions.length(); i++) {

                                    JSONObject jsonObject = dataQuestions.getJSONObject(i);
                                    Question question = new Gson().fromJson(jsonObject.toString(), Question.class);
                                    questions.add(question);
                                }

                                mAdapter = new PersonalInformationTestAdapter(questions, PersonalInformationTestActivity.this);
                                recyclerView.setAdapter(mAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                },
                this,
                RESPONSE_TYPE_OBJECT
        );
        queue.add(getQuestions);

        patientId = getIntent().getStringExtra("PATIENT_ID");
//        int userId = Integer.parseInt(getIntent().getStringExtra("USER_ID"));
//
//        patientService.getPatientData(queue, userId, this, new VolleyCallback() {
//            @Override
//            public void onSuccess(Object result) {
//                patient = new Gson().fromJson(result.toString(), Patient.class);
//            }
//        });



    }

    public void submitAnswersToPersonalHistoryTest(Map<Integer,String> answers) {

        List<Answer> submittedAnswers = new ArrayList<>();

        for(int i = 0; i< questions.size(); i++ )  {
            if(answers.get(i) != null) {
                Answer currentAnswer = new Answer();

                currentAnswer.setAnswer(answers.get(i));
                currentAnswer.setQuestion(questions.get(i));

                submittedAnswers.add(currentAnswer);
            }
        }

        PostRequest submitAnswers = new PostRequest(
                submittedAnswers,
                REQUEST_URL + "/patient/" + patientId + "/testConfig/" + PERSONAL_HISTORY_TEST_ID + "/answers",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Intent testsBoardIntent = new Intent(PersonalInformationTestActivity.this, TestsBoardActivity.class);
                        PersonalInformationTestActivity.this.startActivity(testsBoardIntent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int a=2;

                    }
                },
                this
        );

        queue.add(submitAnswers);
    }
}
