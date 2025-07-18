package com.geo4net.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geo4net.main.beans.Intervention;
import com.geo4net.main.exception.RecordNotFoundException;
import com.geo4net.main.repository.InterventionRepository;

@Service
public class InterventionService {

    @Autowired
    private InterventionRepository repository;

    /**
     * Récupère toutes les interventions pour un utilisateur spécifique
     *
     * @param username Nom d'utilisateur
     * @return Liste des interventions pour l'utilisateur donné
     */
    public List<Intervention> getAllInterventions(String username) {
        List<Intervention> interventionList = repository.findByUsername(username);

        if (interventionList.size() > 0) {
            return interventionList;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Récupère toutes les interventions pour tous les utilisateurs
     *
     * @return Liste de toutes les interventions
     */
    public List<Intervention> getAllInterventionsForAllUsers() {
        return repository.findAll();
    }

    /**
     * Crée ou met à jour une intervention
     *
     * @param intervention L'intervention à créer ou à mettre à jour
     * @return Liste des interventions de l'utilisateur après l'opération
     * @throws RecordNotFoundException Si aucune intervention avec un ID spécifique n'existe
     */
    public List<Intervention> createOrUpdateIntervention(Intervention intervention) throws RecordNotFoundException {
        if (intervention.getPsn() != null) {
            Optional<Intervention> interv = repository.findById(intervention.getPsn());

            if (interv.isPresent()) {
                Intervention newEntity = interv.get();
                newEntity.setSim(intervention.getSim());
                newEntity.setMatricule(intervention.getMatricule());
                newEntity.setLvcan(intervention.getLvcan());
                newEntity.setCurrLoc(intervention.getCurrLoc());
                newEntity.setNomTech(intervention.getNomTech());
                newEntity.setUser(intervention.getUser());

                repository.save(newEntity);
                return getAllInterventions(newEntity.getUser().getUsername());
            } else {
                repository.save(intervention);
                return getAllInterventions(intervention.getUser().getUsername());
            }
        } else {
            repository.save(intervention);
            return getAllInterventions(intervention.getUser().getUsername());
        }
    }

    /**
     * Supprime une intervention par son ID
     *
     * @param psn L'identifiant de l'intervention
     * @throws RecordNotFoundException Si l'intervention n'existe pas
     */
    public void deleteInterventionById(String psn) throws RecordNotFoundException {
        Optional<Intervention> intervention = repository.findById(psn);

        if (intervention.isPresent()) {
            repository.deleteById(psn);
        } else {
            throw new RecordNotFoundException("No Intervention record exists for given id", psn);
        }
    }
}
