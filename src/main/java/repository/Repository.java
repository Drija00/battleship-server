/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.repository;


import main.java.domain.GenericEntity;
import main.java.domain.Player;

import java.sql.SQLException;
import java.util.List;

public interface Repository {
    GenericEntity findRecord(GenericEntity entity) throws SQLException;
    List<Player> findRecords(GenericEntity entity) throws SQLException;
    Long insertRecord(GenericEntity entity) throws SQLException, ClassNotFoundException;

    void updateRecord(GenericEntity entity,long recordId) throws SQLException, ClassNotFoundException;

}
