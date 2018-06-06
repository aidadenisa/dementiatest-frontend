package blog.aida.dementiatest_frontend.main.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import blog.aida.dementiatest_frontend.R;
import blog.aida.dementiatest_frontend.main.adapters.PersonListAdapter;
import blog.aida.dementiatest_frontend.main.models.Answer;
import blog.aida.dementiatest_frontend.main.models.Doctor;
import blog.aida.dementiatest_frontend.main.models.Test;
import blog.aida.dementiatest_frontend.main.models.UserAccount;
import blog.aida.dementiatest_frontend.main.requests.VolleyCallback;
import blog.aida.dementiatest_frontend.main.services.PatientService;
import blog.aida.dementiatest_frontend.main.services.ToolbarManager;

public class PatientsListActivity extends AppCompatActivity {

    private Integer userId;

    private Doctor doctor;

    private RequestQueue queue;

    private RecyclerView patientListRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private PatientService patientService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_list);

        ToolbarManager.setupToolbar(this);

        patientService = new PatientService();

        queue = Volley.newRequestQueue(this);

        patientListRecyclerView = findViewById(R.id.patient_list_recycler_view);
        patientListRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        patientListRecyclerView.setLayoutManager(layoutManager);

        doctor = (Doctor) getIntent().getSerializableExtra("DOCTOR_INFO");

        if(doctor != null) {

            if(doctor.getPatients().size()>0) {
                List<String> names = new ArrayList<>();
                for(int i=0; i<doctor.getPatients().size(); i++ ) {
                    UserAccount patientAccount = doctor.getPatients().get(i).getUserAccount();
                    names.add(patientAccount.getFirstName() + " " + patientAccount.getLastName());
                }
                adapter = new PersonListAdapter(names, true, this);
                patientListRecyclerView.setAdapter(adapter);
            }

        }
    }

    public void openTestSummary(int position) {

        Test lastTakenTest;

        if(doctor.getPatients() != null && doctor.getPatients().size() > 0) {
            if(doctor.getPatients().get(position) != null) {
                List<Test> tests = doctor.getPatients().get(position).getTakenTests();
                if(tests != null && tests.size() > 0 ) {
                    lastTakenTest = tests.get(0);

                    patientService.getAnswersOfTakenTest(
                        queue,
                        doctor.getPatients().get(position).getId(),
                        lastTakenTest.getId(),
                        this,
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(Object result) {

                                List<Answer> answers = (List<Answer>) result;

                                Intent goToTestSummary = new Intent(PatientsListActivity.this, PatientTestSummaryActivity.class);
                                goToTestSummary.putExtra("ANSWERS_LIST", (Serializable) answers);
                                PatientsListActivity.this.startActivity(goToTestSummary);

                            }
                        }
                    );
                }
            }
        }
    }
}
