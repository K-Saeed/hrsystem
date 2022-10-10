package com.hrsystem.hrsystem.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Team {
    @Id
    @Column(name = "id")
    private Integer id;
    private String tname;

    public Team() {
    }

    public Team(Integer id, String tname) {
        this.id = id;
        this.tname = tname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
