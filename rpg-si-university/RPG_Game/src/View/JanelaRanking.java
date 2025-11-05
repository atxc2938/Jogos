package View;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import Model.Jogador;

public class JanelaRanking extends JFrame {
    private List<Jogador> jogadores;
    
    public JanelaRanking(List<Jogador> jogadores) {
        this.jogadores = jogadores;
        
        setTitle("Ranking dos Jogadores");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        carregarRanking();
    }
    
    private void carregarRanking() {
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 25, 60), 0, getHeight(), new Color(10, 15, 40));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JLabel titulo = new JLabel("RANKING DOS JOGADORES", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(255, 215, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        Collections.sort(jogadores, new Comparator<Jogador>() {
            @Override
            public int compare(Jogador j1, Jogador j2) {
                return Integer.compare(j2.getPontos(), j1.getPontos());
            }
        });
        
        JPanel painelJogadores = new JPanel();
        painelJogadores.setLayout(new GridLayout(jogadores.size(), 1, 10, 10));
        painelJogadores.setOpaque(false);
        
        for (int i = 0; i < jogadores.size(); i++) {
            Jogador jogador = jogadores.get(i);
            JPanel cardJogador = criarCardJogador(jogador, i + 1);
            painelJogadores.add(cardJogador);
        }
        
        JScrollPane scrollPane = new JScrollPane(painelJogadores);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        
        painelFundo.add(titulo, BorderLayout.NORTH);
        painelFundo.add(scrollPane, BorderLayout.CENTER);
        
        add(painelFundo);
    }
    
    private JPanel criarCardJogador(Jogador jogador, int posicao) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(getCorFundoPosicao(posicao));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getCorBordaPosicao(posicao), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel labelPosicao = new JLabel(String.valueOf(posicao), SwingConstants.CENTER);
        labelPosicao.setFont(new Font("Segoe UI", Font.BOLD, 24));
        labelPosicao.setForeground(getCorTextoPosicao(posicao));
        labelPosicao.setPreferredSize(new Dimension(50, 50));
        
        JPanel painelInfo = new JPanel(new GridLayout(3, 1, 5, 5));
        painelInfo.setOpaque(false);
        
        JLabel labelNome = new JLabel(jogador.getNome(), SwingConstants.LEFT);
        labelNome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelNome.setForeground(Color.WHITE);
        
        String classe = jogador.getPersonagem() != null ? 
            jogador.getPersonagem().getNome() : "Sem especialização";
        JLabel labelClasse = new JLabel(classe, SwingConstants.LEFT);
        labelClasse.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelClasse.setForeground(new Color(200, 200, 200));
        
        JLabel labelPontos = new JLabel(jogador.getPontos() + " pontos", SwingConstants.LEFT);
        labelPontos.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelPontos.setForeground(new Color(255, 215, 0));
        
        painelInfo.add(labelNome);
        painelInfo.add(labelClasse);
        painelInfo.add(labelPontos);
        
        JLabel labelMedalha = new JLabel(getSimboloPosicao(posicao), SwingConstants.CENTER);
        labelMedalha.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelMedalha.setForeground(getCorMedalhaPosicao(posicao));
        labelMedalha.setPreferredSize(new Dimension(40, 40));
        
        card.add(labelPosicao, BorderLayout.WEST);
        card.add(painelInfo, BorderLayout.CENTER);
        card.add(labelMedalha, BorderLayout.EAST);
        
        return card;
    }
    
    private Color getCorFundoPosicao(int posicao) {
        switch (posicao) {
            case 1: return new Color(70, 50, 120); 
            case 2: return new Color(60, 70, 100);
            case 3: return new Color(80, 60, 70);   
            default: return new Color(50, 55, 80); 
        }
    }
    
    private Color getCorBordaPosicao(int posicao) {
        switch (posicao) {
            case 1: return new Color(255, 215, 0);  
            case 2: return new Color(192, 192, 192);
            case 3: return new Color(205, 127, 50); 
            default: return new Color(100, 150, 255); 
        }
    }
    
    private Color getCorTextoPosicao(int posicao) {
        switch (posicao) {
            case 1: return new Color(255, 215, 0);   
            case 2: return new Color(192, 192, 192); 
            case 3: return new Color(205, 127, 50); 
            default: return new Color(100, 200, 255); 
        }
    }
    
    private Color getCorMedalhaPosicao(int posicao) {
        switch (posicao) {
            case 1: return new Color(255, 215, 0);  
            case 2: return new Color(192, 192, 192);
            case 3: return new Color(205, 127, 50);  
            default: return new Color(100, 150, 255);
        }
    }
    
    private String getSimboloPosicao(int posicao) {
        switch (posicao) {
            case 1: return "●";
            case 2: return "♦";
            case 3: return "●";
            default: return "♦";
        }
    }
    
    public void mostrar() {
        setVisible(true);
    }
}