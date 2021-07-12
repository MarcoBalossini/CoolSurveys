package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NamedQueries({
        @NamedQuery(name = "Questionnaire.selectByName",
                query = "select q from Questionnaire q where q.name = :name"),
        @NamedQuery(name = "Questionnaire.selectByDate",
                query = "select q from Questionnaire q where q.date = :date"),
        @NamedQuery(name = "Questionnaire.findAll",
                query = "select q from Questionnaire q"),
        @NamedQuery(name = "Questionnaire.findPast",
                query = "select q from Questionnaire q where q.date < :date")
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
    private LocalDate date;

    @Column (name = "photo", nullable = false)
    @Lob
    private byte[] photo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "questionnaire", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questionnaire")
    private List<Submission> submissions = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "questionnaire", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public Questionnaire() {
    }

    public Questionnaire(String name, byte[] photo) {
        this.name = name;
        this.photo = photo;
    }

    public Questionnaire(String name, byte[] photo, LocalDate date) {
        this.name = name;
        this.photo = photo;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public void addQuestion(Question question) {

        getQuestions().add(question);
        question.setQuestionnaire(this);

    }

    public Question removeQuestion(Question question) {
        getQuestions().remove(question);
        //question.setQuestionnaire(null);

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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;

        for (Review review : reviews)
            review.setQuestionnaire(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire that = (Questionnaire) o;
        return qId.equals(that.qId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qId);
    }
}
