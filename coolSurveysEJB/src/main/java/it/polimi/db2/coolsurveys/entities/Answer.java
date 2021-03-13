package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "answer")
public class Answer implements Serializable {
    
    @Id
    @Column(name = "answer_id", nullable = false)
    private Integer answer_id;

    @Id
    @Column(name = "question_id", nullable = false)
    private Integer question_id;

    @Id
    @Column(name = "questionnaire_id", nullable = false)
    private Integer questionnaire_id;

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer user_id;

    @Column(name = "answer")
    private String answer;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Question question;

    /**
     * If a user is removed, also his answers have to be removed
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
