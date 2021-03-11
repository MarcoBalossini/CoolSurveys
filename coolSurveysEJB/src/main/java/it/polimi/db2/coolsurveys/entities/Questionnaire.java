package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Questionnaire.selectByName",
                query = "select q from Questionnaire q where q.name = :name")
})

@Entity
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int q_id;

    @Column (nullable = false, unique = true)
    private String name;

    //TODO: see how images in byte[] work
    @Column (nullable = false)
    private byte[] photo;

    @Column (nullable = false)
    private final LocalDateTime date = LocalDateTime.now();

    @OneToMany(fetch = FetchType.EAGER)
    private List<Question> questions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questionnaire")
    private List<Submission> submissions;

    //TODO: necessary??
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "questionnaire")
    private List<Option> options;

    public LocalDateTime getDate() {
        return date;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getQ_id() {
        return q_id;
    }
}
