package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "submission")
public class Submission implements Serializable {

    @EmbeddedId
    private SubmissionPK id;

    @Column(name = "submitted", nullable = false)
    private boolean submitted;

    @Column(name = "age")
    private Integer age = null;

    @Column(name = "sex")
    private Integer sex = null;

    //TODO: use enum?
    @Column(name = "expertise_level")
    private Integer expertiseLevel = null;

    @ManyToOne
    @JoinColumn(name="questionnaire_id")
    private Questionnaire questionnaire;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    public SubmissionPK getId() {
        return id;
    }

    public void setId(SubmissionPK id) {
        this.id = id;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
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
    public int getAge() {
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
