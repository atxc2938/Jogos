package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/perguntasRPG";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    static {
        // Carrega o driver explicitamente
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ Driver MySQL carregado com sucesso");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERRO: Driver MySQL não encontrado!");
            System.err.println("   Certifique-se de que o MySQL Connector/J está no classpath");
            throw new RuntimeException("Driver MySQL não encontrado", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}