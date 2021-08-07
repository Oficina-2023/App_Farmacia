package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.util.FarmDialogs;
import br.com.apoo2021.farm.util.ScreenAdjusts;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoadScreenController implements Initializable {

    @FXML
    private Text loadMessageTextField;

    @FXML
    private StackPane stackPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMessageTextField.setText("Carregando...");
        new Thread(() -> {
            boolean loaded = false;
            Thread updateFarm = new Thread(() -> {
                FarmApple.dataManager.getFarmManager().updateFarmData();
            });
            Thread updateProduct = new Thread(() -> {
                FarmApple.dataManager.getProductManager().updateProductList();
            });
            Thread updateCostumer = new Thread(() -> {
                FarmApple.dataManager.getCostumerManager().updateCostumerList();
            });
            Thread updateSells = new Thread(() -> {
                FarmApple.dataManager.getSellManager().updateSellList();
            });
            updateFarm.start();
            updateProduct.start();
            updateCostumer.start();
            updateSells.start();
            try{
                loadMessageTextField.setText("Carregando dados de usu\u00e1rio...");
                updateFarm.join();
                loadMessageTextField.setText("Carregando produtos...");
                updateProduct.join();
                loadMessageTextField.setText("Carregando clientes...");
                updateCostumer.join();
                loadMessageTextField.setText("Carregando vendas...");
                updateSells.join();
                loaded = true;
                loadMessageTextField.setText("Finalizando carregamento...");
            }catch(InterruptedException e){
                FarmApple.logger.error("Erro nos Threads no carregamento de dados no login",e);
            }
            boolean finalLoaded = loaded;
            Platform.runLater(() -> {
                if(finalLoaded){
                    try{
                        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/MainScreen.fxml")));
                        Stage stage = (Stage) loadMessageTextField.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        ScreenAdjusts.centerScreen(stage);
                        ScreenAdjusts.setDraggable(root,stage);
                    }catch(IOException e){
                        FarmApple.logger.error("Error ao tentar abrir a tela principal!",e);
                    }
                }else{
                    FarmDialogs.showLoginError(stackPane, loadMessageTextField);
                }
            });
        }).start();
    }
}
