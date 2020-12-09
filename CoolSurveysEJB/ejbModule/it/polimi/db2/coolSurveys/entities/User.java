package it.polimi.db2.coolSurveys.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "user", schema = "questionnaire_db")
@NamedQuery(name = "User.getLeaderboard",
			query = "SELECT u "
					+ "FROM User u "
					+ "ORDER BY u.points DESC")

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int userId;
	
	private String username;
	
	private int age;
	
	@Column(name = "expertise_level")
	private int expertiseLevel;
	
	//tinyint, boolean if possible
	private int gender;
	
	private String mail;
	
	private int points;
	
	//tinyint, boolean if possible
	private int producer;

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getExpertiseLevel() {
		return expertiseLevel;
	}

	public void setExpertiseLevel(int expertise_level) {
		this.expertiseLevel = expertise_level;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getProducer() {
		return producer;
	}

	public void setProducer(int producer) {
		this.producer = producer;
	}

}
