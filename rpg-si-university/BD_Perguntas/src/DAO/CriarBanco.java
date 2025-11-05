package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CriarBanco {
    public static void criarBanco() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS perguntasRPG");
            System.out.println("Banco de dados perguntasRPG criado ou já existente");
            
        } catch (Exception e) {
            System.out.println("Erro ao criar banco: " + e.getMessage());
        }
    }
}