package blog.aida.dementiatest_frontend.main.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.models.Question;

/**
 * Created by aida on 24-Apr-18.
 */

public class PersonalInformationTestAdapter extends RecyclerView.Adapter<PersonalInformationTestAdapter.ViewHolder> {

    private List<Question> questions;
    private Context parentContext;
    private ViewGroup parent;

    public PersonalInformationTestAdapter(List<Question> questions) {
        this.questions = questions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;
        public TextView questionText;
        public LinearLayout questionLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            questionText = (TextView) itemView.findViewById(R.id.question_text);
            questionLayout = (LinearLayout) itemView.findViewById(R.id.question_linear_layout);
        }


    }

    @Override
    public PersonalInformationTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        parentContext = parent.getContext();
        this.parent = parent;

        LayoutInflater inflater = LayoutInflater.from(parentContext);
        View view = inflater.inflate(R.layout.list_item_personal_info_question, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PersonalInformationTestAdapter.ViewHolder holder, int position) {

        holder.questionLayout.removeAllViews();

        Question currentQuestion = questions.get(position);

        holder.questionText.setText(currentQuestion.getText());

        this.renderQuestion(holder, currentQuestion);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    private void renderQuestion(PersonalInformationTestAdapter.ViewHolder holder, Question currentQuestion) {

        if(currentQuestion.getInstructions() != null) {
            this.renderInstructions(currentQuestion, holder);
        }

        if(currentQuestion.getChoices() != null) {
            this.renderChoices(currentQuestion, holder);
        }

    }

    private void renderChoices(Question currentQuestion, ViewHolder holder) {
        String[] choices = currentQuestion.getChoices().split("\\|");
        for (int i=0; i < choices.length ; i++ ) {
            choices[i] = choices[i].trim();
            CheckBox checkBox = new CheckBox(parentContext);
            checkBox.setText(choices[i]);
            holder.questionLayout.addView(checkBox);
        }
    }

    private void renderInstructions(Question currentQuestion, PersonalInformationTestAdapter.ViewHolder holder) {

        TextView instructions = new TextView(parentContext);
        instructions.setText(currentQuestion.getInstructions() + " -id>" + currentQuestion.getId());

        holder.questionLayout.addView(instructions);

    }

    public void add(int position, Question question) {
        questions.add(position, question);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        questions.remove(position);
        notifyItemRemoved(position);
    }


}
