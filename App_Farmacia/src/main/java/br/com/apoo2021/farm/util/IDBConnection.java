package br.com.apoo2021.farm.util;

import java.util.ResourceBundle;

public interface IDBConnection {

    // Carregamento do bundle dos dados
    ResourceBundle dbConnection = ResourceBundle.getBundle("DBConnection");

    //Dados do SQL
    String address = dbConnection.getString("farm.sql.address");
    String username = dbConnection.getString("farm.sql.username");
    String password = dbConnection.getString("farm.sql.password");

}
