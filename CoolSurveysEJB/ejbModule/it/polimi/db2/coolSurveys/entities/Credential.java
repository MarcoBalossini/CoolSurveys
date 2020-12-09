package it.polimi.db2.coolSurveys.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "credentials", schema = "questionnaire_db")
@NamedQuery(name = "Credential.getCredentials", 
			query = "SELECT c "
					+ "FROM Credential c "
					+ "WHERE c.user.username = ?1")
public class Credential implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@OneToOne
	@Column(name = "user_id")
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "password_hash")
	private String passwordHash;

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String password_hash) {
		this.passwordHash = password_hash;
	}
	
}
