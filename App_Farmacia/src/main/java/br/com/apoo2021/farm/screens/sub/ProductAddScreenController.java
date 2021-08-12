package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.EasyFarma;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Produto;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ProductAddScreenController implements Initializable {

    @FXML
    private AnchorPane addPane;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXTextField labTextField;

    @FXML
    private JFXTextField priceTextField;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXButton addProductButton;

    @FXML
    private JFXSpinner progressIndicator;

    @FXML
    private JFXTextField loteTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addProductButton.disableProperty().bind(loteTextField.textProperty().isEmpty().or(nameTextField.textProperty().isEmpty().or(labTextField.textProperty().isEmpty().or
                (priceTextField.textProperty().isEmpty().or(datePicker.promptTextProperty().isEmpty())))));
    }

    @FXML
    void addProductPressed(ActionEvent event){
        progressIndicator.setVisible(true);
        setLockedData(true);
        new Thread(() -> {
            boolean parseError = false;
            List<Object> loteLista = null;
            try {
                if(!loteTextField.getText().contains(" ")) {
                    loteLista = SQLRunner.ExecuteSQLScript.SQLSelect("ProductLoteVerify", loteTextField.getText());
                    if (loteLista == null || loteLista.isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String data = datePicker.getValue().format(formatter);
                        Produto novoProduto = new Produto();
                        novoProduto.setLote(loteTextField.getText());
                        novoProduto.setPreco(Float.parseFloat(priceTextField.getText().replace(",", ".")));
                        novoProduto.setNome(nameTextField.getText());
                        novoProduto.setLaboratorio(labTextField.getText());
                        novoProduto.setValidade(Date.valueOf(data));
                        SQLRunner.ExecuteSQLScript.SQLSet("ProductInsert", loteTextField.getText(), EasyFarma.dataManager.getFarmManager().getFarmaceutico().getCrf(),
                                nameTextField.getText(), Float.parseFloat(priceTextField.getText().replace(",", ".")),labTextField.getText(),
                                Date.valueOf(data));
                        EasyFarma.dataManager.getProductManager().getProdutosList().add(novoProduto);
                    }
                }
            }catch(NumberFormatException e){
                parseError = true;
            }
            boolean finalParseError = parseError;
            List<Object> finalLoteLista = loteLista;
            Platform.runLater(() -> {
                if(finalParseError){
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(),"Erro","O campos pre\u00e7o s\u00f3 aceita n\u00fameros!");
                }else if(loteTextField.getText().contains(" ")){
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(),"Erro","Lote Inv\u00e1lido !");
                }else if(finalLoteLista != null && !finalLoteLista.isEmpty()){
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(),"Erro","Lote j\u00e1 registrado !");
                }else
                {
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(),"Registrado","Registrado com sucesso!");
                }
                progressIndicator.setVisible(false);
                setLockedData(false);
            });
        }).start();
        }
        private void setLockedData(boolean isProcessing){
            loteTextField.setDisable(isProcessing);
            nameTextField.setDisable(isProcessing);
            priceTextField.setDisable(isProcessing);
            labTextField.setDisable(isProcessing);
            datePicker.setDisable(isProcessing);
            if(isProcessing){
                 addProductButton.disableProperty().unbind();
                 addProductButton.setDisable(true);
            }else{
                addProductButton.setDisable(false);
                addProductButton.disableProperty().bind(nameTextField.textProperty().isEmpty().or(labTextField.textProperty().isEmpty().or
                (priceTextField.textProperty().isEmpty().or(datePicker.promptTextProperty().isEmpty()))));
            }
        }
}

