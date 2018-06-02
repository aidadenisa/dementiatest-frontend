package blog.aida.dementiatest_frontend.main.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.adapters.TestQuestionsAdapter;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.models.Test;
import blog.aida.dementiatest_frontend.main.models.TestConfiguration;
import blog.aida.dementiatest_frontend.main.views.QuestionViewPager;

public class TestActivity extends AppCompatActivity {

    private QuestionViewPager viewPager;

    private List<Answer> answers;

    private TestConfiguration testConfig;

    private String patientId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testConfig = (TestConfiguration) getIntent().getSerializableExtra("testConfig");

        patientId = getIntent().getStringExtra("patientId");

        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
        TestQuestionsAdapter testQuestionsAdapter = new TestQuestionsAdapter(getSupportFragmentManager(), testConfig);
        viewPager.setAdapter(testQuestionsAdapter);
        viewPager.setCurrentItem(0);

        this.answers = new ArrayList<>();
    }

    public void saveAnswerToAnswersList(String answerText, Question question) {

        boolean existingAnswer = false;

        for( int i = 0; i < answers.size(); i++ ) {
            if (answers.get(i).getQuestion().equals(question)) {
                answers.get(i).setAnswer(answerText);
                existingAnswer = true;
            }
        }

        if(!existingAnswer) {
            Answer answer = new Answer();
            answer.setAnswer(answerText);
            answer.setQuestion(question);
            answers.add(answer);
        }

    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public int getTestConfigurationId() {
       return testConfig.getId();
    }

    public int getPatientId() {
        return Integer.parseInt(patientId);
    }

}
