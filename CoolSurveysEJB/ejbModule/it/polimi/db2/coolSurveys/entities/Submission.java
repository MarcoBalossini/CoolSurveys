package it.polimi.db2.coolSurveys.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "submission", schema = "questionnaire_db")
@NamedQuery(name = "Submission.getSubmissionsByUserId", 
			query = "SELECT s "
					+ "FROM Submission s "
					+ "WHERE s.submissionKey.user.userId = ?1")

public class Submission implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "submission_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int submissionId;
	
	@OneToOne
	@JoinColumn(name = "q_id")
	@Column(name = "q_id")
	private Questionnaire questionnaire;

	@Column(name = "user_id")
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	private int submitted;

	public int getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(int submissionId) {
		this.submissionId = submissionId;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getSubmitted() {
		return submitted;
	}

	public void setSubmitted(int submitted) {
		this.submitted = submitted;
	}
   
}
