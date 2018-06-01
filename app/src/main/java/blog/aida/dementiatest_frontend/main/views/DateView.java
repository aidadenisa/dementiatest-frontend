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
        try {
            int years = Integer.parseInt(yearInput.getText().toString());
            int months = Integer.parseInt(monthInput.getText().toString());
            int days = Integer.parseInt(dateInput.getText().toString());

            Calendar c = Calendar.getInstance();
            c.set(years, months - 1, days, 0, 0);

            return c.getTimeInMillis()+"";

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"Do not leave the fields empty", Toast.LENGTH_SHORT).show();
        }

        return "";
    }
}
