package blog.aida.dementiatest_frontend.main.adapters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.fragments.DatePickerFragment;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.Question;
import blog.aida.dementiatest_frontend.main.services.FontManager;

/**
 * Created by aida on 24-Apr-18.
 */

public class PersonalInformationTestAdapter extends RecyclerView.Adapter<PersonalInformationTestAdapter.ViewHolder> {

    private List<Question> questions;
    private Map<Integer, String> answers;
    private Context parentContext;
    private ViewGroup parent;
    private Activity activity;
//    private Typeface fontAwesome;

    public PersonalInformationTestAdapter(List<Question> questions, Activity activity) {
        this.questions = questions;
        this.activity = activity;
        this.answers = new HashMap<>();
//        this.fontAwesome = FontManager.getTypeface(parentContext,FontManager.FONTAWESOME);
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

        if(currentQuestion.getYesOrNoConfiguration() != null && currentQuestion.getYesOrNoConfiguration()) {
            this.renderYesOrNoConfiguration(currentQuestion, holder);
        }

        if(currentQuestion.getDateConfiguration() != null && currentQuestion.getDateConfiguration()) {
            this.renderDatePicker(currentQuestion, holder);
        }

        if(currentQuestion.getInputConfiguration() != null && currentQuestion.getInputConfiguration()) {
            this.renderInputText(currentQuestion, holder);
        }

    }

    private void renderInputText(Question currentQuestion, ViewHolder holder) {

        int position = holder.getAdapterPosition();

        EditText inputText = new EditText(parentContext);
        attachInputTextBlurListener(inputText, holder);

        if(answers.get(position) != null) {
            inputText.setText(answers.get(position));
        }

        holder.questionLayout.addView(inputText);

    }

    private void attachInputTextBlurListener(final EditText inputText, final ViewHolder holder) {
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                int position = holder.getAdapterPosition();

                if (!hasFocus) {
                    answers.put(position, inputText.getText().toString());
                }
            }
        });
    }

    private void renderDatePicker(Question currentQuestion, ViewHolder holder) {

        final int position = holder.getAdapterPosition();
        String previousAnswer = answers.get(position);




        LinearLayout dateLayout = new LinearLayout(parentContext);
        dateLayout.setOrientation(LinearLayout.HORIZONTAL);
        dateLayout.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        dateLayout.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView calendarPickerIcon = new TextView(parentContext);
        calendarPickerIcon.setTypeface(FontManager.getTypeface(parentContext,FontManager.FONTAWESOME));
        calendarPickerIcon.setText(R.string.fa_calendar);
        calendarPickerIcon.setTextSize(18);
        LinearLayout.LayoutParams pickerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        calendarPickerIcon.setLayoutParams(pickerParams);

        final TextView dateText = new TextView(parentContext);
        dateText.setLayoutParams(pickerParams);

        if(previousAnswer != null) {
            Date date = new Date(previousAnswer);
            dateText.setText(date.getDate() + "/"
                    + (date.getMonth() + 1) + "/" + date.getYear());
        }

        dateLayout.addView(dateText);
        dateLayout.addView(calendarPickerIcon);
        holder.questionLayout.addView(dateLayout);

        calendarPickerIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(parentContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateText.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                                Date chosenDate = new Date(year, monthOfYear, dayOfMonth);

                                answers.put(position,chosenDate.toString());


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
    }

    private void renderYesOrNoConfiguration(Question currentQuestion, ViewHolder holder) {

        int position = holder.getAdapterPosition();
        String previousSelectedChoice = null;
        if(answers.get(position) != null) {
            previousSelectedChoice = answers.get(position);
        }

        RadioGroup  radioGroup = new RadioGroup(parentContext);

        RadioButton yesButton = new RadioButton(parentContext);
        yesButton.setText("YES");
        attachRadioButtonStateListener(yesButton,holder);
        radioGroup.addView(yesButton);

        RadioButton noButton = new RadioButton(parentContext);
        noButton.setText("NO");
        attachRadioButtonStateListener(noButton,holder);
        radioGroup.addView(noButton);

        RadioButton occasionallyButton = null;
        if(currentQuestion.getOnlyOccasionallyOption() != null && currentQuestion.getOnlyOccasionallyOption()) {
            occasionallyButton = new RadioButton(parentContext);
            occasionallyButton.setText("Only Occasionally");
            attachRadioButtonStateListener(occasionallyButton,holder);
            radioGroup.addView(occasionallyButton);
        }

        if(previousSelectedChoice != null) {
            switch(previousSelectedChoice) {
                case "YES" :
                    yesButton.setChecked(true);
                    break;

                case "NO" :
                    noButton.setChecked(true);
                    break;

                case "Only Occasionally" :
                    if(occasionallyButton != null) {
                        occasionallyButton.setChecked(true);
                    }
                    break;
                default:
                    break;
            }
        }

        holder.questionLayout.addView(radioGroup);

    }

    private void attachRadioButtonStateListener(final RadioButton radioButton, final ViewHolder holder) {

        radioButton.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int position = holder.getAdapterPosition();

                if (isChecked) {
                    answers.put(position, radioButton.getText().toString());
                } else {
                    answers.remove(position);
                }
            }
        });
    }

    private void renderChoices(Question currentQuestion, ViewHolder holder) {

        int position = holder.getAdapterPosition();
        String[] previousSelectedChoices = null;
        if(answers.get(position) != null) {
            previousSelectedChoices = answers.get(position).split("\\|");
            for(int i = 0; i<previousSelectedChoices.length; i++ ) {
                previousSelectedChoices[i] = previousSelectedChoices[i].trim();
            }
        }

        String[] choices = currentQuestion.getChoices().split("\\|");
        for (int i=0; i < choices.length ; i++ ) {
            choices[i] = choices[i].trim();
            CheckBox checkBox = new CheckBox(parentContext);
            checkBox.setText(choices[i]);
            if(previousSelectedChoices != null && Arrays.asList(previousSelectedChoices).contains(choices[i])) {
                checkBox.setChecked(true);
            }
            attachCheckboxStateListener(checkBox,holder);
            holder.questionLayout.addView(checkBox);
        }


    }

    private void attachCheckboxStateListener(final CheckBox checkBox, final ViewHolder holder) {

        checkBox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int position = holder.getAdapterPosition();

                if (isChecked) {
                    if(answers.get(position) == null) {
                        answers.put(position,checkBox.getText().toString() + " | ");
                    } else {
                        answers.put(position, answers.get(position) + checkBox.getText().toString() + " | " );
                    }
                } else {
                    answers.put(position,answers.get(position).replace(checkBox.getText().toString() + " | ", ""));
                }

            }
        });

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
