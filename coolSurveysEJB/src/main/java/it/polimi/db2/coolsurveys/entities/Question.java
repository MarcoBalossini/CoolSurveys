package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Question.selectByQuestion",
                query = "select q from Question q where q.question = :question")
})

@Entity
public class Question {

    //TODO: composite keys (with @Embedded?)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int question_id;

    @Id
    private int questionnaire_id;

    @Column (nullable = false)
    private String question;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Questionnaire questionnaire;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question_id")
    private List<Option> options;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question_id")
    private List<Answer> answers;

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setQuestionnaire_id(int questionnaire_id) {
        this.questionnaire_id = questionnaire_id;
    }

    public int getQuestion_id() {
        return question_id;
    }
}
