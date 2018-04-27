package blog.aida.dementiatest_frontend.main.models;

/**
 * Created by aida on 24-Apr-18.
 */

public class Question {

    private int id;

    private String text;

    private String instructions;

    private String choices;

    private String image1;

    private String image2;

    private Boolean yesOrNoConfiguration;

    private Boolean onlyOccasionallyOption;

    private Boolean yesAnswerWithSpecification;

    private Boolean dateConfiguration;

    private Boolean noImmediateAnswer;

    private Boolean drawingConfiguration;

    private Boolean multipleTextConfiguration;

    private Boolean dragAndDropConfiguration;

//        private List<TestConfiguration> testConfigurations;


    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public Boolean getYesOrNoConfiguration() {
        return yesOrNoConfiguration;
    }

    public void setYesOrNoConfiguration(Boolean yesOrNoConfiguration) {
        this.yesOrNoConfiguration = yesOrNoConfiguration;
    }

    public Boolean getOnlyOccasionallyOption() {
        return onlyOccasionallyOption;
    }

    public void setOnlyOccasionallyOption(Boolean onlyOccasionallyOption) {
        this.onlyOccasionallyOption = onlyOccasionallyOption;
    }

    public Boolean getYesAnswerWithSpecification() {
        return yesAnswerWithSpecification;
    }

    public void setYesAnswerWithSpecification(Boolean yesAnswerWithSpecification) {
        this.yesAnswerWithSpecification = yesAnswerWithSpecification;
    }

    public Boolean getDateConfiguration() {
        return dateConfiguration;
    }

    public void setDateConfiguration(Boolean dateConfiguration) {
        this.dateConfiguration = dateConfiguration;
    }

    public Boolean getNoImmediateAnswer() {
        return noImmediateAnswer;
    }

    public void setNoImmediateAnswer(Boolean noImmediateAnswer) {
        this.noImmediateAnswer = noImmediateAnswer;
    }

    public Boolean getDrawingConfiguration() {
        return drawingConfiguration;
    }

    public void setDrawingConfiguration(Boolean drawingConfiguration) {
        this.drawingConfiguration = drawingConfiguration;
    }

    public Boolean getMultipleTextConfiguration() {
        return multipleTextConfiguration;
    }

    public void setMultipleTextConfiguration(Boolean multipleTextConfiguration) {
        this.multipleTextConfiguration = multipleTextConfiguration;
    }

    public Boolean getDragAndDropConfiguration() {
        return dragAndDropConfiguration;
    }

    public void setDragAndDropConfiguration(Boolean dragAndDropConfiguration) {
        this.dragAndDropConfiguration = dragAndDropConfiguration;
    }
}
