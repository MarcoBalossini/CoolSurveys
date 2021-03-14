package it.polimi.db2.coolsurveys.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OptionPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "option_id", nullable = false)
    private Integer optionId;

    private QuestionPK questionId;


    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public QuestionPK getQuestionId() {
        return questionId;
    }

    public void setQuestionId(QuestionPK questionId) {
        this.questionId = questionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionPK optionPK = (OptionPK) o;
        return Objects.equals(optionId, optionPK.optionId) && Objects.equals(questionId, optionPK.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optionId, questionId);
    }
}
