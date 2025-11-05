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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;

import Controller.JogoController;
import Model.Jogador;

public class JanelaJogoPrincipal extends JFrame {
    private List<Jogador> jogadores;
    private Jogador jogadorAtual;
    private JogoController controller;
    private JPanel painelPrincipal;
    private JPanel painelEsquerdo;
    
    private JLabel labelAcertadas;
    private JLabel labelRespondidas;
    private JLabel labelSequencia;
    
    private static java.util.Map<String, EstatisticasJogador> estatisticasPorJogador = new java.util.HashMap<>();
    
    public JanelaJogoPrincipal(List<Jogador> jogadores, JogoController controller) {
        this.jogadores = jogadores;
        this.controller = controller;
        this.jogadorAtual = controller.getJogadorAtual();
        this.controller.setView(this);
        
        for (Jogador jogador : jogadores) {
            if (!estatisticasPorJogador.containsKey(jogador.getNome())) {
                estatisticasPorJogador.put(jogador.getNome(), new EstatisticasJogador());
            }
        }
        
        setTitle("Universidade de SI - RPG");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        setLocationRelativeTo(null);
        
        carregarLayoutCompleto();
        setVisible(true);
    }
    
    private void carregarLayoutCompleto() {
        painelPrincipal = new JPanel(new BorderLayout());
        
        painelEsquerdo = criarPainelInformacoesJogador();
        JPanel painelCentro = criarPainelMapaComMundos();
        
        painelPrincipal.add(painelEsquerdo, BorderLayout.WEST);
        painelPrincipal.add(painelCentro, BorderLayout.CENTER);
        
        getContentPane().removeAll();
        add(painelPrincipal);
        revalidate();
        repaint();
    }
    
    public void atualizarJogadorAtual(Jogador novoJogador) {
        this.jogadorAtual = novoJogador;
        System.out.println("Atualizando para jogador: " + novoJogador.getNome());
        carregarLayoutCompleto();
    }
    
    public void atualizarEstatisticas(boolean acertou, int novaSequencia) {
        if (jogadorAtual == null) return;
        
        EstatisticasJogador stats = estatisticasPorJogador.get(jogadorAtual.getNome());
        if (stats == null) {
            stats = new EstatisticasJogador();
            estatisticasPorJogador.put(jogadorAtual.getNome(), stats);
        }
        
        stats.perguntasRespondidas++;
        if (acertou) {
            stats.perguntasAcertadas++;
        }
        stats.sequenciaAcertos = novaSequencia;
        
        atualizarLabelsEstatisticas();
        
        System.out.println("Estatisticas atualizadas - " + jogadorAtual.getNome() + 
                          ": Acertadas: " + stats.perguntasAcertadas + 
                          ", Respondidas: " + stats.perguntasRespondidas + 
                          ", Sequencia: " + stats.sequenciaAcertos);
    }
    
    private void atualizarLabelsEstatisticas() {
        if (jogadorAtual == null) return;
        
        EstatisticasJogador stats = estatisticasPorJogador.get(jogadorAtual.getNome());
        if (stats == null) return;
        
        if (labelAcertadas != null && labelRespondidas != null && labelSequencia != null) {
            labelAcertadas.setText(String.valueOf(stats.perguntasAcertadas));
            labelRespondidas.setText(String.valueOf(stats.perguntasRespondidas));
            labelSequencia.setText(String.valueOf(stats.sequenciaAcertos));
            
            labelAcertadas.revalidate();
            labelAcertadas.repaint();
            labelRespondidas.revalidate();
            labelRespondidas.repaint();
            labelSequencia.revalidate();
            labelSequencia.repaint();
        }
    }
    
    public void avancarParaProximoJogador() {
        controller.avancarJogador();
        Jogador novoJogador = controller.getJogadorAtual();
        
        if (novoJogador != null) {
            mostrarJanelaProximoJogador(novoJogador);
        }
    }
    
    private void mostrarJanelaProximoJogador(Jogador proximoJogador) {
        JFrame janelaProximo = new JFrame();
        janelaProximo.setTitle("Vez do Proximo Jogador");
        janelaProximo.setSize(500, 350);
        janelaProximo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaProximo.setLocationRelativeTo(this);
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
        painelFundo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("VEZ DO PROXIMO JOGADOR", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        JLabel lblJogador = new JLabel(proximoJogador.getNome(), SwingConstants.CENTER);
        lblJogador.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblJogador.setForeground(new Color(100, 200, 255));
        lblJogador.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        String especializacao = (proximoJogador.getPersonagem() != null) ? 
            proximoJogador.getPersonagem().getNome() : "Sem especializacao";
        JLabel lblEspecializacao = new JLabel(especializacao, SwingConstants.CENTER);
        lblEspecializacao.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        lblEspecializacao.setForeground(new Color(200, 200, 200));
        
        JButton btnContinuar = new JButton("CONTINUAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color cor = getModel().isRollover() ? new Color(100, 150, 255) : new Color(70, 130, 180);
                g2d.setColor(cor);
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
        btnContinuar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setPreferredSize(new Dimension(150, 45));
        btnContinuar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnContinuar.addActionListener(e -> janelaProximo.dispose());
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(lblTitulo, BorderLayout.NORTH);
        painelCentral.add(lblJogador, BorderLayout.CENTER);
        painelCentral.add(lblEspecializacao, BorderLayout.SOUTH);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        painelBotoes.add(btnContinuar);
        
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janelaProximo.add(painelFundo);
        janelaProximo.setVisible(true);
    }
    
    private JPanel criarPainelInformacoesJogador() {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(9, 1, 10, 10));
        painel.setBackground(new Color(20, 25, 60));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        painel.setPreferredSize(new Dimension(320, 0));
        
        JLabel titulo = new JLabel("JOGADOR ATUAL", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(255, 215, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        painel.add(titulo);
        
        if (jogadorAtual != null) {
            EstatisticasJogador stats = estatisticasPorJogador.get(jogadorAtual.getNome());
            if (stats == null) {
                stats = new EstatisticasJogador();
                estatisticasPorJogador.put(jogadorAtual.getNome(), stats);
            }
            
            JPanel cardNome = criarCardInformacao("NOME", jogadorAtual.getNome(), "");
            painel.add(cardNome);
            
            String nomeClasse = jogadorAtual.getPersonagem() != null ? 
                jogadorAtual.getPersonagem().getNome() : "Nao escolhida";
            JPanel cardClasse = criarCardInformacao("ESPECIALIZACAO", nomeClasse, "");
            painel.add(cardClasse);
            
            JPanel cardPontuacao = criarCardInformacao("PONTUACAO", String.valueOf(jogadorAtual.getPontos()), "pontos");
            painel.add(cardPontuacao);
            
            int usosHabilidade = jogadorAtual.getPersonagem() != null ? 
                jogadorAtual.getPersonagem().getUsosHabilidade() : 0;
            JPanel cardUsos = criarCardInformacao("USOS DE HABILIDADE", String.valueOf(usosHabilidade), "disponiveis");
            painel.add(cardUsos);
            
            JPanel cardAcertadas = criarCardInformacaoDinamica("PERGUNTAS ACERTADAS", String.valueOf(stats.perguntasAcertadas));
            painel.add(cardAcertadas);
            
            JPanel cardRespondidas = criarCardInformacaoDinamica("PERGUNTAS RESPONDIDAS", String.valueOf(stats.perguntasRespondidas));
            painel.add(cardRespondidas);
            
            JPanel cardSequencia = criarCardInformacaoDinamica("SEQUENCIA DE ACERTOS", String.valueOf(stats.sequenciaAcertos));
            painel.add(cardSequencia);
            
            String habilidade = jogadorAtual.getPersonagem() != null ? 
                jogadorAtual.getPersonagem().getHabilidade() : "Nenhuma";
            JPanel cardHabilidade = criarCardHabilidadeEspecial(habilidade);
            painel.add(cardHabilidade);
        }
        
        return painel;
    }
    
    private JPanel criarCardHabilidadeEspecial(String habilidade) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(40, 45, 80));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
            BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));
        card.setPreferredSize(new Dimension(280, 80));
        
        JLabel labelTitulo = new JLabel("HABILIDADE ESPECIAL", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelTitulo.setForeground(new Color(100, 200, 255));
        
        JLabel labelValor = new JLabel("<html><div style='text-align: center; font-size: 11px;'>" + habilidade + "</div></html>", SwingConstants.CENTER);
        labelValor.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        labelValor.setForeground(Color.WHITE);
        
        card.add(labelTitulo, BorderLayout.NORTH);
        card.add(labelValor, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarCardInformacao(String titulo, String valor, String subtitulo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(40, 45, 80));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
            BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));
        
        JLabel labelTitulo = new JLabel(titulo, SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelTitulo.setForeground(new Color(100, 200, 255));
        
        JLabel labelValor = new JLabel(valor, SwingConstants.CENTER);
        labelValor.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelValor.setForeground(Color.WHITE);
        
        JLabel labelSubtitulo = new JLabel(subtitulo, SwingConstants.CENTER);
        labelSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        labelSubtitulo.setForeground(new Color(200, 200, 200));
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(labelValor, BorderLayout.CENTER);
        painelCentral.add(labelSubtitulo, BorderLayout.SOUTH);
        
        card.add(labelTitulo, BorderLayout.NORTH);
        card.add(painelCentral, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarCardInformacaoDinamica(String titulo, String valorInicial) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(40, 45, 80));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
            BorderFactory.createEmptyBorder(12, 10, 12, 10)
        ));
        
        JLabel labelTitulo = new JLabel(titulo, SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelTitulo.setForeground(new Color(100, 200, 255));
        
        JLabel labelValor = new JLabel(valorInicial, SwingConstants.CENTER);
        labelValor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelValor.setForeground(new Color(255, 215, 0));
        
        if (titulo.equals("PERGUNTAS ACERTADAS")) {
            labelAcertadas = labelValor;
        } else if (titulo.equals("PERGUNTAS RESPONDIDAS")) {
            labelRespondidas = labelValor;
        } else if (titulo.equals("SEQUENCIA DE ACERTOS")) {
            labelSequencia = labelValor;
        }
        
        card.add(labelTitulo, BorderLayout.NORTH);
        card.add(labelValor, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarPainelMapaComMundos() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.BLACK);
        
        try {
            ImageIcon imagemMapa = new ImageIcon(getClass().getResource("/fundo/fundo rpg.jpg"));
            JLabel labelMapa = new JLabel(imagemMapa, SwingConstants.CENTER);
            
            JPanel painelMundos = criarOverlayMundos();
            painelMundos.setOpaque(false);
            
            JPanel painelRanking = criarBotaoRanking();
            painelRanking.setOpaque(false);
            
            painel.setLayout(new OverlayLayout(painel));
            painel.add(painelRanking);
            painel.add(painelMundos);
            painel.add(labelMapa);
            
        } catch (Exception e) {
            System.out.println("Erro ao carregar mapa: " + e.getMessage());
            JLabel labelErro = new JLabel("MAPA DO RPG - UNIVERSIDADE SI", SwingConstants.CENTER);
            labelErro.setFont(new Font("Segoe UI", Font.BOLD, 24));
            labelErro.setForeground(Color.WHITE);
            labelErro.setBackground(Color.DARK_GRAY);
            labelErro.setOpaque(true);
            painel.add(labelErro, BorderLayout.CENTER);
            
            JPanel painelMundos = criarOverlayMundos();
            painel.add(painelMundos, BorderLayout.CENTER);
        }
        
        return painel;
    }
    
    private JPanel criarOverlayMundos() {
        JPanel painel = new JPanel(null);
        painel.setOpaque(false);
        painel.setPreferredSize(new Dimension(1600, 896));
        
        MundoInfo[] mundos = {
            new MundoInfo(230, 150, 274, 195, "Banco de Dados", "BANCO DE DADOS", 150, 150, 150, 150),
            new MundoInfo(160, 630, 300, 220, "HTML e JavaScript", "HTML E JAVASCRIPT", 150, 510, 650, 600),
            new MundoInfo(1050, 130, 309, 256, "Java", "JAVA", 1300, 250, 1250, 200),
            new MundoInfo(1140, 480, 411, 319, "Linguagem C", "LINGUAGEM C", 1100, 550, 1050, 500),
            new MundoInfo(600, 270, 403, 375, "Rede de Computadores", "REDE DE COMPUTADORES", 750, 450, 700, 400)
        };
        
        for (MundoInfo mundo : mundos) {
            JPanel botaoInvisivel = criarBotaoInvisivel(mundo);
            painel.add(botaoInvisivel);
            
            JLabel labelNome = criarLabelNomeMundo(mundo);
            painel.add(labelNome);
        }
        
        return painel;
    }
    
    private JPanel criarBotaoInvisivel(MundoInfo mundo) {
        JPanel panel = new JPanel();
        panel.setBounds(mundo.botaoX, mundo.botaoY, mundo.botaoLargura, mundo.botaoAltura);
        panel.setOpaque(false);
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirJanelaPergunta(mundo.nome);
            }
        });
        
        panel.setToolTipText("Clique para acessar " + mundo.nome);
        
        return panel;
    }
    
    private JLabel criarLabelNomeMundo(MundoInfo mundo) {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setOpaque(false);
        
        if (mundo.nome.equals("Rede de Computadores")) {
            label.setText("REDE DE COMPUTADORES");
            label.setBounds(mundo.mundoX - 180, mundo.mundoY - 140, 180, 30);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
        } else if (mundo.nome.equals("HTML e JavaScript")) {
            label.setText("HTML E JAVASCRIPT");
            label.setBounds(mundo.mundoX - 4, mundo.mundoY + 30, 200, 30);
            label.setHorizontalAlignment(SwingConstants.LEFT);
        } else {
            label.setText(mundo.nomeDisplay);
            label.setBounds(mundo.mundoX, mundo.mundoY - 40, 200, 30);
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        return label;
    }
    
    private JPanel criarBotaoRanking() {
        JPanel painelRanking = new JPanel(null);
        painelRanking.setOpaque(false);
        painelRanking.setPreferredSize(new Dimension(1600, 896));
        
        JPanel btnRanking = new JPanel(new BorderLayout());
        btnRanking.setBounds(1400, 820, 150, 60);
        btnRanking.setBackground(new Color(70, 130, 180));
        btnRanking.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel labelRanking = new JLabel("RANKING", SwingConstants.CENTER);
        labelRanking.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelRanking.setForeground(Color.WHITE);
        
        btnRanking.add(labelRanking, BorderLayout.CENTER);
        
        btnRanking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirJanelaRanking();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRanking.setBackground(new Color(100, 150, 255));
                btnRanking.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnRanking.setBackground(new Color(70, 130, 180));
                btnRanking.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255), 2));
            }
        });
        
        painelRanking.add(btnRanking);
        return painelRanking;
    }
    
    private void abrirJanelaPergunta(String nomeMundo) {
        System.out.println("Mundo clicado: " + nomeMundo);
        System.out.println("Jogador atual: " + jogadorAtual.getNome());
        
        JanelaSelecaoPergunta janelaSelecao = new JanelaSelecaoPergunta(nomeMundo, jogadorAtual, controller, this);
        janelaSelecao.mostrar();
    }
    
    private void abrirJanelaRanking() {
        System.out.println("Abrindo ranking dos jogadores");
        
        JanelaRanking janelaRanking = new JanelaRanking(jogadores);
        janelaRanking.mostrar();
    }
    
    private static class EstatisticasJogador {
        int perguntasAcertadas = 0;
        int perguntasRespondidas = 0;
        int sequenciaAcertos = 0;
    }
    
    private class MundoInfo {
        int botaoX, botaoY, botaoLargura, botaoAltura;
        String nome;
        String nomeDisplay;
        int mundoX, mundoY;
        int tituloX, tituloY;
        
        MundoInfo(int botaoX, int botaoY, int botaoLargura, int botaoAltura, 
                  String nome, String nomeDisplay, int mundoX, int mundoY, int tituloX, int tituloY) {
            this.botaoX = botaoX;
            this.botaoY = botaoY;
            this.botaoLargura = botaoLargura;
            this.botaoAltura = botaoAltura;
            this.nome = nome;
            this.nomeDisplay = nomeDisplay;
            this.mundoX = mundoX;
            this.mundoY = mundoY;
            this.tituloX = tituloX;
            this.tituloY = tituloY;
        }
    }
}