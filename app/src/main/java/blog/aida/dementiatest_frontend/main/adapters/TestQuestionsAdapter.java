package blog.aida.dementiatest_frontend.main.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import blog.aida.dementiatest_frontend.main.fragments.QuestionFragment;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.models.TestConfiguration;

/**
 * Created by aida on 04-May-18.
 */

public class TestQuestionsAdapter extends FragmentStatePagerAdapter {

    private TestConfiguration testConfiguration;
    private List<Question> questions;

    private List<Answer> answers;

    public TestQuestionsAdapter(FragmentManager fragmentManager, TestConfiguration testConfiguration) {
        super(fragmentManager);
        this.testConfiguration = testConfiguration;
        this.questions = testConfiguration.getQuestions();
    }

    @Override
    public Fragment getItem(int position) {

        Fragment questionFragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pageNumber", position+1);
        if( getCount() == position + 1 ) {
            bundle.putBoolean("finalQuestion", true);
        }
        if( position < questions.size()) {
            bundle.putSerializable("question",questions.get(position));
        }
        questionFragment.setArguments(bundle);

        return questionFragment;
    }

    @Override
    public int getCount() {
        return questions.size() + 1;
    }
}
