package lk.ijse.dep10.sas.db;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;
    private DBConnection(){
        try {
            File configFile =new File("application.properties");
            Properties properties = new Properties();
            FileReader fr=new FileReader(configFile);
            properties.load(fr);
            fr.close();

            
            String host = properties.getProperty("mysql.host","localhost");
            String port = properties.getProperty("mysql.port","3306");
            String database = properties.getProperty("mysql.database","dep10_sas");
            String username = properties.getProperty("mysql.username","root");
            String password = properties.getProperty("mysql.password","");

            connection= DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+
                    "?createDatabaseIfNotExist=true&allowMultiQueries=true"+
                    ","+username+","+password);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"configuration file doesn't exist").showAndWait();
            throw new RuntimeException(e);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,"failed to read configurations").showAndWait();
            throw new RuntimeException(e);
        }
    }
    public static DBConnection getInstance(){
        return (dbConnection==null)?dbConnection=new DBConnection():dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }
}
