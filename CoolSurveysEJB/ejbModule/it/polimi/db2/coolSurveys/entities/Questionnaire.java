package it.polimi.db2.coolSurveys.entities;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "questionnaire", schema = "questionnaire_db")
@NamedQuery(name = "Questionnaire.findQuestionnaire", 
			query = "SELECT q "
					+ "FROM Questionnaire q "
					+ "WHERE q.date = ?1")
public class Questionnaire implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "q_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int qId;
	
	private Date date;
	
	private String name;
	
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private byte[] photo;

	public int getQId() {
		return qId;
	}

	public void setQId(int q_id) {
		this.qId = q_id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
}
