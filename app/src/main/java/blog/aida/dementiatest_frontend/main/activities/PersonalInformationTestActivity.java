package blog.aida.dementiatest_frontend.main.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.login.activities.LoginActivity;
import blog.aida.dementiatest_frontend.main.adapters.PersonalInformationTestAdapter;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.requests.GetRequest;

public class PersonalInformationTestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_test);

        recyclerView = (RecyclerView) findViewById(R.id.personal_info_questions_recycler_view);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
//        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final List<Question> questions = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            Question question = new Question();
//            question.setText("tEST " + i);
//            questions.add(question);
//        }// define an adapter

        RequestQueue queue = Volley.newRequestQueue(PersonalInformationTestActivity.this);

        GetRequest getQuestions = new GetRequest(
                "http://10.11.31.5:8090/testconfigs/51/",
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

                                mAdapter = new PersonalInformationTestAdapter(questions);
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
                this
        );

        queue.add(getQuestions);






    }
}
