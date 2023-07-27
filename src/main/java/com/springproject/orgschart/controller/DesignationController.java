package com.springproject.orgschart.controller;

import com.springproject.orgschart.entity.Level;
import com.springproject.orgschart.service.DesignationServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DesignationController {

    @Autowired
    private DesignationServiceImplementation designationServiceImpl;

    public DesignationController(DesignationServiceImplementation designationServiceImpl){
        this.designationServiceImpl =designationServiceImpl;
    }


    @GetMapping("/designations")
    public ResponseEntity<List<Level>>getAllDesignationSortedByLevelId(){
        List<Level> designations= designationServiceImpl.getDesignationsSortedByLevelId();
        return ResponseEntity.ok(designations);
    }

    @GetMapping("/designations/{levelId}")
    public ResponseEntity<Optional<Level>> getDesignationByLevelId(@PathVariable int levelId){
        Optional<Level> designations= designationServiceImpl.getDesignationByLevelId(levelId);
        return ResponseEntity.ok(designations);
    }

    @PostMapping("/designations")
    public ResponseEntity<Optional<Level>> addDesignation(@RequestBody Level level){
        Optional<Level> createdDesignation= Optional.ofNullable(designationServiceImpl.addDesignation(level));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDesignation);
    }

    @DeleteMapping("/designations")
    public ResponseEntity<String> deleteDesignation(@RequestBody Level level){
        boolean deleted = designationServiceImpl.deleteDesignation(level);
        if(deleted){
            return ResponseEntity.ok("Designation deleted successfully!");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to delete,Check the values once again!");
        }
    }


}
