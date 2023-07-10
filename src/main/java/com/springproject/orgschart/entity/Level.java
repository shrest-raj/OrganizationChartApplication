package com.springproject.orgschart.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "level")
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private int levelId;
    @Column(name = "designation")
    private String designation;

    // Default constructor
    public Level() {
    }

    // Parameterized constructor
    public Level(int levelId, String designation) {
        this.levelId = levelId;
        this.designation = designation;
    }

    // Getters and setters
    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public String toString() {
        return "Level{" +
                "levelId=" + levelId +
                ", designation='" + designation + '\'' +
                '}';
    }
}
