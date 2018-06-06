package blog.aida.dementiatest_frontend.main.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.adapters.PatientTestSummaryAdapter;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.Question;

public class PatientTestSummaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Answer> answers;

    private TextView patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_test_summary);

        patientName = findViewById(R.id.patient_name);

        recyclerView = findViewById(R.id.patient_test_summary_recycler_view);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        answers = (List<Answer>) getIntent().getSerializableExtra("ANSWERS_LIST");

        adapter = new PatientTestSummaryAdapter(answers);
        recyclerView.setAdapter(adapter);

        patientName.setText(answers.get(0).getPatient().getDisplayName());
    }
}
