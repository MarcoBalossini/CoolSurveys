package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;


@NamedQueries({
        @NamedQuery(name = "Answer.selectByUserAndQuestion",
                query = "select a from Answer a where a.user = :user and a.question = :question")
})

@Entity
@Table(name = "answer")
public class Answer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private int answerId;

    @Column(name = "answer")
    private String answer;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * If a user is removed, also his answers have to be removed
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Answer() {}

    public Answer(Question question, User user, String text) {
        this.answer = text;
        this.question = question;
        this.user = user;
    }

    public String getAnswer() {
        return answer;
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
