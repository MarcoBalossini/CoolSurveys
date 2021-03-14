package it.polimi.db2.coolsurveys.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the question database table.
 * 
 */
@Embeddable
@Table(name="question")
public class QuestionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name="question_id")
	private int questionId;

	@Column(name="questionnaire_id", insertable=false, updatable=false)
	private int questionnaireId;

	public int getQuestionId() {
		return this.questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getQuestionnaireId() {
		return this.questionnaireId;
	}
	public void setQuestionnaireId(int questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof QuestionPK)) {
			return false;
		}
		QuestionPK castOther = (QuestionPK)other;
		return 
			(this.questionId == castOther.questionId)
			&& (this.questionnaireId == castOther.questionnaireId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.questionId;
		hash = hash * prime + this.questionnaireId;
		
		return hash;
	}
}