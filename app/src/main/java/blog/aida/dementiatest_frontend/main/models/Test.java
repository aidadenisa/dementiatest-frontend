package blog.aida.dementiatest_frontend.main.models;

/**
 * Created by aida on 29-Apr-18.
 */

public class Test {

    private int id;

    private TestConfiguration testConfiguration;

    private Patient patient;

    public int getId() {
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
}
