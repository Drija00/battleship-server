/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.settings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertiesLoader {
    private Properties properties;
    private static PropertiesLoader instance;
    
    private PropertiesLoader() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream("src/main/java/config/DatabaseProperties.properties"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static PropertiesLoader getInstance() {
        if (instance == null) {
            instance = new PropertiesLoader();
        }
        return instance;
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key, "N/A");
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void saveProperties() throws FileNotFoundException, IOException {
        properties.store(new FileOutputStream("src/main/java/config/DatabaseProperties.properties"), "Successfully saved");
    }
}
