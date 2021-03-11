package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedQueries({
        @NamedQuery(name = "User.selectByUsername",
                query = "select u from User u where u.credentials.username = :username"),
        @NamedQuery(name = "User.selectByMail",
                query = "select u from User u where u.credentials.mail = :mail")
})

@Entity
public class User {

    @Id
    private int user_id;

    @Column
    private int points = 0;

    @Column
    private LocalDateTime blocked_until = LocalDateTime.now();

    @OneToOne (fetch = FetchType.EAGER, mappedBy = "user_id", cascade = CascadeType.ALL)
    private Credentials credentials;

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
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
