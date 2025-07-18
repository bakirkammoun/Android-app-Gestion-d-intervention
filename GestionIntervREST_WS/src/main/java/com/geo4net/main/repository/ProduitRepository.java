package com.geo4net.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.geo4net.main.beans.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, String> {}
