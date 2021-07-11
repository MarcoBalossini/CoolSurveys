package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "log")
@IdClass(LogPK.class)
public class Log implements Serializable {

    @Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private int userId;

    @Id
    @Column(name = "date")
    private Timestamp dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Log() {}

    public Log(User user) {
        this.user = user;
        this.dateTime = new Timestamp(System.currentTimeMillis());
        System.out.println("Current Time: " + dateTime.toLocalDateTime());
    }

    public LocalDateTime getDateTime() {
        return dateTime.toLocalDateTime();
    }

}
