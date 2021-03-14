package it.polimi.db2.coolsurveys.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the bad_words database table.
 * 
 */
@Entity
@Table(name="bad_words")
@NamedQuery(name="BadWord.findAll", query="SELECT b FROM BadWord b")
public class BadWord implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String word;

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}