package blog.aida.dementiatest_frontend.main.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aida on 13-Apr-18.
 */

public class UserAccount implements Serializable{

    private long id;

    private String hash;

    private String firstName;

    private String lastName;

    private Date birthday;

    private String email;

    private String address;

    // 0 - patient, 1 - doctor
    private int role;

    public long getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
