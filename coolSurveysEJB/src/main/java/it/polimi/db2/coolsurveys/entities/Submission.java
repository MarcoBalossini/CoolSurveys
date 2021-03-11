package it.polimi.db2.coolsurveys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Submission {

    //TODO: composite keys (with @Embedded?)
    @Id
    private int user_id;

    @Id
    private int questionnaire_id;

    @Column (nullable = false)
    private boolean submitted;

    //todo: initialize to null?
    @Column
    private int age = 0;

    @Column
    private boolean sex;

    //TODO: use enum?
    @Column
    private int expertiseLevel;

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

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getExpertiseLevel() {
        return expertiseLevel;
    }

    public void setExpertiseLevel(int expertiseLevel) {
        this.expertiseLevel = expertiseLevel;
    }
}
