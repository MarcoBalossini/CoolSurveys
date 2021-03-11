package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(name = "Option.selectByText",
                query = "select o from Option o where o.text = :text")
})

@Entity
public class Option {

    //TODO: composite keys (with @Embedded?)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int option_id;

    @Id
    private int question_id;

    @Id
    private int questionnaire_id;

    @Column (nullable = false)
    private String text;

    @ManyToOne (cascade = CascadeType.ALL)
    private Question question;

    //TODO: is the relation with the questionnaire necessary?
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "questionnaire_id", orphanRemoval = true, cascade = CascadeType.ALL)
    private Questionnaire questionnaire;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getQuestionnaire_id() {
        return questionnaire_id;
    }

    public void setQuestionnaire_id(int questionnaire_id) {
        this.questionnaire_id = questionnaire_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }
}
