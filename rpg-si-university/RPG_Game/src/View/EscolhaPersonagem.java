package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Controller.Jogo;
import Controller.JogoController;
import Model.CyberSecurity;
import Model.DataScientist;
import Model.DevOps;
import Model.FullStackDeveloper;
import Model.Jogador;
import Model.Personagem;

public class EscolhaPersonagem {
    private JFrame janela;
    private List<Jogador> jogadores;
    private JogoController controller;
    private int jogadorAtual;
    private int personagemAtual;
    
    private Personagem[] personagens;
    
    private JLabel labelNome;
    private JLabel labelHabilidade;
    private JLabel labelUsos;
    private JLabel labelDescricao;
    private JLabel labelImagem;
    private JLabel labelJogadorAtual;
    private JButton btnSelecionar;
    private JPanel painelInfo;
    private JPanel containerInfo;
    
    public EscolhaPersonagem(List<Jogador> jogadores, JogoController controller) {
        this.jogadores = jogadores;
        this.controller = controller;
        this.jogadorAtual = 0;
        this.personagemAtual = 0;
        this.personagens = new Personagem[]{
            new FullStackDeveloper(),
            new DataScientist(), 
            new CyberSecurity(),
            new DevOps()
        };
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        janela = new JFrame();
        janela.setTitle("Universidade SI - Escolha de Especializacao");
        janela.setSize(1200, 900);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        janela.setLayout(new BorderLayout());
        janela.setUndecorated(false);
        
        criarComponentes();
        configurarAcoes();
        organizarLayout();
        atualizarInterface();
    }
    
    private void criarComponentes() {
        labelNome = new JLabel();
        labelNome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        labelNome.setForeground(new Color(255, 215, 0));
        
        labelHabilidade = new JLabel();
        labelHabilidade.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelHabilidade.setForeground(new Color(100, 200, 255));
        
        labelUsos = new JLabel();
        labelUsos.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        labelUsos.setForeground(new Color(200, 200, 200));
        
        labelDescricao = new JLabel();
        labelDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        labelDescricao.setForeground(new Color(220, 220, 220));
        
        labelImagem = new JLabel("", SwingConstants.CENTER);
        labelImagem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 4),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        labelImagem.setBackground(new Color(30, 30, 60));
        labelImagem.setOpaque(true);
        labelImagem.setPreferredSize(new Dimension(400, 400));
        
        labelJogadorAtual = new JLabel();
        labelJogadorAtual.setFont(new Font("Segoe UI", Font.BOLD, 24));
        labelJogadorAtual.setForeground(new Color(100, 255, 100));
        
        btnSelecionar = criarBotaoEstilizado("SELECIONAR ESPECIALIZACAO", 
                                           new Color(70, 130, 180), 
                                           new Color(100, 150, 255));
        
        painelInfo = new JPanel();
        painelInfo.setLayout(new GridLayout(4, 1, 20, 20));
        painelInfo.setBackground(new Color(40, 45, 80));
        painelInfo.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        containerInfo = new JPanel(new BorderLayout());
        containerInfo.setBackground(new Color(40, 45, 80));
        containerInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 3),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
    }
    
    private JButton criarBotaoEstilizado(String texto, Color corBase, Color corHover) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color corAtual = getModel().isRollover() ? corHover : corBase;
                g2d.setColor(corAtual);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
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
        
        botao.setFont(new Font("Segoe UI", Font.BOLD, 18));
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(320, 60));
        botao.setBorder(BorderFactory.createEmptyBorder(18, 35, 18, 35));
        
        return botao;
    }
    
    private void configurarAcoes() {
        btnSelecionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecionarPersonagem();
            }
        });
    }
    
    private void organizarLayout() {
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
        
        JLabel titulo = new JLabel("ESCOLHA SUA ESPECIALIZACAO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(new Color(255, 215, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));
        
        JPanel painelJogadorInfo = new JPanel();
        painelJogadorInfo.setOpaque(false);
        painelJogadorInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        painelJogadorInfo.add(labelJogadorAtual);
        
        JPanel painelPersonagem = new JPanel();
        painelPersonagem.setLayout(new BorderLayout(40, 40));
        painelPersonagem.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        painelPersonagem.setOpaque(false);
        
        JPanel painelImagem = new JPanel(new BorderLayout());
        painelImagem.setOpaque(false);
        painelImagem.add(labelImagem, BorderLayout.CENTER);
        
        painelInfo.removeAll();
        painelInfo.add(labelNome);
        painelInfo.add(labelHabilidade);
        painelInfo.add(labelUsos);
        painelInfo.add(labelDescricao);
        
        containerInfo.removeAll();
        containerInfo.add(painelInfo, BorderLayout.CENTER);
        
        painelPersonagem.add(painelImagem, BorderLayout.WEST);
        painelPersonagem.add(containerInfo, BorderLayout.CENTER);
        
        JPanel painelNavegacao = criarPainelNavegacao();
        
        painelFundo.add(titulo, BorderLayout.NORTH);
        painelFundo.add(painelJogadorInfo, BorderLayout.CENTER);
        painelFundo.add(painelPersonagem, BorderLayout.CENTER);
        painelFundo.add(painelNavegacao, BorderLayout.SOUTH);
        
        janela.add(painelFundo);
    }
    
    private JPanel criarPainelNavegacao() {
        JPanel painelNavegacao = new JPanel();
        painelNavegacao.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));
        painelNavegacao.setOpaque(false);
        painelNavegacao.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        JButton btnAnterior = criarBotaoEstilizado("◀ ANTERIOR", 
                                                 new Color(100, 100, 120), 
                                                 new Color(130, 130, 150));
        
        JButton btnProximo = criarBotaoEstilizado("PROXIMO ▶", 
                                                new Color(100, 100, 120), 
                                                new Color(130, 130, 150));
        
        btnAnterior.addActionListener(e -> {
            personagemAtual = (personagemAtual - 1 + personagens.length) % personagens.length;
            atualizarInterface();
        });
        
        btnProximo.addActionListener(e -> {
            personagemAtual = (personagemAtual + 1) % personagens.length;
            atualizarInterface();
        });
        
        painelNavegacao.add(btnAnterior);
        painelNavegacao.add(btnSelecionar);
        painelNavegacao.add(btnProximo);
        
        return painelNavegacao;
    }
    
    private void atualizarInterface() {
        Personagem personagem = personagens[personagemAtual];
        
        labelNome.setText(personagem.getNome());
        labelHabilidade.setText("Habilidade: " + personagem.getHabilidade());
        labelUsos.setText("Usos de Habilidade: " + personagem.getUsosHabilidade() + " por jogo");
        labelDescricao.setText("<html><div style='text-align: center; width: 450px; color: #DCDCDC; font-size: 16px;'>" + 
                              personagem.getDescricaoHabilidade() + "</div></html>");
        
        if (jogadorAtual < jogadores.size()) {
            labelJogadorAtual.setText("Jogador: " + jogadores.get(jogadorAtual).getNome() + 
                                    " | Turno: " + (jogadorAtual + 1) + "/" + jogadores.size());
        }
        
        String caminho = "/images/personagens/" + getNomeArquivo(personagem.getImage());
        
        try {
            java.net.URL imageURL = getClass().getResource(caminho);
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                Image scaledImage = icon.getImage().getScaledInstance(380, 380, Image.SCALE_SMOOTH);
                labelImagem.setIcon(new ImageIcon(scaledImage));
                labelImagem.setText("");
            } else {
                labelImagem.setIcon(null);
                labelImagem.setText("<html><div style='text-align: center; color: #FF6B6B; font-size: 16px;'>" +
                                   "Imagem nao encontrada<br>" +
                                   "<small>" + caminho + "</small></div></html>");
            }
        } catch (Exception e) {
            labelImagem.setIcon(null);
            labelImagem.setText("<html><div style='text-align: center; color: #FF6B6B; font-size: 16px;'>" +
                               "Erro ao carregar imagem<br>" +
                               "<small>" + e.getMessage() + "</small></div></html>");
        }
        
        painelInfo.revalidate();
        painelInfo.repaint();
        containerInfo.revalidate();
        containerInfo.repaint();
    }

    private String getNomeArquivo(String caminho) {
        if (caminho == null || caminho.isEmpty()) {
            return "default.png";
        }
        return caminho.substring(caminho.lastIndexOf("/") + 1);
    }
    
    private void selecionarPersonagem() {
        if (jogadorAtual < jogadores.size()) {
            Jogador jogador = jogadores.get(jogadorAtual);
            Personagem personagemEscolhido = personagens[personagemAtual];
            
            // Cria uma nova instância do personagem escolhido
            Personagem novoPersonagem = criarPersonagemPorTipo(personagemEscolhido);
            jogador.setPersonagem(novoPersonagem);
            
            System.out.println("Jogador " + jogador.getNome() + " escolheu: " + 
                              jogador.getPersonagem().getNome() + " - Usos: " + 
                              jogador.getPersonagem().getUsosHabilidade());
            
            jogadorAtual++;
            
            if (jogadorAtual < jogadores.size()) {
                mostrarJanelaProximoJogador();
            } else {
                finalizarEscolha();
            }
        }
    }
    
    private Personagem criarPersonagemPorTipo(Personagem personagem) {
        if (personagem instanceof FullStackDeveloper) {
            return new FullStackDeveloper();
        } else if (personagem instanceof DataScientist) {
            return new DataScientist();
        } else if (personagem instanceof CyberSecurity) {
            return new CyberSecurity();
        } else if (personagem instanceof DevOps) {
            return new DevOps();
        }
        return null;
    }
    
    private void mostrarJanelaProximoJogador() {
        JFrame janelaProximo = new JFrame();
        janelaProximo.setTitle("Proximo Jogador");
        janelaProximo.setSize(600, 400);
        janelaProximo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaProximo.setLocationRelativeTo(janela);
        janelaProximo.setLayout(new BorderLayout());
        janelaProximo.setUndecorated(true);
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 60, 90), 0, getHeight(), new Color(15, 30, 45));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(255, 215, 0));
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel lblIcone = new JLabel("✓", SwingConstants.CENTER);
        lblIcone.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        lblIcone.setForeground(new Color(100, 255, 100));
        lblIcone.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel lblTitulo = new JLabel("ESPECIALIZACAO SELECIONADA!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        JLabel lblJogador = new JLabel("Agora e a vez de:", SwingConstants.CENTER);
        lblJogador.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJogador.setForeground(new Color(100, 200, 255));
        lblJogador.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        JLabel lblNomeJogador = new JLabel(jogadores.get(jogadorAtual).getNome(), SwingConstants.CENTER);
        lblNomeJogador.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblNomeJogador.setForeground(new Color(100, 255, 100));
        
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
        btnContinuar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setPreferredSize(new Dimension(180, 55));
        btnContinuar.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btnContinuar.addActionListener(e -> {
            janelaProximo.dispose();
            atualizarInterface();
        });
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(lblTitulo, BorderLayout.NORTH);
        painelCentral.add(lblJogador, BorderLayout.CENTER);
        painelCentral.add(lblNomeJogador, BorderLayout.SOUTH);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        painelBotoes.add(btnContinuar);
        
        painelFundo.add(lblIcone, BorderLayout.NORTH);
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janelaProximo.add(painelFundo);
        janelaProximo.setVisible(true);
    }
    
    private void finalizarEscolha() {
        janela.dispose();
        mostrarJanelaInicioJornada();
    }

    private void mostrarJanelaInicioJornada() {
        JFrame janelaInicio = new JFrame();
        janelaInicio.setTitle("Jornada Iniciada!");
        janelaInicio.setSize(700, 550);
        janelaInicio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaInicio.setLocationRelativeTo(null);
        janelaInicio.setLayout(new BorderLayout());
        janelaInicio.setUndecorated(true);
        
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
                g2d.setStroke(new java.awt.BasicStroke(4));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 25, 25);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel lblIcone = new JLabel("✓", SwingConstants.CENTER);
        lblIcone.setFont(new Font("Segoe UI", Font.PLAIN, 100));
        lblIcone.setForeground(new Color(100, 255, 100));
        lblIcone.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JLabel lblTitulo = new JLabel("ESPECIALIZACOES CONCLUIDAS!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        JLabel lblSubtitulo = new JLabel("Ordem dos Jogadores e Especializacoes:", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblSubtitulo.setForeground(new Color(100, 200, 255));
        lblSubtitulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        
        JLabel lblJogadores = new JLabel(getResumoJogadores(), SwingConstants.CENTER);
        lblJogadores.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblJogadores.setForeground(Color.WHITE);
        lblJogadores.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));
        
        JLabel lblMensagem = new JLabel("Preparem-se para a aventura na Universidade SI!", SwingConstants.CENTER);
        lblMensagem.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblMensagem.setForeground(new Color(200, 200, 200));
        
        JButton btnIniciar = new JButton("INICIAR JOGO") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color cor = getModel().isRollover() ? new Color(100, 200, 100) : new Color(70, 180, 70);
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
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setPreferredSize(new Dimension(220, 65));
        btnIniciar.setBorder(BorderFactory.createEmptyBorder(18, 35, 18, 35));
        btnIniciar.addActionListener(e -> {
            janelaInicio.dispose();
            abrirJanelaJogoPrincipal();
        });
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(lblTitulo, BorderLayout.NORTH);
        painelCentral.add(lblSubtitulo, BorderLayout.CENTER);
        painelCentral.add(lblJogadores, BorderLayout.SOUTH);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        painelBotoes.add(btnIniciar);
        
        painelFundo.add(lblIcone, BorderLayout.NORTH);
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(lblMensagem, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janelaInicio.add(painelFundo);
        janelaInicio.setVisible(true);
    }
    
    private void abrirJanelaJogoPrincipal() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Jogo.iniciarJogoPrincipal();
            }
        });
    }
    
    private String getResumoJogadores() {
        StringBuilder resumo = new StringBuilder("<html><div style='text-align: center; font-size: 18px;'>");
        for (int i = 0; i < jogadores.size(); i++) {
            Jogador jogador = jogadores.get(i);
            String nomePersonagem = (jogador.getPersonagem() != null) ? 
                jogador.getPersonagem().getNome() : "Sem especialização";
            
            resumo.append("<b style='color: #FFD700;'>").append(i + 1).append("º </b>")
                  .append("<span style='color: #4ECDC4;'>").append(jogador.getNome()).append("</span>")
                  .append(" - <span style='color: #FF6B6B;'>").append(nomePersonagem).append("</span>")
                  .append("<br>");
        }
        resumo.append("</div></html>");
        return resumo.toString();
    }
    
    public void mostrar() {
        atualizarInterface();
        janela.setVisible(true);
    }
}