package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AnswerPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "answer_id", nullable = false)
    private Integer answerId;

    private QuestionPK questionId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerPK answerPK = (AnswerPK) o;
        return Objects.equals(answerId, answerPK.answerId) && Objects.equals(questionId, answerPK.questionId) &&  Objects.equals(userId, answerPK.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerId, questionId, userId);
    }
}
