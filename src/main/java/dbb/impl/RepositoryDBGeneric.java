/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.dbb.impl;


import main.java.dbb.DBBConnectionFactory;
import main.java.dbb.DBRepository;
import main.java.domain.GenericEntity;
import main.java.domain.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RepositoryDBGeneric implements DBRepository {

    @Override
    public GenericEntity findRecord(GenericEntity entity) throws SQLException {
        try {
            Connection connection = DBBConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM " + entity.getClassName() + " WHERE " + entity.getWhereCondition();
            System.out.println(query);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            boolean signal = resultSet.next();
            if (signal == true) {
                return entity.getNewRecord(resultSet);
            }
            
            return null;
       
        } catch (SQLException ex) {
            throw new SQLException("Error retrieving user from database");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Player> findRecords(GenericEntity entity) throws SQLException {
        try {
            Connection connection = DBBConnectionFactory.getInstance().getConnection();
            String query = "SELECT * FROM " + entity.getClassName() + " ORDER BY wins DESC";
            System.out.println(query);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Player> players = new ArrayList<>();
            int i1=0;
            while(resultSet.next()){
                System.out.println("result set count "+(++i1));
                players.add((Player) entity.getNewRecord(resultSet));
            }
            return players;


        } catch (SQLException ex) {
            throw new SQLException("Error retrieving user from database");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long insertRecord(GenericEntity entity) throws SQLException, ClassNotFoundException {
        try {
            String query;
            Connection connection = DBBConnectionFactory.getInstance().getConnection();
            query = "INSERT INTO "+ entity.getClassName() + " (" + entity.getAtrNames() + ") VALUES (" + entity.getAtrValues() + ")";
            System.out.println(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs != null && rs.next()) {
                return rs.getLong(1);
            } else {
                return 0L;
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void updateRecord(GenericEntity entity, long recordId) throws SQLException, ClassNotFoundException {
        try {
            String query;
            Connection connection = DBBConnectionFactory.getInstance().getConnection();

            query = "UPDATE " + entity.getClassName() + " SET " + entity.setAtrValues();
            query += " WHERE id = " + entity.getId();

            System.out.println(query);

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw ex;
        }
    }




}
