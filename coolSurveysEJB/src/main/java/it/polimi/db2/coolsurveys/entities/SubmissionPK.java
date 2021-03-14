package it.polimi.db2.coolsurveys.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the submission database table.
 * 
 */
@Embeddable
public class SubmissionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_id", insertable=false, updatable=false)
	private int userId;

	@Column(name="questionnaire_id", insertable=false, updatable=false)
	private int questionnaireId;

	public SubmissionPK() {
	}
	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
		if (!(other instanceof SubmissionPK)) {
			return false;
		}
		SubmissionPK castOther = (SubmissionPK)other;
		return 
			(this.userId == castOther.userId)
			&& (this.questionnaireId == castOther.questionnaireId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.questionnaireId;
		
		return hash;
	}
}