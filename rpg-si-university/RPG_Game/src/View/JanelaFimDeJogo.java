package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Model.Jogador;

public class JanelaFimDeJogo extends JFrame {
    private List<Jogador> jogadores;
    
    public JanelaFimDeJogo(List<Jogador> jogadores) {
        this.jogadores = jogadores;
        ordenarJogadoresPorPontuacao();
        inicializarInterface();
    }
    
    private void ordenarJogadoresPorPontuacao() {
        if (jogadores != null) {
            Collections.sort(jogadores, new Comparator<Jogador>() {
                @Override
                public int compare(Jogador j1, Jogador j2) {
                    return Integer.compare(j2.getPontos(), j1.getPontos());
                }
            });
        }
    }
    
    private void inicializarInterface() {
        setTitle("Fim do Jogo - Ranking Final");
        // AUMENTEI APENAS A JANELA: 1200x800 (mais largura para os nomes)
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(false);
        
        carregarInterface();
    }
    
    private void carregarInterface() {
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 15, 40), 0, getHeight(), new Color(20, 30, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        // MANTIVE O MESMO ESPAÇAMENTO INTERNO
        painelFundo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("RANKING FINAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36)); // MANTIVE A FONTE ORIGINAL
        lblTitulo.setForeground(new Color(255, 215, 0));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); // MANTIVE O ESPAÇAMENTO
        
        JPanel painelRanking = criarPainelRanking();
        JPanel painelBotoes = criarPainelBotoes();
        
        painelFundo.add(lblTitulo, BorderLayout.NORTH);
        painelFundo.add(painelRanking, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelFundo);
    }
    
    private JPanel criarPainelRanking() {
        // MANTIVE O GRID ORIGINAL
        JPanel painel = new JPanel(new GridLayout(jogadores != null ? jogadores.size() : 1, 1, 15, 15));
        painel.setOpaque(false);
        // AUMENTEI AS BORDAS LATERAIS para dar mais espaço para os nomes
        painel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        if (jogadores != null && !jogadores.isEmpty()) {
            for (int i = 0; i < jogadores.size(); i++) {
                Jogador jogador = jogadores.get(i);
                JPanel cardJogador = criarCardJogador(jogador, i);
                painel.add(cardJogador);
            }
        } else {
            // Mensagem quando não há jogadores
            JLabel lblSemJogadores = new JLabel("Nenhum jogador encontrado", SwingConstants.CENTER);
            lblSemJogadores.setFont(new Font("Segoe UI", Font.BOLD, 20)); // MANTIVE A FONTE ORIGINAL
            lblSemJogadores.setForeground(Color.WHITE);
            painel.add(lblSemJogadores);
        }
        
        return painel;
    }
    
    private JPanel criarCardJogador(Jogador jogador, int posicao) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(getCorCard(posicao));
        // MANTIVE O TAMANHO ORIGINAL DOS CARDS
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getCorBorda(posicao), 3), // MANTIVE A BORDA ORIGINAL
            BorderFactory.createEmptyBorder(20, 25, 20, 25) // MANTIVE O ESPAÇAMENTO INTERNO ORIGINAL
        ));
        
        JLabel lblPosicao = new JLabel((posicao + 1) + "º LUGAR", SwingConstants.LEFT);
        lblPosicao.setFont(new Font("Segoe UI", Font.BOLD, 18)); // MANTIVE A FONTE ORIGINAL
        lblPosicao.setForeground(getCorTexto(posicao));
        
        // CORREÇÃO: Ajustei o nome para quebrar linha se necessário
        String nomeJogador = jogador.getNome();
        JLabel lblNome = new JLabel("<html><div style='text-align: center; width: 300px;'>" + nomeJogador + "</div></html>", SwingConstants.CENTER);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 22)); // MANTIVE A FONTE ORIGINAL
        lblNome.setForeground(getCorTexto(posicao));
        
        JLabel lblPontos = new JLabel(jogador.getPontos() + " PONTOS", SwingConstants.RIGHT);
        lblPontos.setFont(new Font("Segoe UI", Font.BOLD, 20)); // MANTIVE A FONTE ORIGINAL
        lblPontos.setForeground(getCorTexto(posicao));
        
        String especializacao = jogador.getPersonagem() != null ? 
            jogador.getPersonagem().getNome() : "Sem especialização";
        JLabel lblEspecializacao = new JLabel(especializacao, SwingConstants.CENTER);
        lblEspecializacao.setFont(new Font("Segoe UI", Font.ITALIC, 14)); // MANTIVE A FONTE ORIGINAL
        lblEspecializacao.setForeground(getCorTexto(posicao));
        
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.setOpaque(false);
        painelSuperior.add(lblPosicao, BorderLayout.WEST);
        painelSuperior.add(lblPontos, BorderLayout.EAST);
        
        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setOpaque(false);
        painelInferior.add(lblNome, BorderLayout.CENTER);
        painelInferior.add(lblEspecializacao, BorderLayout.SOUTH);
        
        card.add(painelSuperior, BorderLayout.NORTH);
        card.add(painelInferior, BorderLayout.CENTER);
        
        return card;
    }
    
    private Color getCorCard(int posicao) {
        switch (posicao) {
            case 0: return new Color(255, 215, 0, 150); // MANTIVE AS CORES ORIGINAIS
            case 1: return new Color(192, 192, 192, 150);
            case 2: return new Color(205, 127, 50, 150);
            default: return new Color(70, 130, 180, 150);
        }
    }
    
    private Color getCorBorda(int posicao) {
        switch (posicao) {
            case 0: return new Color(255, 215, 0); // MANTIVE AS CORES ORIGINAIS
            case 1: return new Color(192, 192, 192);
            case 2: return new Color(205, 127, 50);
            default: return new Color(100, 150, 255);
        }
    }
    
    private Color getCorTexto(int posicao) {
        switch (posicao) {
            case 0: return new Color(255, 215, 0); // MANTIVE AS CORES ORIGINAIS
            case 1: return new Color(220, 220, 220);
            case 2: return new Color(255, 165, 0);
            default: return Color.WHITE;
        }
    }
    
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        // MANTIVE O ESPAÇAMENTO ORIGINAL
        painel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JButton btnReiniciar = criarBotaoEstilizado("NOVO JOGO", new Color(70, 180, 70), new Color(100, 220, 100));
        JButton btnSair = criarBotaoEstilizado("SAIR", new Color(220, 80, 80), new Color(255, 100, 100));
        
        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarJogo();
            }
        });
        
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        painel.add(btnReiniciar);
        painel.add(btnSair);
        
        return painel;
    }
    
    private JButton criarBotaoEstilizado(String texto, Color corBase, Color corHover) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color corAtual = getModel().isRollover() ? corHover : corBase;
                g2d.setColor(corAtual);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // MANTIVE O BORDER RADIUS ORIGINAL
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                javax.swing.plaf.basic.BasicGraphicsUtils.drawStringUnderlineCharAt(g2d, getText(), -1, 
                    getWidth()/2 - g2d.getFontMetrics().stringWidth(getText())/2, 
                    getHeight()/2 + g2d.getFontMetrics().getAscent()/2 - 2);
            }
            
            @Override
            public void setContentAreaFilled(boolean b) {
                super.setContentAreaFilled(false);
            }
        };
        
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16)); // MANTIVE A FONTE ORIGINAL
        botao.setForeground(Color.WHITE);
        // MANTIVE O TAMANHO ORIGINAL DOS BOTÕES
        botao.setPreferredSize(new Dimension(180, 50));
        // MANTIVE O ESPAÇAMENTO INTERNO ORIGINAL
        botao.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        return botao;
    }
    
    private void reiniciarJogo() {
        dispose();
        Controller.Jogo.iniciarSorteio();
    }
    
    public void mostrar() {
        setVisible(true);
    }
}