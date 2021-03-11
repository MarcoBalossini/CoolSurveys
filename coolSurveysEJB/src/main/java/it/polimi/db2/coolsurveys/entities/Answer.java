package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;

@Entity
public class Answer {

    //TODO: composite keys (with @Embedded?)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int answer_id;

    @Column (nullable = false)
    private int question_id;

    @Column (nullable = false)
    private int questionnaire_id;

    @Column (nullable = false)
    private int user_id;

    @Column
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
