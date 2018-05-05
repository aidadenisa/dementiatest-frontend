package blog.aida.dementiatest_frontend.main.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aida on 29-Apr-18.
 */

public class TestConfiguration implements Serializable {

    private int id;

    private String name;

    private List<Question> questions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
