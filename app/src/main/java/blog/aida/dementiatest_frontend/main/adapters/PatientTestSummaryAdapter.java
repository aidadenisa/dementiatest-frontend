package blog.aida.dementiatest_frontend.main.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.ConnectPoints;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.services.GsonService;
import blog.aida.dementiatest_frontend.main.services.ImageManager;
import blog.aida.dementiatest_frontend.main.views.DrawingView;

/**
 * Created by aida on 05-Jun-18.
 */

public class PatientTestSummaryAdapter extends RecyclerView.Adapter<PatientTestSummaryAdapter.ViewHolder> {

    private List<Answer> answers;

    private Context context;

    public PatientTestSummaryAdapter(List<Answer> answers) {
        this.answers = answers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;
        public TextView questionText;
        public LinearLayout summaryLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            questionText = itemView.findViewById(R.id.summary_question_text);
            summaryLayout = itemView.findViewById(R.id.summary_linear_layout);
        }

    }

    @Override
    public PatientTestSummaryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_patient_test_summary, parent, false);

        PatientTestSummaryAdapter.ViewHolder viewHolder = new PatientTestSummaryAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PatientTestSummaryAdapter.ViewHolder holder, int position) {

        holder.summaryLayout.removeAllViews();

        Answer currentAnswer = answers.get(position);

        holder.questionText.setText(currentAnswer.getQuestion().getText());

        this.renderAnswer(holder, currentAnswer);

        this.renderScore(holder,currentAnswer);

    }

    private void renderScore(ViewHolder holder, Answer answer) {
        TextView scoreView = new TextView(context);
        scoreView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        scoreView.setBackgroundColor(context.getResources().getColor(R.color.buttonBgColor));
        String scoreText = "Score " + answer.getScore();
        scoreView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        scoreView.setText(scoreText);
        scoreView.setPadding(10,10,10,10);

        holder.summaryLayout.addView(scoreView);
    }

    private void renderAnswer(ViewHolder holder, Answer currentAnswer) {

        Question question = currentAnswer.getQuestion();

        if(question.getInstructions() != null) {
            this.renderInstructions(holder, currentAnswer);
        }

        if(question.getDateConfiguration() != null && question.getDateConfiguration()) {
            this.renderDateQuestion(holder, currentAnswer);
        }

        if(question.getImage1() != null
                && question.getDrawingConfiguration() == null
                && question.getDragAndDropConfiguration() == null
                && question.getNoImmediateAnswer() == null
                && question.getPoints().size() < 1
                ) {
            renderOneImageExercise(holder, currentAnswer);
        }

        if(question.getInputConfiguration() != null && question.getInputConfiguration()) {
            renderInputConfiguration(holder, currentAnswer);
        }

        if(question.getDrawingConfiguration() != null && question.getDrawingConfiguration()) {
            renderDrawingConfiguration(holder,currentAnswer);
        }

        if(question.getMultipleTextConfiguration() != null) {
            renderMultipleAnswers(holder, currentAnswer);
        }

        if(question.getPoints().size() > 0) {
            renderConnectPointsExercise(holder,currentAnswer);
        }

        if(question.getDragAndDropConfiguration() != null && question.getDragAndDropConfiguration()) {
            renderDrawingConfiguration(holder,currentAnswer);
        }

    }

    private void renderConnectPointsExercise(ViewHolder holder, Answer answer) {
        Type listType = new TypeToken<ArrayList<ConnectPoints>>(){}.getType();
        GsonService gson = new GsonService();
        List<ConnectPoints> points = gson.getBuilder().fromJson(answer.getAnswer(), listType);
        String renderedAnswer = "";

        if(points != null && points.size() > 0) {
            renderedAnswer = points.get(0).getCode();
            for(int i = 1; i < points.size(); i++ ) {
                renderedAnswer = renderedAnswer + " " + points.get(i).getCode();
            }
        }

        TextView answerTextView = new TextView(context);
        answerTextView.setText(renderedAnswer);

        holder.summaryLayout.addView(answerTextView);

    }

    private void renderMultipleAnswers(ViewHolder holder, Answer answer) {
        String[] answers = answer.getAnswer().split("\\|");
        for(int i=0; i< answers.length; i++ ) {
            TextView textView = new TextView(context);
            textView.setText(answers[i].trim());
            holder.summaryLayout.addView(textView);
        }
    }

    private void renderDrawingConfiguration(ViewHolder holder, Answer answer) {

        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeWidth(8);

        byte[] answerDrawing = Base64.decode(answer.getAnswer(), Base64.DEFAULT);
        Bitmap bMap = BitmapFactory.decodeByteArray(answerDrawing, 0, answerDrawing.length);
        Bitmap mutableBitmap = bMap.copy(Bitmap.Config.ARGB_8888, true);


//        DrawingView drawingView = new DrawingView(context, paint,true);
//        drawingView.drawBitmap(mutableBitmap);

        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(mutableBitmap);

        holder.summaryLayout.addView(imageView);
    }

    private void renderInputConfiguration(ViewHolder holder, Answer answer) {
        TextView patientInput = new TextView(context);
        patientInput.setText(answer.getAnswer());

        holder.summaryLayout.addView(patientInput);
    }

    private void renderOneImageExercise(ViewHolder holder, Answer answer) {

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(ImageManager.getImageResourceFromName(answer.getQuestion().getImage1()));

        TextView imageAnswer = new TextView(context);
        imageAnswer.setText(answer.getAnswer());

        holder.summaryLayout.addView(imageView);
        holder.summaryLayout.addView(imageAnswer);
    }

    private void renderDateQuestion(ViewHolder holder, Answer answer) {
        TextView dateScore =  new TextView(context);
        if(answer.getScore() == 4 ) {
            dateScore.setText("Exact date submitted");
        } else {
            if(answer.getScore() < 4 && answer.getScore() > 0) {
                dateScore.setText("Partially correct date submitted");
            } else {
                dateScore.setText("Incorrect date submitted");
            }
        }
        holder.summaryLayout.addView(dateScore);
    }

    private void renderInstructions(ViewHolder holder, Answer answer) {

        TextView instructions = new TextView(context);
        instructions.setText(answer.getQuestion().getInstructions());

        holder.summaryLayout.addView(instructions);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }
}
