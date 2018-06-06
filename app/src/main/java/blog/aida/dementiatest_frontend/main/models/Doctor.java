package blog.aida.dementiatest_frontend.main.models;

import java.io.Serializable;
import java.util.List;

import blog.aida.dementiatest_frontend.main.interfaces.Person;

/**
 * Created by aida on 29-Apr-18.
 */

public class Doctor implements Serializable, Person {

    private long id;

    private UserAccount userAccount;

    private List<Patient> patients;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public String getDisplayName() {
        return userAccount.getFirstName() + " " + userAccount.getLastName();
    }

}
