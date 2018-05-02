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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.adapters.PersonalInformationTestAdapter;
import blog.aida.dementiatest_frontend.main.adapters.TestListAdapter;
import blog.aida.dementiatest_frontend.main.models.Test;
import blog.aida.dementiatest_frontend.main.models.TestConfiguration;
import blog.aida.dementiatest_frontend.main.requests.GetRequest;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.REQUEST_URL;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_ARRAY;

public class TestsBoardActivity extends AppCompatActivity {

    private RecyclerView testListRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RequestQueue queue;
    private List<TestConfiguration> tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests_board);

        testListRecyclerView = findViewById(R.id.test_list_recycler_view);

        testListRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        testListRecyclerView.setLayoutManager(layoutManager);

        queue = Volley.newRequestQueue(TestsBoardActivity.this);

        tests = new ArrayList<>();

        GetRequest getTests = new GetRequest(
                REQUEST_URL + "/testconfigs",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response != null) {

                            try {
                                JSONArray data = response.getJSONArray("data");

                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject currentTest = data.getJSONObject(i);
                                    TestConfiguration test = new Gson().fromJson(currentTest.toString(), TestConfiguration.class);
                                    tests.add(test);
                                }

                                adapter = new TestListAdapter(tests, TestsBoardActivity.this);
                                testListRecyclerView.setAdapter(adapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                },
                this,
                RESPONSE_TYPE_ARRAY
        );

        queue.add(getTests);


    }
}