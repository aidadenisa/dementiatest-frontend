package blog.aida.dementiatest_frontend.main.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.models.Question;

/**
 * Created by aida on 24-Apr-18.
 */

public class PersonalInformationTestAdapter extends RecyclerView.Adapter<PersonalInformationTestAdapter.ViewHolder> {

    private List<Question> questions;

    public PersonalInformationTestAdapter(List<Question> questions) {
        this.questions = questions;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View layout;
        public TextView questionText;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            questionText = (TextView) itemView.findViewById(R.id.question_text);
        }


    }

    @Override
    public PersonalInformationTestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_personal_info_question, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PersonalInformationTestAdapter.ViewHolder holder, int position) {
        Question currentQuestion = questions.get(position);

        holder.questionText.setText(currentQuestion.getText());
    }

    @Override
    public int getItemCount() {
        return questions.size();
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
