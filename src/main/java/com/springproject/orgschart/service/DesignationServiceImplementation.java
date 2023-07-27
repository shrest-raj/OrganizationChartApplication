package com.springproject.orgschart.service;

import com.springproject.orgschart.dao.LevelRepository;
import com.springproject.orgschart.entity.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.substring;

@Service
public class DesignationServiceImplementation implements DesignationService{

    @Autowired
    private final LevelRepository levelRepository;

    public DesignationServiceImplementation(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public List<Level> getDesignationsSortedByLevelId() {
            return levelRepository.findAll(Sort.by(Sort.Direction.ASC, "levelId"));
    }

    @Override
    public Optional<Level> getDesignationByLevelId(int levelId) {
        Optional<Level> designation = levelRepository.findById(levelId);
        return designation;
    }


    @Override
    public Level addDesignation(Level level){
        Level existingLevel = levelRepository.findByLevelId(level.getLevelId());
        if(existingLevel != null){
            existingLevel.setDesignation(existingLevel.getDesignation() + ", " + level.getDesignation());
            return levelRepository.save(existingLevel);
        }
        else {
            return levelRepository.save(level);
        }
    }

    @Override
    public boolean deleteDesignation(Level level) {
        Level existingLevel = levelRepository.findByLevelId(level.getLevelId());
        if(existingLevel != null){
            String existingDesignation = existingLevel.getDesignation();
            String designationToRemove = level.getDesignation();

            if (existingDesignation.contains(designationToRemove)) {
                String updatedDesignation = existingDesignation.replace(designationToRemove, "").trim();
                existingLevel.setDesignation(updatedDesignation);
                levelRepository.save(existingLevel);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }



}
