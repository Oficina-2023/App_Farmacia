package br.com.apoo2021.farm;

import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Farmaceutico;
import br.com.apoo2021.farm.objects.ScreenData;
import br.com.apoo2021.farm.objects.UserManager;
import br.com.apoo2021.farm.util.ScreenAdjusts;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FarmApp extends Application {

    public static final Logger logger = LogManager.getLogger(FarmApp.class.getName());

    public static final UserManager userManager = new UserManager();
    public static final ScreenData screenData = new ScreenData();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/LoginScreen.fxml")));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        ScreenAdjusts.centerScreen(primaryStage);
        ScreenAdjusts.setDraggable(root, primaryStage);
    }
}
