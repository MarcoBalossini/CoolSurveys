package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "Questionnaire.selectByName",
                query = "select q from Questionnaire q where q.name = :name")
})

@Entity
@Table(name = "questionnaire")
public class Questionnaire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_id", nullable = false)
    private Integer qId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private final LocalDateTime date = LocalDateTime.now();

    /**
     * Photo_path not unique since a questionnaire may be re-proposed
     */
    //TODO: see how images in byte[] work
    @Column (name = "photo", nullable = false)
    private byte[] photo;

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

    public Integer getQ_id() {
        return qId;
    }
}
