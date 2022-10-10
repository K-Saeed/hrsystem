package com.hrsystem.hrsystem.entity;

import javax.persistence.*;

@Entity
public class Department {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String dname;

    public Department() {

    }

    public Department(Integer id, String dname) {
        this.id = id;
        this.dname = dname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }


}
