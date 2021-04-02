package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Question.selectByQuestion",
                query = "select q from Question q where q.question = :question"),
        @NamedQuery(name="Question.findAll", query="SELECT q FROM Question q")
})

@Entity
@Table(name = "question")
public class Question implements Serializable {

    @EmbeddedId
    private QuestionPK id;

    @Column(name = "question", nullable = false)
    private String question;

    @MapsId("questionnaireId")
    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Option> options = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();


    public Question(String question, Questionnaire questionnaire) {
        this.question = question;
        this.questionnaire = questionnaire;

    }

    public Question() {}

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

    public String getQuestion() {
        return question;
    }
}
