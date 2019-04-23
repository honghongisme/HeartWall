package com.example.administrator.ding.model.entry;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/23.
 */

public class User implements Serializable {

    private int id;
    private String accountNumber;
    private String password;
    private String name;
    private String sex;
    private String identity;
    private String department;

    public User() {

    }

    public User(int id, String accountNumber, String password, String name, String sex, String identity, String department) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.identity = identity;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", identity='" + identity + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
