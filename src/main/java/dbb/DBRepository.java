/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.dbb;

import main.java.repository.Repository;

import java.sql.SQLException;


public interface DBRepository extends Repository {
    default public void connect() throws Exception{
        DBBConnectionFactory.getInstance().getConnection();
    }
    
    default public void disconnect() throws Exception{
        DBBConnectionFactory.getInstance().getConnection().close();
    }
    
    default public void commit() throws Exception{
        DBBConnectionFactory.getInstance().getConnection().commit();
    }
    
    default public void rollback() throws Exception{
        DBBConnectionFactory.getInstance().getConnection().rollback();
    }
}
