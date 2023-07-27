package com.springproject.orgschart.service;

import com.springproject.orgschart.entity.Level;
import org.w3c.dom.ls.LSException;

import java.util.List;
import java.util.Optional;

public interface DesignationService {

     List<Level> getDesignationsSortedByLevelId();

   Optional<Level> getDesignationByLevelId(int levelId);

     Level addDesignation(Level level);

     boolean deleteDesignation(Level level);

}

