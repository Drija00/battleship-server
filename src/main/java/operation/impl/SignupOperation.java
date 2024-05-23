/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.operation.impl;

import main.java.domain.GenericEntity;
import main.java.domain.Player;
import main.java.operation.AbstractGenericOperation;

public class SignupOperation extends AbstractGenericOperation<Player, Player> {

    GenericEntity object = null;
    
    @Override
    protected void preconditions(Player entity) throws Exception {
        if(entity.getFirstname() == null || entity.getFirstname().isEmpty()) {
            throw new Exception("name is required");
        }
        if(entity.getLastname() == null || entity.getLastname().isEmpty()) {
            throw new Exception("Lastname is required");
        }
        if(entity.getUsername() == null || entity.getUsername().isEmpty()) {
            throw new Exception("Username is required");
        }
        if(entity.getPassword()== null || entity.getPassword().isEmpty()) {
            throw new Exception("Password is required");
        }
    }

    @Override
    protected void executeOperation(Player entity) throws Exception {
        if(repository.findRecord(new Player(entity.getFirstname())) == null) {
            Player registered = new Player();
            registered.setId(repository.insertRecord(entity));
            object = registered;
        } else {
           throw new RuntimeException("Firstname already exists");
        }
    }

    public GenericEntity getObject() {
        return object;
    }
    
}
