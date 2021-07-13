package it.polimi.db2.coolsurveys.entities;

import jdk.jfr.Name;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name = "Submission.get", query = "Select s from Submission s where s.id.userId = :userId and s.id.questionnaireId = :questionnaireId"),
        @NamedQuery(name = "Submission.getResponders", query = "Select s.user from Submission s where s.id.questionnaireId = :questionnaireId and s.submitted = :hasSubmitted")
})

@Entity
@Table(name = "submission")
public class Submission implements Serializable {
    
    public enum Gender {
        MALE,
        FEMALE,
        NON_BINARY,
        DUNNO
    }

    public enum ExpertiseLevel {
        LOW,
        MEDIUM,
        HIGH
    }

    @EmbeddedId
    private SubmissionPK id;

    @Column(name = "age")
    private Integer age = null;

    @Column(name = "sex")
    private Integer sex = null;

    @Column(name = "expertise_level")
    private Integer expertiseLevel = null;

    @ManyToOne
    @JoinColumn(name="questionnaire_id")
    private Questionnaire questionnaire;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "submitted", nullable = false)
    private Boolean submitted = false;


    public Submission() {}
    public Submission(User user, Questionnaire questionnaire) {
        this.user = user;

        user.getSubmission().add(this); //updates directly

        this.questionnaire = questionnaire;

        questionnaire.addSubmission(this);
    }

    public SubmissionPK getId() {
        return id;
    }

    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public void setId(SubmissionPK id) {
        this.id = id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setExpertiseLevel(Integer expertiseLevel) {
        this.expertiseLevel = expertiseLevel;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getExpertiseLevel() {
        return expertiseLevel;
    }

    public void setExpertiseLevel(int expertiseLevel) {
        this.expertiseLevel = expertiseLevel;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
