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

import java.net.URL;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProductEditScreenController implements Initializable {

    @FXML
    private JFXDatePicker validadeDatePicker;

    @FXML
    private JFXTextField labTextField;

    @FXML
    private JFXTextField priceTextField;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXButton productUpdateButton;

    @FXML
    private JFXTextField loteTextField;

    @FXML
    private JFXSpinner progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productUpdateButton.disableProperty().bind(loteTextField.textProperty().isEmpty().or(nameTextField.textProperty().isEmpty().or(labTextField.textProperty().isEmpty().or
                (priceTextField.textProperty().isEmpty().or(validadeDatePicker.promptTextProperty().isEmpty())))));
        updateProductData();
    }

    @FXML
    void updatePressed(ActionEvent event) {
        progressIndicator.setVisible(true);
        setLockedData(true);
        new Thread(() -> {
            boolean parseError = false;
            try{
                Produto editableProduct = EasyFarma.dataManager.getEditableProduct();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String data = validadeDatePicker.getValue().format(formatter);
                editableProduct.setNome(nameTextField.getText());
                editableProduct.setLaboratorio(labTextField.getText());
                editableProduct.setPreco(Float.parseFloat(priceTextField.getText().replace(",", ".")));
                editableProduct.setValidade(Date.valueOf(data));

                SQLRunner.ExecuteSQLScript.SQLSet("ProductUpdate", editableProduct.getNome(), editableProduct.getValidade(), editableProduct.getLaboratorio(), editableProduct.getPreco(), editableProduct.getLote());
            }catch (NumberFormatException e){
                EasyFarma.logger.error("Erro ao editar o produto!", e);
                parseError = true;
            }

            boolean finalParseError = parseError;
            Platform.runLater(() -> {
                if(finalParseError){
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(), "Erro", "O campos pre\u00e7o s\u00f3 aceita n\u00fameros!");
                }else{
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(), "Sucesso", "O produto foi editado com sucesso!");
                    updateProductData();
                }
                progressIndicator.setVisible(false);
                setLockedData(false);
            });
        }).start();
    }

    private void updateProductData(){
        Produto editableProduct = EasyFarma.dataManager.getEditableProduct();

        loteTextField.setText(editableProduct.getLote());
        labTextField.setText(editableProduct.getLaboratorio());
        nameTextField.setText(editableProduct.getNome());
        priceTextField.setText(String.valueOf(editableProduct.getPreco()));
        validadeDatePicker.setValue(editableProduct.getValidade().toLocalDate());
    }

    private void setLockedData(boolean isProcessing){
        nameTextField.setDisable(isProcessing);
        priceTextField.setDisable(isProcessing);
        labTextField.setDisable(isProcessing);
        validadeDatePicker.setDisable(isProcessing);
        if(isProcessing){
            productUpdateButton.disableProperty().unbind();
            productUpdateButton.setDisable(true);
        }else{
            productUpdateButton.setDisable(false);
            productUpdateButton.disableProperty().bind(nameTextField.textProperty().isEmpty().or(labTextField.textProperty().isEmpty().or
                    (priceTextField.textProperty().isEmpty().or(validadeDatePicker.promptTextProperty().isEmpty()))));
        }
    }
}
