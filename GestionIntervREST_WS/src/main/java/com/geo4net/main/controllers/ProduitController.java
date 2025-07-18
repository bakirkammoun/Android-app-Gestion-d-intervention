package com.geo4net.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.geo4net.main.beans.Produit;
import com.geo4net.main.service.ProduitService;

@RestController
@RequestMapping("/produit")
public class ProduitController {

    @Autowired
    private ProduitService service;

    @GetMapping("/all")
    public ResponseEntity<List<Produit>> getAllProduits() {
        return ResponseEntity.ok(service.getAllProduits());
    }

    @PostMapping("/add")
    public ResponseEntity<Produit> addProduit(@RequestBody Produit produit) {
        return ResponseEntity.ok(service.addProduit(produit));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable String id) {
        service.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable String id, @RequestBody Produit produit) {
        Produit updatedProduit = service.updateProduit(id, produit);
        return ResponseEntity.ok(updatedProduit);
    }
}
