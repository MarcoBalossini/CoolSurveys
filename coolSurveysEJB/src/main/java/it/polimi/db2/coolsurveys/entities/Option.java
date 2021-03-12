package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name = "Option.selectByText",
                query = "select o from Option o where o.text = :text")
})

@Entity
@Table(name = "option")
public class Option implements Serializable {

    @Id
    @Column(name = "option_id", nullable = false)
    private Integer optionId;

    @Id
    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @Id
    @Column(name = "questionnaire_id", nullable = false)
    private Integer questionnaireId;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Question question;

    //TODO: is the relation with the questionnaire necessary?
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Questionnaire questionnaire;

    public Integer getQuestion_id() {
        return questionId;
    }

    public void setQuestion_id(int question_id) {
        this.questionId = question_id;
    }

    public Integer getQuestionnaire_id() {
        return questionnaireId;
    }

    public void setQuestionnaire_id(int questionnaire_id) {
        this.questionnaireId = questionnaire_id;
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
