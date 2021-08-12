package br.com.apoo2021.farm.util;

import br.com.apoo2021.farm.FarmApple;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Cliente;
import br.com.apoo2021.farm.objects.ProductCart;
import br.com.apoo2021.farm.objects.Produto;
import br.com.apoo2021.farm.objects.Vendas;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class FarmDialogs {

    public static void showSoftwareCloseDialog(StackPane pane, Node... nodes){
        JFXDialogLayout content = new JFXDialogLayout();
        Text head = new Text("Confirma\u00e7\u00e3o de Sa\u00edda");
        Text body = new Text("Deseja realmente sair?");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> System.exit(0));
        content.setStyle("-fx-background-color: #2f3136;");
        noButton.setStyle("-fx-text-fill: white;");
        yesButton.setStyle("-fx-text-fill: white;");
        content.setActions(yesButton, noButton);
        dialog.show();

        for(Node node : nodes){
            node.disableProperty().bind(dialog.visibleProperty());
        }
    }

    public static void showDialog(StackPane pane,String title, String message){
        JFXDialogLayout content = new JFXDialogLayout();
        Text head = new Text(title);
        Text body = new Text(message);
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXButton closeButton = new JFXButton("Fechar");
        closeButton.setOnAction(event -> dialog.close());
        content.setStyle("-fx-background-color: #2f3136;");
        closeButton.setStyle("-fx-text-fill: white;");
        content.setActions(closeButton);
        dialog.show();
    }

    public static void showSoftwareLogoutDialog(StackPane pane, JFXButton closeButton, Node... nodes){
        JFXDialogLayout content = new JFXDialogLayout();
        Text head = new Text("Confirma\u00e7\u00e3o de Sa\u00edda");
        Text body = new Text("Deseja realmente realizar logout?");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> {
            try{
                Parent root = FXMLLoader.load(Objects.requireNonNull(FarmDialogs.class.getClassLoader().getResource("screens/LoginScreen.fxml")));
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                ScreenAdjusts.centerScreen(stage);
                ScreenAdjusts.setDraggable(root,stage);
                FarmApple.dataManager.getFarmManager().clearFarmData();
            }catch(IOException e){
                FarmApple.logger.error("Error ao tentar retornar a tela de login!",e);
            }
        });
        content.setStyle("-fx-background-color: #2f3136;");
        noButton.setStyle("-fx-text-fill: white;");
        yesButton.setStyle("-fx-text-fill: white;");
        content.setActions(yesButton, noButton);
        dialog.show();

        for(Node node : nodes){
            node.disableProperty().bind(dialog.visibleProperty());
        }
    }

    public static void showLoginError(StackPane pane, Node node){
        JFXDialogLayout content = new JFXDialogLayout();
        Text head = new Text("Error");
        Text body = new Text("Error ao carregar os dados.\nTente novamente mais tarde!");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXButton closeButton = new JFXButton("Fechar");
        closeButton.setOnAction(event -> {
            try{
                dialog.close();
                FarmApple.dataManager.getFarmManager().clearFarmData();
                Parent root = FXMLLoader.load(Objects.requireNonNull(FarmDialogs.class.getClassLoader().getResource("screens/LoginScreen.fxml")));
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(new Scene(root));
                ScreenAdjusts.centerScreen(stage);
                ScreenAdjusts.setDraggable(root,stage);
            }catch(IOException e){
                FarmApple.logger.error("Error ao tentar abrir a tela de login após um error de carregamento!",e);
            }
        });
        content.setStyle("-fx-background-color: #2f3136;");
        closeButton.setStyle("-fx-text-fill: white;");
        content.setActions(closeButton);
        dialog.show();
    }

    public static void showDeleteProductConfirmDialog(StackPane pane, ListView<Produto> listView, Produto produto){
        JFXDialogLayout content = new JFXDialogLayout();
        String produtoName = produto.getNome();
        if(produtoName.length() > 20){
            produtoName = produtoName.substring(0,20) + "...";
        }
        Text head = new Text("Confirma\u00e7\u00e3o de exclus\u00e3o");
        Text body = new Text("Deseja realmente deletar o produto "+ produtoName +"?");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM);
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> {
            try{
                SQLRunner.ExecuteSQLScript.SQLSet("ProductRemove", produto.getLote());
                listView.getItems().remove(produto);
                FarmApple.dataManager.getProductManager().removeProduto(produto.getLote());
            }catch (Exception e){
                FarmApple.logger.error("Error ao remover o produto!", e);
            }
            dialog.close();
        });
        content.setStyle("-fx-background-color: #2f3136;");
        noButton.setStyle("-fx-text-fill: white;");
        yesButton.setStyle("-fx-text-fill: white;");
        content.setActions(yesButton, noButton);
        dialog.show();
    }

    public static void showDeleteCustomerConfirmDialog(StackPane pane, ListView<Cliente> listView, Cliente cliente){
        JFXDialogLayout content = new JFXDialogLayout();
        String clienteCpf = cliente.getCpf();
        if(clienteCpf.length() > 20){
            clienteCpf = clienteCpf.substring(0,20) + "...";
        }
        Text head = new Text("Confirma\u00e7\u00e3o de exclus\u00e3o");
        Text body = new Text("Deseja realmente deletar o Cliente de CPF "+ clienteCpf +"?");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM);
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> {
            try{
                SQLRunner.ExecuteSQLScript.SQLSet("ClienteRemove", cliente.getCpf());
                listView.getItems().remove(cliente);
                FarmApple.dataManager.getCostumerManager().removeCliente(cliente.getCpf());
            }catch (Exception e){
                FarmApple.logger.error("Error ao remover o cliente!", e);
            }
            dialog.close();
        });
        content.setStyle("-fx-background-color: #2f3136;");
        noButton.setStyle("-fx-text-fill: white;");
        yesButton.setStyle("-fx-text-fill: white;");
        content.setActions(yesButton, noButton);
        dialog.show();
    }

    public static void showCartAddDialog(StackPane pane, Produto produto){
        JFXDialogLayout content = new JFXDialogLayout();
        Text head = new Text("Adi\u00e7\u00e3o de Produtos");
        Text body = new Text("Digite a quantidade de produtos a adicionar no carrinho.");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXTextField quantityTextField = new JFXTextField();
        JFXButton cancelButton = new JFXButton("Cancelar");
        JFXButton addButton = new JFXButton("Adicionar");
        quantityTextField.setPromptText("Quantidade de produtos");
        cancelButton.setOnAction(event -> dialog.close());
        addButton.setOnAction(event -> {
            try{
                if(!quantityTextField.getText().isEmpty()) {
                    int quantity = Integer.parseInt(quantityTextField.getText());
                    boolean founded = false;
                    for (ProductCart productCart : FarmApple.dataManager.getCartManager().getSellList()) {
                        if (productCart.getProduto().getLote().equals(produto.getLote())) {
                            if(Integer.parseInt(quantityTextField.getText()) > 0){
                                if(Integer.parseInt(quantityTextField.getText()) <= 50){
                                    FarmApple.dataManager.getCartManager().editProductQuantity(produto, quantity);
                                    showDialog(pane, "Quantidade Alterada!", "O produto j\u00e1 estava no carrinho!\nA quantidade foi alterada para o valor inserido.");
                                    founded = true;
                                }else{
                                    FarmDialogs.showDialog(pane, "Erro", "N\u00e3o possuimos estoque!");
                                }
                            }else{
                                FarmDialogs.showDialog(pane, "Erro", "N\u00e3o \u00e9 possivel adicionar uma quantidade inferior a 1!");
                            }
                            break;
                        }
                    }
                    if (!founded) {
                        if(Integer.parseInt(quantityTextField.getText()) > 0) {
                            if(Integer.parseInt(quantityTextField.getText()) <= 50){
                                FarmApple.dataManager.getCartManager().addProductToCart(produto, quantity);
                                showDialog(pane, "Adicionado ao carrinho!", "Produto adicionado com sucesso!");
                            }else{
                                FarmDialogs.showDialog(pane, "Erro", "N\u00e3o possuimos estoque!");
                            }
                        }else{
                            FarmDialogs.showDialog(pane, "Erro", "N\u00e3o \u00e9 possivel adicionar uma quantidade inferior a 1!");
                        }
                    }
                }else{
                    showDialog(pane, "Campos vazios!", "Existem campos obrigatorios que est\u00e3o vazios");
                }
            }catch (NumberFormatException e){
                showDialog(pane, "Erro", "O campos de quantidade s\u00f3 aceita n\u00fameros!");
            }
            dialog.close();
        });
        content.setStyle("-fx-background-color: #2f3136;");
        addButton.setStyle("-fx-text-fill: white;");
        cancelButton.setStyle("-fx-text-fill: white;");
        quantityTextField.setStyle("-fx-text-fill: white;-fx-prompt-text-fill: white;-fx-background-color: transparent");
        content.setActions(quantityTextField, addButton, cancelButton);
        dialog.show();
    }

    public static void showCartRemoveDialog(StackPane pane, ListView<ProductCart> listView, ProductCart productCart, Text totalPrice){
        JFXDialogLayout content = new JFXDialogLayout();
        String produtoNome = productCart.getProduto().getNome();
        if(produtoNome.length() > 20){
            produtoNome = produtoNome.substring(0,20) + "...";
        }
        Text head = new Text("Confirma\u00e7\u00e3o de exclus\u00e3o");
        Text body = new Text("Deseja realmente deletar o produto de nome "+ produtoNome +"?");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM);
        JFXButton noButton = new JFXButton("N\u00e3o");
        JFXButton yesButton = new JFXButton("Sim");
        noButton.setOnAction(event -> dialog.close());
        yesButton.setOnAction(event -> {
            try{
                listView.getItems().remove(productCart);
                FarmApple.dataManager.getCartManager().removeProductOfCart(productCart.getProduto());
                totalPrice.setText("R$ " + String.format("%.2f", FarmApple.dataManager.getCartManager().getTotalPrice()).replace(".", ","));
            }catch (Exception e){
                FarmApple.logger.error("Error ao remover o produto do carrinho!", e);
            }
            dialog.close();
        });
        content.setStyle("-fx-background-color: #2f3136;");
        noButton.setStyle("-fx-text-fill: white;");
        yesButton.setStyle("-fx-text-fill: white;");
        content.setActions(yesButton, noButton);
        dialog.show();
    }

    public static void showCartEditDialog(StackPane pane, ListView<ProductCart> listView,ProductCart productCart, Text totalPrice){
        JFXDialogLayout content = new JFXDialogLayout();
        Text head = new Text("Edi\u00e7\u00e3o de Quantidade");
        Text body = new Text("Digite a quantidade desejada.");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXTextField quantityTextField = new JFXTextField();
        JFXButton cancelButton = new JFXButton("Cancelar");
        JFXButton updateButton = new JFXButton("Atualizar");
        quantityTextField.setPromptText("Quantidade de produtos");
        cancelButton.setOnAction(event -> dialog.close());
        updateButton.setOnAction(event -> {
            try{
                if(!quantityTextField.getText().isEmpty()) {
                    int quantity = Integer.parseInt(quantityTextField.getText());
                    if(quantity > 0){
                        if(quantity <= 50){
                            FarmApple.dataManager.getCartManager().editProductQuantity(productCart.getProduto(), quantity);
                            listView.refresh();
                            totalPrice.setText("R$ " + String.format("%.2f", FarmApple.dataManager.getCartManager().getTotalPrice()).replace(".", ","));
                            showDialog(pane, "Quantidade alterada!", "A quantidade foi alterada com sucesso!");
                        }else{
                            FarmDialogs.showDialog(pane, "Erro", "N\u00e3o possuimos estoque!");
                        }
                    }else{
                        FarmDialogs.showDialog(pane, "Erro", "N\u00e3o \u00e9 possivel adicionar uma quantidade inferior a 1!");
                    }
                }else{
                    showDialog(pane, "Campos vazios!", "Existem campos obrigatorios que est\u00e3o vazios");
                }
            }catch (NumberFormatException e){
                showDialog(pane, "Erro", "O campos de quantidade s\u00f3 aceita n\u00fameros!");
            }
            dialog.close();
        });
        content.setStyle("-fx-background-color: #2f3136;");
        cancelButton.setStyle("-fx-text-fill: white;");
        updateButton.setStyle("-fx-text-fill: white;");
        quantityTextField.setStyle("-fx-text-fill: white;-fx-prompt-text-fill: white;-fx-background-color: transparent");
        content.setActions(quantityTextField, updateButton, cancelButton);
        dialog.show();
    }

    public static void showFinishSellDialog(StackPane pane, ListView<ProductCart> listView, Text totalPrice){
        JFXDialogLayout content = new JFXDialogLayout();
        Text head = new Text("Edi\u00e7\u00e3o de Quantidade");
        Text body = new Text("Digite a quantidade desejada.");
        head.setFill(Color.color(1,1,1));
        body.setFill(Color.color(1,1,1));
        content.setHeading(head);
        content.setBody(body);
        JFXDialog dialog = new JFXDialog(pane, content, JFXDialog.DialogTransition.BOTTOM );
        JFXTextField cpfTextField = new JFXTextField();
        JFXTextField nfTextField = new JFXTextField();
        JFXButton cancelButton = new JFXButton("Cancelar");
        JFXButton updateButton = new JFXButton("Finalizar venda");
        cpfTextField.setPromptText("Cpf do Cliente");
        nfTextField.setPromptText("Nota Fiscal");
        cancelButton.setOnAction(event -> dialog.close());
        updateButton.setOnAction(event -> {
            try{
                if(!cpfTextField.getText().isEmpty() && !nfTextField.getText().isEmpty()){
                    boolean founded = false;
                    for(Cliente cliente : FarmApple.dataManager.getCostumerManager().getClienteList()){
                        if(cliente.getCpf().equals(cpfTextField.getText())){
                            int notaFiscal = Integer.parseInt(nfTextField.getText());
                            List<Object> nfList = SQLRunner.ExecuteSQLScript.SQLSelect("VerifyNF", notaFiscal);
                            if(nfList == null || nfList.isEmpty()) {
                                Vendas venda = new Vendas();
                                venda.setNf(notaFiscal);
                                venda.setData(Timestamp.from(Instant.now()));
                                venda.setCrf(FarmApple.dataManager.getFarmManager().getFarmaceutico().getCrf());
                                venda.setCpf(cliente.getCpf());
                                SQLRunner.ExecuteSQLScript.SQLSet("FinishSell", notaFiscal, FarmApple.dataManager.getFarmManager().getFarmaceutico().getCrf(), cliente.getCpf());
                                FarmApple.dataManager.getSellManager().addVenda(venda);

                                for (ProductCart productCart : FarmApple.dataManager.getCartManager().getSellList()) {
                                    SQLRunner.ExecuteSQLScript.SQLSet("RegisterProductSell", productCart.getProduto().getLote(), productCart.getQuantity(), notaFiscal);
                                }

                                FarmApple.dataManager.getCartManager().clearSellList();
                                showDialog(pane, "Venda finalizada!", "A venda foi finalizada com sucesso!");
                            }else{
                                showDialog(pane, "Erro", "Nota fiscal j\u00e1 registrada!");
                            }
                            founded = true;

                            break;
                        }
                    }
                    if(!founded){
                        showDialog(pane, "Cliente n\u00e3o encontrado!", "O CPF informado n\u00e3o foi encontrado na lista de clientes!\nRealize o cadastro e finalize tente novamente.");
                    }

                    listView.refresh();
                    totalPrice.setText("R$ " + String.format("%.2f", FarmApple.dataManager.getCartManager().getTotalPrice()).replace(".", ","));
                }else{
                    showDialog(pane, "Campos vazios!", "Existem campos obrigatorios que est\u00e3o vazios");
                }
            }catch (NumberFormatException e){
                showDialog(pane, "Erro", "O campos de quantidade e nota fiscal s\u00f3 aceita n\u00fameros!");
            }
            dialog.close();
        });
        content.setStyle("-fx-background-color: #2f3136;");
        cancelButton.setStyle("-fx-text-fill: white;");
        updateButton.setStyle("-fx-text-fill: white;");
        nfTextField.setStyle("-fx-text-fill: white;-fx-prompt-text-fill: white;-fx-background-color: transparent");
        cpfTextField.setStyle("-fx-text-fill: white;-fx-prompt-text-fill: white;-fx-background-color: transparent");
        content.setActions(nfTextField,cpfTextField, updateButton, cancelButton);
        dialog.show();
    }
}
