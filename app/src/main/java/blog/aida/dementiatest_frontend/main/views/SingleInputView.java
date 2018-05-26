package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;

import blog.aida.dementiatest_frontend.main.models.Question;

/**
 * Created by aida on 24-May-18.
 */

public class SingleInputView {

    private Context context;
    private EditText input;

    private ViewGroup questionLayout;

    public SingleInputView(Context context, ViewGroup questionLayout) {

        this.context = context;
        this.questionLayout = questionLayout;

        input = new EditText(context);
        questionLayout.addView(input);

    }

    public String getInputAnswer() {
        return input.getText().toString();
    }
}
