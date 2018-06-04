package blog.aida.dementiatest_frontend.main.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.adapters.PersonListAdapter;
import blog.aida.dementiatest_frontend.main.models.Doctor;
import blog.aida.dementiatest_frontend.main.models.Patient;
import blog.aida.dementiatest_frontend.main.requests.VolleyCallback;
import blog.aida.dementiatest_frontend.main.services.DoctorService;
import blog.aida.dementiatest_frontend.main.services.GsonService;
import blog.aida.dementiatest_frontend.main.services.PatientService;
import blog.aida.dementiatest_frontend.main.services.ToolbarManager;

public class TestResultActivity extends AppCompatActivity {

    private static final int TEST_MAXIMUM = 22;
    private static final int NORMAL_STATE = 0;
    private static final int MILD_CONDITION_STATE = 1;
    private static final int STRONG_CONDITION_STATE = 2;

    private TextView scoreView;
    private TextView testDetailsView;
    private Button backToMyTestsButton;

    private RecyclerView doctorsListRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private DoctorService doctorService;
    private PatientService patientService;

    private int testScore;
    private int patientState;

    private String patientId;

    private List<Doctor> doctors;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);

        ToolbarManager.setupToolbar(this);

        scoreView = findViewById(R.id.test_score);
        testDetailsView = findViewById(R.id.test_result_details);
        backToMyTestsButton = findViewById(R.id.test_result_back_button);

        doctorService = new DoctorService();
        patientService = new PatientService();

        doctorsListRecyclerView = findViewById(R.id.doctor_list_recycler_view);
        doctorsListRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        doctorsListRecyclerView.setLayoutManager(layoutManager);

        testScore = Integer.parseInt(getIntent().getStringExtra("TEST_SCORE"));
        patientId = getIntent().getStringExtra("PATIENT_ID");

        backToMyTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToTestBoard = new Intent(TestResultActivity.this, TestsBoardActivity.class);
                goToTestBoard.putExtra("PATIENT_ID", patientId);

                TestResultActivity.this.startActivity(goToTestBoard);
            }
        });

        queue = Volley.newRequestQueue(this);

        doctorService.getAllDoctors(queue, this, new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {

                doctors = (List<Doctor>) result;
                List<String> doctorNames = new ArrayList<>();
                for(int i=0; i<doctors.size(); i++) {
                    doctorNames.add(
                            doctors.get(i).getUserAccount().getFirstName() + " "
                            + doctors.get(i).getUserAccount().getLastName()
                    );
                }
                adapter = new PersonListAdapter(doctorNames, false, TestResultActivity.this);
                doctorsListRecyclerView.setAdapter(adapter);
            }
        });


//
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

    public void savePatientOfDoctor(int index) {

        if(doctors != null && doctors.size() > 0) {
            Doctor doctor = doctors.get(index);
            patientService.savePatientOfDoctor(queue, patientId, doctor, this , new VolleyCallback() {
                @Override
                public void onSuccess(Object result) {
                    Patient patient = (Patient) result;
                    if ( patient != null ) {
                        Toast.makeText(TestResultActivity.this, "The results have been sent to doctor.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
}
