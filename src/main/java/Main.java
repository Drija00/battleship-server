package main.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.lang.reflect.Method;
import java.net.URL;

public class Main extends Application {
    private Stage primaryStage;
    private AnchorPane layout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Server");

        initLayout();
    }

    private void initLayout() {
        try {
            String resourcePath = "Server.fxml";
            URL location = getClass().getResource(resourcePath);
            FXMLLoader loader = new FXMLLoader(location);
            layout = loader.load();

            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
            primaryStage.show();

            Object controllerObject = loader.getController();

            String serverStatus = invokeGenericMethod(controllerObject, "getServerStatus");
            System.out.println("Server Status: " + serverStatus);

            scene.getWindow().setOnCloseRequest((WindowEvent event) -> {
                try {
                    Object invoked =  invokeGenericMethod(controllerObject,"closeGameServer");
                    System.out.println("Form closed " + invoked.getClass());
                } catch (Exception e) {
                    e.getMessage();
                }
                Platform.exit();
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private <T> T invokeGenericMethod(Object clazz, String methodName) {
        try {
            Method method = clazz.getClass().getMethod(methodName);
            return (T) method.invoke(clazz);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
