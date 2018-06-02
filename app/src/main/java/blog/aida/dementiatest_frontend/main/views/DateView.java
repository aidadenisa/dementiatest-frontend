package blog.aida.dementiatest_frontend.main.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by aida on 23-May-18.
 */

public class DateView {

    private Context context;
    private EditText dateInput;
    private EditText monthInput;
    private EditText yearInput;

    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout.LayoutParams fieldParams;

    private ViewGroup questionLayout;
//    private TextView dateLabel;

    public DateView(Context context, ViewGroup questionLayout) {

        this.context = context;
        this.questionLayout = questionLayout;

        layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        fieldParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f);

        yearInput = new EditText(context);
        monthInput = new EditText(context);
        dateInput = new EditText(context);

        renderDateLayout("Year", yearInput);
        renderDateLayout("Month", monthInput);
        renderDateLayout("Day", dateInput);
    }

    private void renderDateLayout(String dateLabelText, EditText input) {

        LinearLayout dateLayout = new LinearLayout(context);
        dateLayout.setOrientation(LinearLayout.HORIZONTAL);
        dateLayout.setLayoutParams(layoutParams);

        input.setLayoutParams(fieldParams);

        TextView dateLabel = new TextView(context);
        dateLabel.setText(dateLabelText);
        dateLabel.setLayoutParams(fieldParams);

        dateLayout.addView(dateLabel);
        dateLayout.addView(input);

        questionLayout.addView(dateLayout);

    }

    public String getValuesInString() {
        int years;
        int months;
        int days;

        try {
            years = Integer.parseInt(yearInput.getText().toString());
        } catch (Exception e) {
            years = 2000;
        }

        try {
            months = Integer.parseInt(monthInput.getText().toString());
        } catch (Exception e) {
            months = Math.abs(new Date().getMonth() - 1);
        }

        try {
            days = Integer.parseInt(dateInput.getText().toString());
        } catch (Exception e) {
            days = Math.abs(new Date().getDay() - 15);
        }

        return years+"-"+months+"-"+days;
    }
}
