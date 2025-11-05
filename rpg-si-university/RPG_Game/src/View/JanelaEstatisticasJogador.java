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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Model.Jogador;

public class JanelaEstatisticasJogador extends JFrame {
    private Jogador jogador;
    
    public JanelaEstatisticasJogador(Jogador jogador) {
        this.jogador = jogador;
        inicializarInterface();
    }
    
    private void inicializarInterface() {
        setTitle("Estatísticas - " + jogador.getNome());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);
        
        carregarInterface();
    }
    
    private void carregarInterface() {
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 45, 80), 0, getHeight(), new Color(60, 65, 100));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(100, 150, 255));
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("ESTATÍSTICAS DO JOGADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(255, 215, 0));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel lblNome = new JLabel(jogador.getNome(), SwingConstants.CENTER);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNome.setForeground(new Color(100, 200, 255));
        lblNome.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JPanel painelEstatisticas = criarPainelEstatisticas();
        JPanel painelBotoes = criarPainelBotoes();
        
        painelFundo.add(lblTitulo, BorderLayout.NORTH);
        painelFundo.add(lblNome, BorderLayout.CENTER);
        painelFundo.add(painelEstatisticas, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelFundo);
    }
    
    private JPanel criarPainelEstatisticas() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 15, 15));
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        String especializacao = jogador.getPersonagem() != null ? 
            jogador.getPersonagem().getNome() : "Nenhuma";
        String habilidade = jogador.getPersonagem() != null ? 
            jogador.getPersonagem().getHabilidade() : "Nenhuma";
        int usosRestantes = jogador.getPersonagem() != null ? 
            jogador.getPersonagem().getUsosHabilidade() : 0;
        
        adicionarCardEstatistica(painel, "PONTUAÇÃO TOTAL", String.valueOf(jogador.getPontos()), "🏆");
        adicionarCardEstatistica(painel, "ESPECIALIZAÇÃO", especializacao, "⚡");
        adicionarCardEstatistica(painel, "HABILIDADE", habilidade, "🎯");
        adicionarCardEstatistica(painel, "USOS RESTANTES", String.valueOf(usosRestantes), "🔢");
        
        return painel;
    }
    
    private void adicionarCardEstatistica(JPanel painel, String titulo, String valor, String icone) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255, 30));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblIcone = new JLabel(icone, SwingConstants.CENTER);
        lblIcone.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblIcone.setForeground(new Color(255, 215, 0));
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(new Color(200, 200, 255));
        
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblValor.setForeground(Color.WHITE);
        
        JPanel painelConteudo = new JPanel(new BorderLayout());
        painelConteudo.setOpaque(false);
        painelConteudo.add(lblIcone, BorderLayout.NORTH);
        painelConteudo.add(lblTitulo, BorderLayout.CENTER);
        painelConteudo.add(lblValor, BorderLayout.SOUTH);
        
        card.add(painelConteudo, BorderLayout.CENTER);
        painel.add(card);
    }
    
    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel();
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton btnVoltar = criarBotaoEstilizado("VOLTAR", new Color(70, 130, 180), new Color(100, 150, 255));
        
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        painel.add(btnVoltar);
        
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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
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
        
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(120, 40));
        botao.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        return botao;
    }
    
    public void mostrar() {
        setVisible(true);
    }
}