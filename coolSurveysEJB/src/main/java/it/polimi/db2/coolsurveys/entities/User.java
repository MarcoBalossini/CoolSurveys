package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "User.selectByUsername",
                query = "select u from User u where u.credentials.username = :username"),
        @NamedQuery(name = "User.selectByMail",
                query = "select u from User u where u.credentials.mail = :mail")
})

@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "points")
    private Integer points = 0;

    @Column(name = "blocked_until")
    private LocalDateTime blocked_until = LocalDateTime.now();

    @OneToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Credentials credentials;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Answer> answers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Submission> submission;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getBlocked_until() {
        return blocked_until;
    }

    public void setBlocked_until(LocalDateTime blocked_until) {
        this.blocked_until = blocked_until;
    }

    public int getUser_id() {
        return userId;
    }

    public void setUser_id(int user_id) {
        this.userId = user_id;
    }
}
