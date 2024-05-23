package main.java.operation.impl;


import main.java.domain.GenericEntity;
import main.java.domain.Player;
import main.java.operation.AbstractGenericOperation;

public class ChangeCredentialsOperation extends AbstractGenericOperation<Player,Player> {
    GenericEntity object = null;


    @Override
    protected void preconditions(Player entity) throws Exception {
        if(entity.getFirstname() == null || entity.getFirstname().isEmpty()) {
            throw new Exception("Firstname is required");
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
        Player  player = (Player) repository.findRecord(entity);
        if(player != null) {
            System.out.println(entity.toString()+" entity");
            System.out.println(entity.toString()+" player");
           repository.updateRecord(entity, player.getId());

            object = entity;
        } else {
            throw new RuntimeException("ERROR");
        }
    }

    public GenericEntity getObject() {
        return null;
    }
}
