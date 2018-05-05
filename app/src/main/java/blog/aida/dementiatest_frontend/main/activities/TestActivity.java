package blog.aida.dementiatest_frontend.main.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.adapters.TestQuestionsAdapter;
import blog.aida.dementiatest_frontend.main.models.TestConfiguration;
import blog.aida.dementiatest_frontend.main.views.QuestionViewPager;

public class TestActivity extends AppCompatActivity {

    private QuestionViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TestConfiguration testConfig = (TestConfiguration) getIntent().getSerializableExtra("testConfig");


        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        TestQuestionsAdapter testQuestionsAdapter = new TestQuestionsAdapter(getSupportFragmentManager(), testConfig);
        viewPager.setAdapter(testQuestionsAdapter);
        viewPager.setCurrentItem(0);



    }

}
