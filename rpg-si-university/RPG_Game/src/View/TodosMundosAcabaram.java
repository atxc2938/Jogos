package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Controller.JogoController;
import Model.Jogador;

public class TodosMundosAcabaram extends JFrame {
    
    public TodosMundosAcabaram() {
        setTitle("Fim do Jogo");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true);
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 80, 120), 0, getHeight(), new Color(20, 40, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(255, 215, 0));
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblTitulo = new JLabel("TODOS OS MUNDOS ACABARAM", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        JLabel lblMensagem = new JLabel("<html><div style='text-align: center;'>Parabens! Todas as perguntas de todos os mundos foram respondidas!</div></html>", SwingConstants.CENTER);
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMensagem.setForeground(Color.WHITE);
        lblMensagem.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        JButton btnContinuar = new JButton("CONTINUAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color cor = getModel().isRollover() ? new Color(100, 150, 255) : new Color(70, 130, 180);
                g2d.setColor(cor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
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
        btnContinuar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setPreferredSize(new Dimension(150, 45));
        btnContinuar.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btnContinuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                java.awt.Window[] windows = java.awt.Window.getWindows();
                for (java.awt.Window window : windows) {
                    window.dispose();
                }
                if (JogoController.getInstance() != null) {
                    List<Jogador> jogadores = JogoController.getInstance().getJogadores();
                    new JanelaFimDeJogo(jogadores).mostrar();
                } else {
                    new JanelaFimDeJogo(new ArrayList<Jogador>()).mostrar();
                }
            }
        });
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(lblTitulo, BorderLayout.NORTH);
        painelCentral.add(lblMensagem, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        painelBotoes.add(btnContinuar);
        
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelFundo);
    }
    
    public void mostrar() {
        setVisible(true);
    }
}