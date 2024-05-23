/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.operation.impl;

import main.java.domain.GenericEntity;
import main.java.domain.Player;
import main.java.operation.AbstractGenericOperation;

public class LoginOperation extends AbstractGenericOperation<Player, Player> {
    
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
        Player  player = (Player) repository.findRecord(new Player(entity.getFirstname()));
        if(player != null) {
            if(!player.getPassword().equals(entity.getPassword())) {
                throw new RuntimeException("Bad password!");
            }
            object = player;
        } else {
           throw new RuntimeException("Invalid firstname!");
        }
    }

    public GenericEntity getObject() {
        return object;
    }
}
