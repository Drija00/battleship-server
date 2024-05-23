/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.dbb;


import main.java.settings.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBBConnectionFactory {
    private Connection connection;
    private static DBBConnectionFactory instance;

    private DBBConnectionFactory() {
    }
    
    public static DBBConnectionFactory getInstance(){
        if(instance == null){
            instance = new DBBConnectionFactory();
        }
        
        return instance;
    }
    
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if(connection == null || connection.isClosed()){
           
            String url = PropertiesLoader.getInstance().getProperty("url");
            String username = PropertiesLoader.getInstance().getProperty("username");
            String password = PropertiesLoader.getInstance().getProperty("password");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database!");
            connection.setAutoCommit(false);
        
       }
        
        return connection;
    }
    
    public void closeConnection() throws SQLException {
        connection.close();
        instance = null;
    }
}
