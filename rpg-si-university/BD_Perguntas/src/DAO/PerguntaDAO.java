package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PerguntaDAO {
    public static List<Pergunta> getPerguntasPorMundo(String mundo) {
        List<Pergunta> perguntas = new ArrayList<>();
        String sql = "SELECT * FROM pergunta WHERE mundo = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, mundo);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String[] opcoes = {
                    rs.getString("opcao_a"),
                    rs.getString("opcao_b"), 
                    rs.getString("opcao_c"),
                    rs.getString("opcao_d")
                };
                
                Pergunta pergunta = new Pergunta(
                    rs.getString("texto"),
                    opcoes,
                    rs.getInt("resposta_correta")
                );
                perguntas.add(pergunta);
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar perguntas: " + e.getMessage());
        }
        
        return perguntas;
    }
    
    public static class Pergunta {
        private String texto;
        private String[] opcoes;
        private int respostaCorreta;
        
        public Pergunta(String texto, String[] opcoes, int respostaCorreta) {
            this.texto = texto;
            this.opcoes = opcoes;
            this.respostaCorreta = respostaCorreta;
        }
        
        public String getTexto() { return texto; }
        public String[] getOpcoes() { return opcoes; }
        public int getRespostaCorreta() { return respostaCorreta; }
    }
}