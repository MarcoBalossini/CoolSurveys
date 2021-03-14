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
    @Lob
    private byte[] photo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "questionnaire", cascade = CascadeType.PERSIST)
    private List<Question> questions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questionnaire")
    private List<Submission> submissions;

    public LocalDateTime getDate() {
        return date;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Question addQuestion(Question question) {
        getQuestions().add(question);
        question.setQuestionnaire(this);

        return question;
    }

    public Question removeQuestion(Question question) {
        getQuestions().remove(question);
        question.setQuestionnaire(null);

        return question;
    }

    public List<Submission> getSubmissions() {
        return this.submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public Submission addSubmission(Submission submission) {
        getSubmissions().add(submission);
        submission.setQuestionnaire(this);

        return submission;
    }

    public Submission removeSubmission(Submission submission) {
        getSubmissions().remove(submission);
        submission.setQuestionnaire(null);

        return submission;
    }
    public Integer getQId() {
        return qId;
    }
}
