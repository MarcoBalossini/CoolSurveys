package it.polimi.db2.coolsurveys.entities;

import jdk.jfr.Name;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(name = "User.selectByUsername",
                query = "select u from User u where u.credentials.username = :username"),
        @NamedQuery(name = "User.selectByMail",
                query = "select u from User u where u.credentials.mail = :mail"),
        @NamedQuery(name = "User.selectOrderedByPoints",
                query = "select u from User u order by u.points desc ")
})
@Table(name = "user")
public class User implements Serializable {

    @Id
    @OneToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Credentials credentials;

    @Column(name = "points")
    private Integer points = 0;

    @Column(name = "blocked_until")
    private LocalDateTime blockedUntil = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Submission> submission = new ArrayList<>();

    public User(Credentials credentials) {
        this.credentials = credentials;
    }

    public User() {}

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
        credentials.setUser(this);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Submission> getSubmission() {
        return submission;
    }

    public void setSubmission(List<Submission> submission) {
        this.submission = submission;
    }



    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(LocalDateTime blocked_until) {
        this.blockedUntil = blocked_until;
    }

}
