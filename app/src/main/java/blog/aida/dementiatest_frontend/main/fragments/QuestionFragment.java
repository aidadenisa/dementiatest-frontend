package blog.aida.dementiatest_frontend.main.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.activities.TestResultActivity;
import blog.aida.dementiatest_frontend.main.interfaces.CanvasBasedView;
import blog.aida.dementiatest_frontend.main.activities.TestActivity;
import blog.aida.dementiatest_frontend.main.activities.TestsBoardActivity;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.ConnectPoints;
import blog.aida.dementiatest_frontend.main.models.Test;
import blog.aida.dementiatest_frontend.main.requests.PostRequest;
import blog.aida.dementiatest_frontend.main.services.TestUtils;
import blog.aida.dementiatest_frontend.main.services.ToolbarManager;
import blog.aida.dementiatest_frontend.main.views.ConnectPointsView;
import blog.aida.dementiatest_frontend.main.views.DateView;
import blog.aida.dementiatest_frontend.main.views.DragAndDropView;
import blog.aida.dementiatest_frontend.main.views.DrawingView;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.views.ImageWordView;
import blog.aida.dementiatest_frontend.main.views.MultipleInputsView;
import blog.aida.dementiatest_frontend.main.views.QuestionViewPager;
import blog.aida.dementiatest_frontend.main.views.SingleInputView;

import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.REQUEST_URL;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_ARRAY;
import static blog.aida.dementiatest_frontend.main.requests.NetworkConfig.RESPONSE_TYPE_OBJECT;

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

    private DateView dateView;

    private ImageWordView imageWordView;
    private SingleInputView singleInputView;

    private DrawingView drawingView;

    private ConnectPointsView connectPointsView;

    private MultipleInputsView multipleInputsView;

    private DragAndDropView dragAndDropView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        int pageNumber = bundle.getInt("pageNumber");
        String testName = bundle.getString("testName");
        final boolean isFinalQuestion = bundle.getBoolean("finalQuestion");
        if(!isFinalQuestion) {
            question = (Question) bundle.getSerializable("question");
        } else {
            question = new Question();
        }


        this.container = container;

        this.context = getContext();

        viewPager = (QuestionViewPager) getActivity().findViewById(R.id.view_pager);

        if( isFinalQuestion ) {
            view = inflater.inflate(R.layout.submit_btn_dementia_test, container, false);

            Button saveButton = view.findViewById(R.id.dementia_test_submit_btn);

            final ProgressBar spinner = view.findViewById(R.id.spinner);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<Answer> answers = ((TestActivity)context).getAnswers();
                    final int patientId = ((TestActivity)context).getPatientId();
                    int testConfigurationId = ((TestActivity)context).getTestConfigurationId();
                    RequestQueue queue = Volley.newRequestQueue(context);

                    spinner.setVisibility(View.VISIBLE);

                    PostRequest submitAnswers = new PostRequest(
                            answers,
                            REQUEST_URL + "/patient/" + patientId + "/testConfig/" + testConfigurationId + "/answers",
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Gson gson = new Gson();
                                    Test test = new Test();

                                    try {
                                        test = gson.fromJson(response.get("data").toString(), Test.class);

                                        Intent testResultIntent = new Intent(context, TestResultActivity.class);
                                        testResultIntent.putExtra("TEST_SCORE", test.getScore() + "");
                                        context.startActivity(testResultIntent);

                                        spinner.setVisibility(View.INVISIBLE);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    spinner.setVisibility(View.INVISIBLE);
                                }
                            },
                            getActivity(),
                            RESPONSE_TYPE_OBJECT
                    );

                    submitAnswers.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(submitAnswers);

                }
            });
        } else {
            view = inflater.inflate(R.layout.fragmet_test_question, container, false);

            ToolbarManager.setupToolbar(view,(AppCompatActivity) getActivity());

            TextView testTitle = view.findViewById(R.id.test_name_in_question_fragment);
            testTitle.setText(testName);

            TextView textView = view.findViewById(R.id.test_question_text);
            textView.setText(question.getText());

            btnNextQuestion = view.findViewById(R.id.test_next_btn);
            btnPreviousQuestion = view.findViewById(R.id.test_back_btn);

            btnNextQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = getAnswer();
                    if(answer != null && !answer.equals("")) {
                        ((TestActivity)context).saveAnswerToAnswersList(getAnswer(), question);
                    }
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
        }

//        if( isFinalQuestion ) {
////            renderFinalQuestion();
//        } else {
//            renderQuestion();
//
//        }

        return view;
    }

    private void renderQuestion() {

        if(question.getInstructions() != null) {
            this.renderInstructions();
        }

        if(question.getNoImmediateAnswer() != null && question.getNoImmediateAnswer() && question.getImage1() != null) {
            this.renderImageInstrucions();
        }

        if(question.getDateConfiguration() != null && question.getDateConfiguration()) {
            this.renderDateQuestion();
        }

        if(question.getImage1() != null
                && question.getDrawingConfiguration() == null
                && question.getDragAndDropConfiguration() == null
                && question.getNoImmediateAnswer() == null
                && question.getPoints().size() < 1
                ) {
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

        if(question.getDragAndDropConfiguration() != null && question.getDragAndDropConfiguration()) {
            renderDragAndDropConfiguration();
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

    private void renderImageInstrucions() {
        ImageView imageInstructions = new ImageView(context);
        imageInstructions.setImageResource(TestUtils.getImageResource(question.getImage1()));
        imageInstructions.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        questionLayout.addView(imageInstructions);
    }

    private String getAnswer() {

        if(question.getDateConfiguration() != null && question.getDateConfiguration()) {
             return getDateAnswer();
        }

        if(question.getImage1() != null && imageWordView!=null) {
            return getOneImageWordAnswer();
        }

        if(question.getInputConfiguration() != null && question.getInputConfiguration() && singleInputView != null) {
            return getSingleInputAnswer();
        }

        if(question.getDrawingConfiguration() != null && question.getDrawingConfiguration() && drawingView!=null ) {
            byte[] drawing = drawingView.getDrawing();
            String encodedDrawing = Base64.encodeToString(drawing, Base64.NO_WRAP);
            return encodedDrawing;
        }

        if(question.getMultipleTextConfiguration() != null && multipleInputsView != null) {
            return getMultipleAnswers();
        }

        if(question.getPoints().size() > 0 && connectPointsView != null) {
            return getConnectPointsExerciseAnswer();
        }

        if(question.getDragAndDropConfiguration() != null && question.getDragAndDropConfiguration() && dragAndDropView != null) {
            byte[] drawing = dragAndDropView.getDrawing();
            String bytes = new Gson().toJson(drawing);
            return bytes;
        }
        return null;
    }

    private String getConnectPointsExerciseAnswer() {
        return connectPointsView.getOrderOfConnectedPointsToString();
    }

    private String getMultipleAnswers() {
        return multipleInputsView.getMultipleInputsAnswer();
    }

    private String getSingleInputAnswer() {
        return singleInputView.getInputAnswer();
    }

    private String getOneImageWordAnswer() {

        return imageWordView.getWordAnswer();
    }

    private String getDateAnswer() {

        return dateView.getValuesInString();
    }

    private void renderDragAndDropConfiguration() {

        final DragAndDropView dragAndDropView = new DragAndDropView(context);

        addResetButton(dragAndDropView);

        questionLayout.addView(dragAndDropView);
    }

    private void renderConnectPointsExercise() {

        List<ConnectPoints> points = question.getPoints();

        connectPointsView = new ConnectPointsView(context);

        connectPointsView.setPoints(points);

        addResetButton(connectPointsView);

        questionLayout.addView(connectPointsView);
    }

    private void renderMultipleAnswers() {

        multipleInputsView = new MultipleInputsView(context,questionLayout);
    }

    private void renderDrawingConfiguration() {

        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeWidth(8);

        drawingView = new DrawingView(context,paint);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        drawingView.setLayoutParams(layoutParams);

        if(question.getImage1() != null && question.getImage1().equals("cube")){
            ImageView drawingModel = new ImageView(context);
            drawingModel.setImageResource(R.drawable.cube);
            drawingModel.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            questionLayout.addView(drawingModel);
        }

        addResetButton(drawingView);

        questionLayout.addView(drawingView);

    }

    private void renderInputConfiguration() {
        singleInputView = new SingleInputView(context,questionLayout);
    }

    private void renderOneImageExercise() {

        imageWordView = new ImageWordView(context,questionLayout,question);
    }

    private void renderDateQuestion() {

        dateView = new DateView(context, questionLayout);

    }

    private void renderInstructions() {

        TextView instructions = new TextView(context);
        instructions.setText(question.getInstructions());

        questionLayout.addView(instructions);

    }

    private void addResetButton(final CanvasBasedView view) {

        Button clearButton = new Button(context);
        clearButton.setText("Start Over");
        clearButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.startOver();
            }
        });

        questionLayout.addView(clearButton);

    }
}
