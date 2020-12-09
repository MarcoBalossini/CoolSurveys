package it.polimi.db2.coolSurveys.entities;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "answer", schema = "questionnaire_db")
@NamedQuery(name = "Answer.findByQuestionnaire", 
			query = "SELECT a "
					+ "FROM Answer a "
					+ "WHERE a.question.questionnaire.qId = ?1")

public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "answer_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int answerId;
	
	private String answer;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@Column(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "question_id")
	@Column(name = "question_id")
	private Question question;

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
}
