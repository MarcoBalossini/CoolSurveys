package it.polimi.db2.coolSurveys.entities;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "option")
@NamedQuery(name = "Option.findAll", 
			query = "SELECT o FROM Option o")

public class Option implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "option_id")
	private int optionId;

	private String text;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;

	public Option() {
	}

	public int getOptionId() {
		return this.optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}