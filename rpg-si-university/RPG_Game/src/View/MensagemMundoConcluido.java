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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MensagemMundoConcluido {
    private String nomeMundo;
    private ActionListener onContinuar;
    
    // CORREÇÃO: Construtor com ActionListener
    public MensagemMundoConcluido(String nomeMundo, ActionListener onContinuar) {
        this.nomeMundo = nomeMundo;
        this.onContinuar = onContinuar;
        mostrar();
    }
    
    private void mostrar() {
        JFrame janela = new JFrame();
        janela.setTitle("Mundo Concluido");
        janela.setSize(500, 400);
        janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janela.setLocationRelativeTo(null); // CORREÇÃO: Centralizar a janela
        janela.setLayout(new BorderLayout());
        janela.setUndecorated(true);
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(60, 60, 100), 0, getHeight(), new Color(30, 30, 60));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(150, 150, 255));
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblIcone = new JLabel("✓", SwingConstants.CENTER);
        lblIcone.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        lblIcone.setForeground(new Color(100, 255, 100));
        lblIcone.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel lblTitulo = new JLabel("MUNDO CONCLUIDO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        JLabel lblMensagem = new JLabel("<html><div style='text-align: center;'>Todas as perguntas do mundo<br><b>" + nomeMundo.toUpperCase() + "</b><br>ja foram respondidas!</div></html>", SwingConstants.CENTER);
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
                janela.dispose();
                if (onContinuar != null) {
                    onContinuar.actionPerformed(e);
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
        
        painelFundo.add(lblIcone, BorderLayout.NORTH);
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janela.add(painelFundo);
        janela.setVisible(true);
    }
}