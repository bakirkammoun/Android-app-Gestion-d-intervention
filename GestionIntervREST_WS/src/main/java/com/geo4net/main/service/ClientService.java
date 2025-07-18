package com.geo4net.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.geo4net.main.beans.Client;
import com.geo4net.main.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    public List<Client> getAllClients() {
        return repository.findAll();
    }

    public Client addClient(Client client) {
        return repository.save(client);
    }

    public void deleteClient(String id) {
        repository.deleteById(id);
    }

    public Client updateClient(String id, Client client) {
        if (repository.existsById(id)) {
            client.setId(id);
            return repository.save(client);
        }
        return null;
    }
}
