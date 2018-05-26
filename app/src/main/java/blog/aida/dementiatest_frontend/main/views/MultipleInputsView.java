package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aida on 24-May-18.
 */

public class MultipleInputsView {

    private Context context;
    private List<EditText> inputs;

    private ViewGroup questionLayout;

    private final static int NO_OF_INPUTS_FOR_MULTIPLE_ANSWERS_QUESTION = 12;

    public MultipleInputsView(Context context, ViewGroup questionLayout) {

        this.context = context;
        this.questionLayout = questionLayout;

        inputs = new ArrayList<>();

        for(int i=0; i< NO_OF_INPUTS_FOR_MULTIPLE_ANSWERS_QUESTION; i++) {
            EditText input = new EditText(context);
            inputs.add(input);
            questionLayout.addView(input);
        }

    }

    public String getMultipleInputsAnswer() {
        String answer = inputs.get(0).getText().toString();
        for(int i=1; i< NO_OF_INPUTS_FOR_MULTIPLE_ANSWERS_QUESTION; i++) {
            answer = answer + "|" + inputs.get(i).getText().toString();
        }
        return answer;
    }
}
