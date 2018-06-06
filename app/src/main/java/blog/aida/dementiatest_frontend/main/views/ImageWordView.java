package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.services.ImageManager;

/**
 * Created by aida on 24-May-18.
 */

public class ImageWordView {

    private Context context;
    private EditText imageAnswer;

    private ViewGroup questionLayout;

    public ImageWordView(Context context, ViewGroup questionLayout, Question question) {

        this.context = context;
        this.questionLayout = questionLayout;

        String imageName = question.getImage1();

        imageAnswer = new EditText(context);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(ImageManager.getImageResourceFromName(imageName));

        questionLayout.addView(imageView);
        questionLayout.addView(imageAnswer);
    }



    public String getWordAnswer() {
        return imageAnswer.getText().toString();
    }
}
