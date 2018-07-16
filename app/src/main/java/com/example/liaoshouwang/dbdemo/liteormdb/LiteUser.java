package com.example.liaoshouwang.dbdemo.liteormdb;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * created by edison 2018/7/16
 */
@Table("LITE_USER")
public class LiteUser {

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    private String name;

    private int age;


    public LiteUser(String name, int age) {
        this.name = name;
        this.age = age;
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
