package blog.aida.dementiatest_frontend.main.models;

import java.util.List;

/**
 * Created by aida on 29-Apr-18.
 */

public class Patient {

    private int id;

    private UserAccount userAccount;

    private List<Test> takenTests;

    private List<Doctor> doctors;

    public int getId() {
        return id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public List<Test> getTakenTests() {
        return takenTests;
    }

    public void setTakenTests(List<Test> takenTests) {
        this.takenTests = takenTests;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}
