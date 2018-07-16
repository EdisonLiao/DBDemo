package com.example.liaoshouwang.dbdemo.greendaodb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * created by edison 2018/7/16
 */
@Entity
public class GreenUser {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private int age;


    @Generated(hash = 669746326)
    public GreenUser(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public GreenUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Generated(hash = 1678257977)
    public GreenUser() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
