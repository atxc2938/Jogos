package DAO;

import java.sql.Connection;
import java.sql.Statement;

public class CriarEstrutura {
    public static void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS pergunta (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "mundo VARCHAR(50) NOT NULL, " +
                    "texto TEXT NOT NULL, " +
                    "opcao_a VARCHAR(255) NOT NULL, " +
                    "opcao_b VARCHAR(255) NOT NULL, " +
                    "opcao_c VARCHAR(255) NOT NULL, " +
                    "opcao_d VARCHAR(255) NOT NULL, " +
                    "resposta_correta INT NOT NULL" +
                    ")";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("Tabela pergunta criada ou já existente");
            
        } catch (Exception e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }
}