package com.geo4net.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.geo4net.main.beans.Technicien;
import com.geo4net.main.service.TechnicienService;

@RestController
@RequestMapping("/technicien")
public class TechnicienController {

    @Autowired
    private TechnicienService service;

    @GetMapping("/all")
    public ResponseEntity<List<Technicien>> getAllTechniciens() {
        return ResponseEntity.ok(service.getAllTechniciens());
    }

    @PostMapping("/add")
    public ResponseEntity<Technicien> addTechnicien(@RequestBody Technicien technicien) {
        return ResponseEntity.ok(service.addTechnicien(technicien));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTechnicien(@PathVariable String id) {
        service.deleteTechnicien(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Technicien> updateTechnicien(@PathVariable String id, @RequestBody Technicien technicien) {
        Technicien updatedTechnicien = service.updateTechnicien(id, technicien);
        return ResponseEntity.ok(updatedTechnicien);
    }
}
