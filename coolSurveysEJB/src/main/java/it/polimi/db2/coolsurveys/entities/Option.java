package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name = "Option.selectByText",
                query = "select o from Option o where o.text = :text")
})

@Entity
@Table(name = "options")
public class Option implements Serializable {

    /*@EmbeddedId
    private OptionPK id;*/

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "option_id")
    private Integer optionId;

    @Column(name = "text", nullable = false)
    private String text;

    //@MapsId("questionId")
    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    private Question question;

    public Option(String text) {
        this.text = text;
    }

    public Option() {}


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


}
