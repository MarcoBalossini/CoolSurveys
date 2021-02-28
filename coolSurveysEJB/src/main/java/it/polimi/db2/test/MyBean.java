package it.polimi.db2.test;

import javax.ejb.Stateless;

@Stateless
public class MyBean {
    public String sayHello() {
        return "Hello!";
    }
}
