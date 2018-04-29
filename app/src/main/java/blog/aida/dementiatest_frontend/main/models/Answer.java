package blog.aida.dementiatest_frontend.main.models;

/**
 * Created by aida on 29-Apr-18.
 */

public class Answer {


    private int id;

    private Patient patient;

    private Test test;

    private Question question;

    private String answer;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
