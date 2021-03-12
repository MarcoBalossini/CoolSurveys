package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "submission")
public class Submission implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Id
    @Column(name = "questionnaire_id", nullable = false)
    private Integer questionnaireId;

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
    private Questionnaire questionnaire;

    @ManyToOne
    private User user;

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
