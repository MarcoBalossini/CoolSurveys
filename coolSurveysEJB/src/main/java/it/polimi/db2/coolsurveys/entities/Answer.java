package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "answer")
public class Answer implements Serializable {

    @EmbeddedId
    private AnswerPK id;

    @Column(name = "answer")
    private String answer;

    @MapsId("questionId")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "question_id", referencedColumnName = "question_id"),
            @JoinColumn(name = "questionnaire_id", referencedColumnName = "questionnaire_id"),
    })
    private Question question;

    /**
     * If a user is removed, also his answers have to be removed
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapsId("userId")
    private User user;



    public String getAnswer() {
        return answer;
    }

    public AnswerPK getId() {
        return id;
    }

    public void setId(AnswerPK id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
