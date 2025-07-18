package com.geo4net.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.geo4net.main.beans.Technicien;
import com.geo4net.main.repository.TechnicienRepository;

@Service
public class TechnicienService {

    @Autowired
    private TechnicienRepository repository;

    public List<Technicien> getAllTechniciens() {
        return repository.findAll();
    }

    public Technicien addTechnicien(Technicien technicien) {
        return repository.save(technicien);
    }

    public void deleteTechnicien(String id) {
        repository.deleteById(id);
    }

    public Technicien updateTechnicien(String id, Technicien technicien) {
        if (repository.existsById(id)) {
            technicien.setId(id);
            return repository.save(technicien);
        }
        return null;
    }
}
