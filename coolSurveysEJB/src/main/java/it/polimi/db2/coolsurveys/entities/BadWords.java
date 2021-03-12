package it.polimi.db2.coolsurveys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "bad_words")
public class BadWords implements Serializable {

    @Id
    @Column(name = "word", nullable = false)
    private String word;

}
