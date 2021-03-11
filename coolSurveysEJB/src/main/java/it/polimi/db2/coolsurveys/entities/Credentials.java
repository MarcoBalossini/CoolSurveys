package it.polimi.db2.coolsurveys.entities;

import javax.persistence.*;

@Entity
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;

    @Column (nullable = false, unique = true)
    private String username;

    @Column (nullable = false)
    private String password_hash;

    @Column (nullable = false, unique = true)
    private String mail;

    //sets admin to false by default
    @Column (nullable = false)
    private boolean admin = false;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    private User user;

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
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

    public int getUser_id() {
        return user_id;
    }
}
