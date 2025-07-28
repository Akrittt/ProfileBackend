package com.portfolio.controller;

import com.portfolio.entity.projectEntity;
import com.portfolio.repository.projectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins="http://localhost:5173")
public class projectController {
    @Autowired
    private projectsRepository repository;

    @GetMapping
    public List<projectEntity> getAllProjects(){
        return repository.findAll();
    }

    @PostMapping
    public projectEntity createProject(@RequestBody projectEntity project){
        return repository.save(project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable String id){
        repository.deleteById(id);
    }
}
