package br.com.apoo2021.farm;

import br.com.apoo2021.farm.database.SQLRunner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FarmApp extends Application {

    public static final Logger logger = LogManager.getLogger(FarmApp.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/LoginScreen.fxml")))));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
