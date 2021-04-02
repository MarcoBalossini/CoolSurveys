package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "credentials")
@NamedQuery(name="Credentials.findAll", query="SELECT c FROM Credentials c")
public class Credentials implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column (name = "mail", nullable = false, unique = true)
    private String mail;

    //sets admin to false by default
    @Column (nullable = false)
    private boolean admin = false;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "credentials", orphanRemoval = true)
    private User user;

    public Credentials(int userId, String username, String passwordHash, String mail, boolean admin) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.mail = mail;
        this.admin = admin;
    }

    public Credentials() {}

    public String getPassword_hash() {
        return passwordHash;
    }

    public void setPassword_hash(String password_hash) {
        this.passwordHash = password_hash;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUser_id() {
        return userId;
    }
}
