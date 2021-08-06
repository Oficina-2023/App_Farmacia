package br.com.apoo2021.farm.database;

import br.com.apoo2021.farm.Farmaple;
import br.com.apoo2021.farm.util.IDBConnection;
import org.apache.commons.dbcp2.BasicDataSource;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLRunner {

    public static class ExecuteSQLScript {

        //Executa Scripts SQL que não nescessitam de retorno
        public static void SQLSet(@NotNull String scriptName,Object... args){
            try{
                Connection connection = SQLConnection.SQLConnect();
                String sqlScript = readScriptContent(scriptName);
                PreparedStatement statement = connection.prepareStatement(sqlScript);
                if(args != null){
                    if(args.length > 0){
                        int dataPos = 0;
                        for(Object obj : args){
                            dataPos++;
                            if(obj instanceof String){
                                statement.setString(dataPos, (String) obj);
                            }else if(obj instanceof Boolean){
                                statement.setBoolean(dataPos, (Boolean) obj);
                            }else if(obj instanceof Integer){
                                statement.setInt(dataPos, (Integer) obj);
                            }else if(obj instanceof Long){
                                statement.setLong(dataPos, (Long) obj);
                            }else if(obj instanceof Float){
                                statement.setFloat(dataPos, (Float) obj);
                            }else if(obj instanceof Date){
                                statement.setDate(dataPos, (java.sql.Date) obj);
                            }else{
                                Farmaple.logger.error("Tipo de dado não declarado na execução do script sql!");
                            }
                        }
                    }
                }
                statement.executeUpdate();
                statement.close();
            }catch (SQLException e){
                Farmaple.logger.error("Erro ao executar um script sql 'set'! - Script: " + scriptName, e);
            }
        }

        //Executa Scripts SQL que nescessitam de um retorno entregando eles em um array de object
        public static List<Object> SQLSelect(@NotNull String scriptName,Object... args){
            try{
                Connection connection = SQLConnection.SQLConnect();
                String sqlScript = readScriptContent(scriptName);
                PreparedStatement statement = connection.prepareStatement(sqlScript);
                List<Object> resultList = new ArrayList<>();
                if(args != null){
                    if(args.length > 0){
                        int dataPos = 0;
                        for(Object obj : args){
                            dataPos++;
                            if(obj instanceof String){
                                statement.setString(dataPos, (String) obj);
                            }else if(obj instanceof Boolean){
                                statement.setBoolean(dataPos, (Boolean) obj);
                            }else if(obj instanceof Integer){
                                statement.setInt(dataPos, (Integer) obj);
                            }else if(obj instanceof Long){
                                statement.setLong(dataPos, (Long) obj);
                            }else if(obj instanceof Float){
                                statement.setFloat(dataPos, (Float) obj);
                            }else if(obj instanceof Date){
                                statement.setDate(dataPos, (java.sql.Date) obj);
                            }else{
                                Farmaple.logger.error("Tipo de dado não declarado na execução do script sql!");
                            }
                        }
                    }
                }
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    resultList.add(resultSet.getObject(sqlScript.split(" ", 3)[1]));
                }
                statement.close();
                return resultList;
            }catch (SQLException e){
                Farmaple.logger.error("Erro ao executar um script sql 'get'! - Script: " + scriptName, e);
            }
            return null;
        }
    }

    private static class SQLConnection implements IDBConnection {
        private static final BasicDataSource dataSource = new BasicDataSource();

        static{
            try{
                dataSource.setUrl(address);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                dataSource.setMinIdle(5);
                dataSource.setMaxIdle(100);
                dataSource.setMaxTotal(1000);
            }catch (Exception e){
                Farmaple.logger.error("Erro ao se conectar no banco de dados!", e);
            }
        }

        public static Connection SQLConnect() throws SQLException {
            return dataSource.getConnection();
        }
    }


    private static String readScriptContent(String scriptName) throws SQLException {
        StringBuilder sqlScriptBuilder = new StringBuilder();
        try{
            InputStream stream = SQLRunner.class.getClassLoader().getResourceAsStream("scripts/" + scriptName + ".sql");
            if(stream != null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()){
                    sqlScriptBuilder.append(line);
                }
                bufferedReader.close();
            }
        }catch (Exception e){
            Farmaple.logger.error("Erro ao ler o arquivo sql! - Script: " + scriptName, e);
            throw new SQLException();
        }
        return sqlScriptBuilder.toString();
    }

}
