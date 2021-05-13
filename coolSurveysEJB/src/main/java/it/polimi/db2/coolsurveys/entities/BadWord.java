package it.polimi.db2.coolsurveys.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.ejb.TransactionAttribute;
import javax.persistence.*;


/**
 * The persistent class for the bad_words database table.
 * 
 */
@Entity
@Table(name="bad_words")
@NamedQuery(name="BadWord.findAllWords", query="SELECT b.word FROM BadWord b")
public class BadWord implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MONTHS_TO_BAN = 1;

	@Id
	private String word;

	public String getWord() {
		return this.word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BadWord badWord = (BadWord) o;
		return Objects.equals(word, badWord.word);
	}

	@Override
	public int hashCode() {
		return Objects.hash(word);
	}
}