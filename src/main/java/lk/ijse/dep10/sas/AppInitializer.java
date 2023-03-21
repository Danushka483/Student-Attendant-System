package lk.ijse.dep10.sas;

import javafx.application.Application;
import javafx.stage.Stage;
import lk.ijse.dep10.sas.db.DBConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{

            try {
                if(DBConnection.getInstance().getConnection() !=null && !DBConnection.getInstance().getConnection().isClosed()){
                    System.out.println("Database connection is about to close");
                    DBConnection.getInstance().getConnection().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        generateSchemaIfNotExist();
    }
    public void generateSchemaIfNotExist(){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm1 = connection.createStatement();
            Statement stm2 = connection.createStatement();
            HashSet<String> tableNameList = new HashSet<>();
            ResultSet rst = stm1.executeQuery("SHOW TABLES");

            while (rst.next()){
                tableNameList.add(rst.getString(1));
            }
            boolean tableExist = tableNameList.containsAll(Set.of("Attendance","Student","Picture","User"));

            if(!tableExist){
                InputStream is = getClass().getResourceAsStream("/shema.sql");
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder dbScript = new StringBuilder();
                while ((line=bf.readLine()) != null){
                    dbScript.append(line).append("\n");
                }

            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
