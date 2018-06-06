package blog.aida.dementiatest_frontend.main.models;

import java.io.Serializable;

/**
 * Created by aida on 29-Apr-18.
 */

public class Test implements Serializable{

    private long id;

    private TestConfiguration testConfiguration;

    private int score;

    private Patient patient;

    public long getId() {
        return id;
    }

    public TestConfiguration getTestConfiguration() {
        return testConfiguration;
    }

    public void setTestConfiguration(TestConfiguration testConfiguration) {
        this.testConfiguration = testConfiguration;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
