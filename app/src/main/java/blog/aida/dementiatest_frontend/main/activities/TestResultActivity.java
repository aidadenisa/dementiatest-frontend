package blog.aida.dementiatest_frontend.main.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.services.ToolbarManager;

public class TestResultActivity extends AppCompatActivity {

    private static final int TEST_MAXIMUM = 22;
    private static final int NORMAL_STATE = 0;
    private static final int MILD_CONDITION_STATE = 1;
    private static final int STRONG_CONDITION_STATE = 2;

    private TextView scoreView;
    private TextView testDetailsView;
    private Button backToMyTestsButton;

    private int testScore;
    private int patientState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        ToolbarManager.setupToolbar(this);

        scoreView = findViewById(R.id.test_score);
        testDetailsView = findViewById(R.id.test_result_details);
        backToMyTestsButton = findViewById(R.id.test_result_back_button);

        testScore = Integer.parseInt(getIntent().getStringExtra("TEST_SCORE"));

        backToMyTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToTestBoard = new Intent(TestResultActivity.this, TestsBoardActivity.class);
                TestResultActivity.this.startActivity(goToTestBoard);
            }
        });

        renderTestInformation();

    }

    private void renderTestInformation() {

        int colorOfPercentage;
        String details;

        if(testScore >= 17 && testScore <= 22) {
            patientState = NORMAL_STATE;
        }

        if(testScore >= 15 && testScore < 17) {
            patientState = MILD_CONDITION_STATE;
        }

        if(testScore <= 14) {
            patientState = STRONG_CONDITION_STATE;
        }

        switch (patientState) {
            case NORMAL_STATE:
                colorOfPercentage = Color.GREEN;
                details = "Your cognitive functions seem to be in the normal range. " +
                        "However, this is not a diagnosis tool and we strongly recommend you to seek the opinion of a doctor.";
                break;
            case MILD_CONDITION_STATE:
                colorOfPercentage = Color.YELLOW;
                details = "You are likely to have mild memory or thinking impairments. Further evaluation by a physician is recommended.";
                break;
            case STRONG_CONDITION_STATE:
                colorOfPercentage = Color.RED;
                details = "You are likely to have a more severe memory or thinking condition. Further evaluation by a physician is strongly recommended.";
                break;
            default:
                colorOfPercentage = Color.GRAY;
                details = "Further evaluation by a physician is recommended.";
                break;
        }

        String testPercentage = (testScore * 100 / TEST_MAXIMUM) + "%";

        scoreView.setText(testPercentage);
        scoreView.setTextColor(colorOfPercentage);

        testDetailsView.setText(details);

    }
}
