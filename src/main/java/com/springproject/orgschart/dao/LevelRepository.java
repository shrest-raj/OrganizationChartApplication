package com.springproject.orgschart.dao;

import com.springproject.orgschart.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level, Integer> {
    Level findByDesignation(String designation);

    boolean existsByDesignation(String designation);

    Level findByLevelId(int levelId);
}
