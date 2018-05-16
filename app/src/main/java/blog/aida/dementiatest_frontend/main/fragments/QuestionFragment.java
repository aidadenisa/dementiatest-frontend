package blog.aida.dementiatest_frontend.main.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.activities.PersonalInformationTestActivity;
import blog.aida.dementiatest_frontend.main.models.ConnectPoints;
import blog.aida.dementiatest_frontend.main.views.ConnectPointsView;
import blog.aida.dementiatest_frontend.main.views.DrawingView;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.views.QuestionViewPager;

/**
 * Created by aida on 04-May-18.
 */

public class QuestionFragment extends Fragment {

    private Question question;

    private ViewGroup container;

    private ViewGroup questionLayout;

    private Context context;

    private Button btnNextQuestion;
    private Button btnPreviousQuestion;

    private QuestionViewPager viewPager;

    private View view;

    private final static int NO_OF_INPUTS_FOR_MULTIPLE_ANSWERS_QUESTION = 12;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int pageNumber = bundle.getInt("pageNumber");
        question = (Question) bundle.getSerializable("question");

        this.container = container;

        this.context = getContext();


        view = inflater.inflate(R.layout.fragmet_test_question, container, false);
        TextView textView = view.findViewById(R.id.test_question_text);
        textView.setText(question.getText());

        viewPager = (QuestionViewPager) getActivity().findViewById(R.id.view_pager);


        btnNextQuestion = view.findViewById(R.id.test_next_btn);
        btnPreviousQuestion = view.findViewById(R.id.test_back_btn);

        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        btnPreviousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });


        questionLayout = view.findViewById(R.id.test_question_layout);


        renderQuestion();

        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void renderQuestion() {
        if(question.getInstructions() != null) {
            this.renderInstructions();
        }

        if(question.getDateConfiguration() != null && question.getDateConfiguration()) {
            this.renderDateQuestion();
        }

        if(question.getImage1() != null) {
           renderOneImageExercise();
        }

        if(question.getInputConfiguration() != null && question.getInputConfiguration()) {
            renderInputConfiguration();
        }

        if(question.getDrawingConfiguration() != null && question.getDrawingConfiguration()) {
            renderDrawingConfiguration();
        }

        if(question.getMultipleTextConfiguration() != null) {
            renderMultipleAnswers();
        }

        if(question.getPoints().size() > 0) {
            renderConnectPointsExercise();
        }

//        if(question.getChoices() != null) {
//            this.renderChoices(currentQuestion, holder);
//        }
//
//        if(currentQuestion.getYesOrNoConfiguration() != null && currentQuestion.getYesOrNoConfiguration()) {
//            this.renderYesOrNoConfiguration(currentQuestion, holder);
//        }



//        if(currentQuestion.getInputConfiguration() != null && currentQuestion.getInputConfiguration()) {
//            this.renderInputText(currentQuestion, holder);
//        }
    }

    private void renderConnectPointsExercise() {

        List<ConnectPoints> points = question.getPoints();

        ConnectPointsView canvasView = new ConnectPointsView(context);

        canvasView.setPoints(points);

        questionLayout.addView(canvasView);
    }

    private void renderMultipleAnswers() {

        for(int i=0; i< NO_OF_INPUTS_FOR_MULTIPLE_ANSWERS_QUESTION; i++) {
            EditText newInput = new EditText(context);
            questionLayout.addView(newInput);
        }
    }

    private void renderDrawingConfiguration() {

        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeWidth(8);

        final DrawingView drawingView = new DrawingView(context,paint);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        drawingView.setLayoutParams(layoutParams);

        Button saveButton = new Button(context);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: SAVE THE DRAWING IN ANSWER
                byte[] drawing = drawingView.getDrawing();
            }
        });

        Button clearButton = new Button(context);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clearDrawing();
            }
        });

        questionLayout.addView(saveButton);
        questionLayout.addView(clearButton);
        questionLayout.addView(drawingView);



    }

    private void renderInputConfiguration() {
        EditText input = new EditText(context);
        questionLayout.addView(input);
    }

    private void renderOneImageExercise() {
        String imageName = question.getImage1();
        EditText imageAnswer = new EditText(context);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(getImageResourceFromName(imageName));

        questionLayout.addView(imageView);
        questionLayout.addView(imageAnswer);


    }

    private int getImageResourceFromName(String imageName) {

        switch(imageName) {
            case "wreath":
                return R.drawable.wreath;

            case "volcano":
                return R.drawable.volcano;

            default:
                return -1;
        }

    }

    private void renderTwoImagesExercise() {


    }

    private void renderDateQuestion() {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f);

        renderDateLayout(layoutParams, fieldParams, "Year");
        renderDateLayout(layoutParams, fieldParams, "Month");
        renderDateLayout(layoutParams, fieldParams, "Day");

    }

    private void renderDateLayout(LinearLayout.LayoutParams layoutParams, LinearLayout.LayoutParams fieldParams, String dateLabelText) {

        LinearLayout dateLayout = new LinearLayout(context);
        dateLayout.setOrientation(LinearLayout.HORIZONTAL);
        dateLayout.setLayoutParams(layoutParams);

        EditText dateInput = new EditText(context);
        dateInput.setLayoutParams(fieldParams);

        TextView dateLabel = new TextView(context);
        dateLabel.setText(dateLabelText);
        dateLabel.setLayoutParams(fieldParams);

        dateLayout.addView(dateLabel);
        dateLayout.addView(dateInput);

        questionLayout.addView(dateLayout);

    }

    private void renderInstructions() {

        TextView insctructions = new TextView(context);
        insctructions.setText(question.getInstructions());

        questionLayout.addView(insctructions);

    }
}
