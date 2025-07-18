package com.geo4net.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.geo4net.main.beans.Produit;
import com.geo4net.main.repository.ProduitRepository;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository repository;

    public List<Produit> getAllProduits() {
        return repository.findAll();
    }

    public Produit addProduit(Produit produit) {
        return repository.save(produit);
    }

    public void deleteProduit(String id) {
        repository.deleteById(id);
    }

    public Produit updateProduit(String id, Produit produit) {
        if (repository.existsById(id)) {
            produit.setId(id);
            return repository.save(produit);
        }
        return null;
    }
}
