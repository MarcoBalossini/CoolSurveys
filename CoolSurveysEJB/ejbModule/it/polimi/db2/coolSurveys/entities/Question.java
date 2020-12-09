package it.polimi.db2.coolSurveys.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the question database table.
 * 
 */
@Entity
@Table(name = "question")
@NamedQuery(name = "Question.findAll", 
			query = "SELECT q FROM Question q")

public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "question_id")
	private int questionId;

	private String question;

	@ManyToOne
	@JoinColumn(name = "questionnaire_id")
	@Column(name = "questionnaire_id")
	private Questionnaire questionnaire;

	//bi-directional many-to-one association to Option
	@OneToMany(mappedBy = "question")
	private List<Option> options;

	public Question() {
	}

	public int getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Questionnaire getQuestionnaire() {
		return this.questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public List<Option> getOptions() {
		return this.options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public Option addOption(Option option) {
		getOptions().add(option);
		option.setQuestion(this);

		return option;
	}

	public Option removeOption(Option option) {
		getOptions().remove(option);
		option.setQuestion(null);

		return option;
	}

}