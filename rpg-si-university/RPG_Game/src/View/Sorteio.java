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
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Controller.Jogo;
import Model.Jogador;

public class Sorteio {
    private JFrame janela;
    private List<Jogador> jogadores;
    private int jogadorAtual;
    private Random random;
    
    private JLabel labelInstrucoes;
    private JLabel labelResultado;
    private JButton botaoSortear;
    private JButton botaoDesempate;
    private JButton botaoProsseguir;
    private JTextField campoNome;
    private JPanel painelNome;
    
    private List<Jogador> jogadoresEmpatados;
    private List<Jogador> jogadoresNaoEmpatados;
    private boolean temEmpate = false;
    private JFrame janelaDesempate;
    
    private List<JButton> botoesSortearDesempate;
    private JButton botaoFinalizarDesempate;
    
    public Sorteio() {
        this.jogadores = new ArrayList<>();
        this.jogadorAtual = 0;
        this.random = new Random();
        this.jogadoresEmpatados = new ArrayList<>();
        this.jogadoresNaoEmpatados = new ArrayList<>();
        this.botoesSortearDesempate = new ArrayList<>();
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        janela = new JFrame();
        janela.setTitle("RPG Universidade SI - Sorteio Inicial");
        janela.setSize(800, 700);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);
        janela.setLayout(new BorderLayout());
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 30, 60), 0, getHeight(), new Color(20, 50, 100));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        labelInstrucoes = new JLabel(
            "<html><div style='text-align: center; color: #E8C547;'>" +
            "<font size='6'><b>TORNEIO DA ORDEM</b></font><br><br>" +
            "<font size='5'>Cada jogador sorteara um numero de 1 a 10</font><br>" +
            "<font size='4'>(Maior numero joga primeiro!)</font><br><br>" +
            "<font size='5' color='#4ECDC4'>Jogador 1 - Digite seu nome:</font>" +
            "</div></html>", SwingConstants.CENTER);
        labelInstrucoes.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelInstrucoes.setBorder(BorderFactory.createEmptyBorder(50, 30, 40, 30));
        
        JPanel painelCentral = new JPanel(new BorderLayout(15, 15));
        painelCentral.setOpaque(false);
        painelCentral.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 80));
        
        painelNome = new JPanel(new FlowLayout());
        painelNome.setOpaque(false);
        
        JLabel labelNome = new JLabel("Nome do Jogador:");
        labelNome.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelNome.setForeground(new Color(200, 220, 255));
        
        campoNome = new JTextField(30);
        campoNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campoNome.setBackground(new Color(30, 40, 70));
        campoNome.setForeground(Color.WHITE);
        campoNome.setCaretColor(Color.WHITE);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 3),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        painelNome.add(labelNome);
        painelNome.add(campoNome);
        
        labelResultado = new JLabel("Aguardando sorteio...", SwingConstants.CENTER);
        labelResultado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        labelResultado.setForeground(new Color(200, 255, 200));
        labelResultado.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(40, 0, 50, 0));
        
        botaoSortear = criarBotaoEstilizado("Lancar Dado - Jogador " + (jogadorAtual + 1), 
                                           new Color(65, 105, 225), new Color(30, 144, 255));
        
        botaoDesempate = criarBotaoEstilizado("", new Color(255, 140, 0), new Color(255, 165, 0));
        botaoDesempate.setVisible(false);
        
        botaoProsseguir = criarBotaoEstilizado("Iniciar Jornada", new Color(50, 205, 50), new Color(60, 220, 60));
        botaoProsseguir.setVisible(false);
        
        painelBotoes.add(botaoSortear);
        painelBotoes.add(botaoDesempate);
        painelBotoes.add(botaoProsseguir);
        
        painelCentral.add(painelNome, BorderLayout.NORTH);
        painelCentral.add(labelResultado, BorderLayout.CENTER);
        painelCentral.add(painelBotoes, BorderLayout.SOUTH);
        
        painelFundo.add(labelInstrucoes, BorderLayout.NORTH);
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        
        janela.add(painelFundo);
        configurarAcoes();
    }
    
    private JButton criarBotaoEstilizado(String texto, Color corBase, Color corHover) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color corAtual = getModel().isRollover() ? corHover : corBase;
                g2d.setColor(corAtual);
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
        
        botao.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(280, 60));
        botao.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        return botao;
    }
    
    private void configurarAcoes() {
        botaoSortear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortearJogador();
            }
        });
        
        botaoDesempate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarDesempate();
            }
        });
        
        botaoProsseguir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prosseguirParaPersonagens();
            }
        });
    }
    
    private void sortearJogador() {
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            mostrarJanelaCampoVazio();
            return;
        }
        
        int numeroSorteado = random.nextInt(10) + 1;
        Jogador jogador = new Jogador(nome, numeroSorteado);
        jogadores.add(jogador);
        
        labelResultado.setText("<html><div style='text-align: center; color: #4ECDC4;'>" +
                              "<b>" + jogador.getNome() + "</b> sorteou: <font size='7' color='#FFD700'>" + 
                              numeroSorteado + "</font></div></html>");
        campoNome.setText("");
        jogadorAtual++;
        
        if (jogadorAtual < 4) {
            atualizarInterfaceProximoJogador();
        } else {
            finalizarSorteio();
        }
    }
    
    private void mostrarJanelaCampoVazio() {
        JFrame janelaAlerta = new JFrame();
        janelaAlerta.setTitle("Atencao");
        janelaAlerta.setSize(500, 300);
        janelaAlerta.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaAlerta.setLocationRelativeTo(janela);
        janelaAlerta.setLayout(new BorderLayout());
        janelaAlerta.setUndecorated(true);
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(100, 30, 30), 0, getHeight(), new Color(150, 50, 50));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(255, 100, 100));
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblIcone = new JLabel("⚠", SwingConstants.CENTER);
        lblIcone.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        lblIcone.setForeground(new Color(255, 255, 100));
        lblIcone.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel lblTitulo = new JLabel("CAMPO OBRIGATORIO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(255, 200, 100));
        
        JLabel lblMensagem = new JLabel("<html><div style='text-align: center;'>O campo de nome nao pode ficar em branco!<br>Digite um nome para continuar.</div></html>", SwingConstants.CENTER);
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMensagem.setForeground(Color.WHITE);
        lblMensagem.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnOk = criarBotaoEstilizado("ENTENDIDO", new Color(220, 80, 80), new Color(255, 100, 100));
        btnOk.addActionListener(e -> janelaAlerta.dispose());
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(lblTitulo, BorderLayout.NORTH);
        painelCentral.add(lblMensagem, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        painelBotoes.add(btnOk);
        
        painelFundo.add(lblIcone, BorderLayout.NORTH);
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janelaAlerta.add(painelFundo);
        janelaAlerta.setVisible(true);
    }
    
    private void atualizarInterfaceProximoJogador() {
        labelInstrucoes.setText(
            "<html><div style='text-align: center; color: #E8C547;'>" +
            "<font size='6'><b>TORNEIO DA ORDEM</b></font><br><br>" +
            "<font size='5'>Cada jogador sorteara um numero de 1 a 10</font><br>" +
            "<font size='4'>(Maior numero joga primeiro!)</font><br><br>" +
            "<font size='5' color='#4ECDC4'>Jogador " + (jogadorAtual + 1) + " - Digite seu nome:</font>" +
            "</div></html>");
        botaoSortear.setText("Lancar Dado - Jogador " + (jogadorAtual + 1));
    }
    
    private void finalizarSorteio() {
        painelNome.setVisible(false);
        campoNome.setVisible(false);
        botaoSortear.setVisible(false);
        
        jogadores.sort((j1, j2) -> Integer.compare(j2.getNumeroSorteado(), j1.getNumeroSorteado()));
        verificarEmpates();
        
        if (temEmpate) {
            configurarInterfaceDesempate();
        } else {
            configurarInterfaceSemEmpate();
        }
    }
    
    private void verificarEmpates() {
        jogadoresEmpatados.clear();
        jogadoresNaoEmpatados.clear();
        
        Map<Integer, List<Jogador>> grupos = new HashMap<>();
        for (Jogador jogador : jogadores) {
            grupos.computeIfAbsent(jogador.getNumeroSorteado(), k -> new ArrayList<>()).add(jogador);
        }
        
        for (Jogador jogador : jogadores) {
            List<Jogador> grupo = grupos.get(jogador.getNumeroSorteado());
            if (grupo.size() > 1) {
                boolean jaExiste = false;
                for (Jogador existente : jogadoresEmpatados) {
                    if (existente.getNome().equals(jogador.getNome())) {
                        jaExiste = true;
                        break;
                    }
                }
                if (!jaExiste) {
                    jogadoresEmpatados.add(jogador);
                }
            } else {
                jogadoresNaoEmpatados.add(jogador);
            }
        }
        
        temEmpate = !jogadoresEmpatados.isEmpty();
    }
    
    private void configurarInterfaceDesempate() {
        StringBuilder nomesEmpatados = new StringBuilder();
        for (int i = 0; i < jogadoresEmpatados.size(); i++) {
            if (i > 0) nomesEmpatados.append(" e ");
            nomesEmpatados.append(jogadoresEmpatados.get(i).getNome())
                         .append(" (").append(jogadoresEmpatados.get(i).getNumeroSorteado()).append(")");
        }
        
        StringBuilder nomesSeguros = new StringBuilder();
        for (Jogador jogador : jogadoresNaoEmpatados) {
            if (nomesSeguros.length() > 0) nomesSeguros.append(", ");
            nomesSeguros.append(jogador.getNome()).append(" (").append(jogador.getNumeroSorteado()).append(")");
        }
        
        if (nomesSeguros.length() == 0) {
            nomesSeguros.append("Nenhum jogador com numero unico");
        }
        
        botaoDesempate.setText("Desempatar: " + nomesEmpatados.toString());
        botaoDesempate.setVisible(true);
        
        labelInstrucoes.setText(
            "<html><div style='text-align: center; color: #E8C547;'>" +
            "<font size='6'><b>DESEMPATE NECESSARIO!</b></font><br><br>" +
            "<font size='5' color='#4ECDC4'><b>Posicoes garantidas:</b> " + nomesSeguros + "</font><br>" +
            "<font size='5' color='#FF6B6B'><b>Desempate entre:</b> " + nomesEmpatados + "</font><br>" +
            "<font size='4'>Clique para iniciar o desempate</font>" +
            "</div></html>");
        
        labelResultado.setText("Empate detectado! Inicie o desempate");
    }
    
    private void configurarInterfaceSemEmpate() {
        botaoProsseguir.setVisible(true);
        
        labelInstrucoes.setText(
            "<html><div style='text-align: center; color: #E8C547;'>" +
            "<font size='6'><b>ORDEM DEFINIDA!</b></font><br><br>" +
            "<font size='5' color='#4ECDC4'>Todos os numeros sao diferentes!</font><br>" +
            "<font size='4'>Preparem-se para escolher suas especializacoes</font>" +
            "</div></html>");
        
        labelResultado.setText("Ordem estabelecida! Inicie a jornada");
        
        mostrarOrdemFinal();
    }
    
    private void iniciarDesempate() {
        criarJanelaDesempate();
    }
    
    private void criarJanelaDesempate() {
        janelaDesempate = new JFrame();
        janelaDesempate.setTitle("Rodada de Desempate");
        janelaDesempate.setSize(800, 700);
        janelaDesempate.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaDesempate.setLocationRelativeTo(janela);
        janelaDesempate.setLayout(new BorderLayout());
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(60, 30, 10), 0, getHeight(), new Color(100, 50, 20));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        JLabel titulo = new JLabel(
            "<html><div style='text-align: center; color: #FFD700;'>" +
            "<font size='6'><b>RODADA DE DESEMPATE</b></font><br>" +
            "<font size='4' color='#4ECDC4'>Jogadores com numeros unicos ja tem posicao garantida!</font><br>" +
            "<font size='4'>Apenas os seguintes jogadores sortearao novamente:</font>" +
            "</div></html>", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));
        
        JPanel painelJogadores = new JPanel(new BorderLayout());
        painelJogadores.setOpaque(false);
        painelJogadores.setBorder(BorderFactory.createEmptyBorder(25, 50, 25, 50));
        
        JPanel painelLista = new JPanel(new GridLayout(jogadoresEmpatados.size(), 1, 20, 20));
        painelLista.setOpaque(false);
        
        botoesSortearDesempate.clear();
        
        for (int i = 0; i < jogadoresEmpatados.size(); i++) {
            Jogador jogador = jogadoresEmpatados.get(i);
            
            JPanel painelJogador = new JPanel(new FlowLayout());
            painelJogador.setBackground(new Color(40, 30, 20, 200));
            painelJogador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 150, 50), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            
            JLabel labelNome = new JLabel(jogador.getNome() + " (" + jogador.getNumeroSorteado() + "): ");
            labelNome.setFont(new Font("Segoe UI", Font.BOLD, 18));
            labelNome.setForeground(Color.WHITE);
            
            JLabel labelNumero = new JLabel("?");
            labelNumero.setFont(new Font("Segoe UI", Font.BOLD, 22));
            labelNumero.setForeground(new Color(255, 215, 0));
            labelNumero.setPreferredSize(new Dimension(50, 30));
            
            JButton botaoSortearJogador = criarBotaoEstilizado("Lancar Dado", new Color(205, 127, 50), new Color(210, 140, 70));
            
            final int index = i;
            final JLabel labelNumeroRef = labelNumero;
            
            botaoSortearJogador.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int novoNumero = random.nextInt(10) + 1;
                    jogadoresEmpatados.get(index).setNumeroSorteado(novoNumero);
                    labelNumeroRef.setText(String.valueOf(novoNumero));
                    botaoSortearJogador.setEnabled(false);
                    botaoSortearJogador.setText("Sorteado");
                    
                    verificarTodosSorteados();
                }
            });
            
            painelJogador.add(labelNome);
            painelJogador.add(labelNumero);
            painelJogador.add(botaoSortearJogador);
            
            painelLista.add(painelJogador);
            botoesSortearDesempate.add(botaoSortearJogador);
        }
        
        botaoFinalizarDesempate = criarBotaoEstilizado("Finalizar Desempate", new Color(50, 205, 50), new Color(60, 220, 60));
        botaoFinalizarDesempate.setEnabled(false);
        
        botaoFinalizarDesempate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                janelaDesempate.dispose();
                processarResultadoDesempate();
            }
        });
        
        painelJogadores.add(painelLista, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        painelBotoes.add(botaoFinalizarDesempate);
        
        painelFundo.add(titulo, BorderLayout.NORTH);
        painelFundo.add(painelJogadores, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janelaDesempate.add(painelFundo);
        janelaDesempate.setVisible(true);
    }
    
    private void verificarTodosSorteados() {
        boolean todosSorteados = true;
        for (JButton botao : botoesSortearDesempate) {
            if (botao.isEnabled()) {
                todosSorteados = false;
                break;
            }
        }
        
        if (todosSorteados && botaoFinalizarDesempate != null) {
            botaoFinalizarDesempate.setEnabled(true);
        }
    }
    
    private void processarResultadoDesempate() {
        List<Jogador> todosJogadores = new ArrayList<>();
        todosJogadores.addAll(jogadoresNaoEmpatados);
        todosJogadores.addAll(jogadoresEmpatados);
        
        todosJogadores.sort((j1, j2) -> Integer.compare(j2.getNumeroSorteado(), j1.getNumeroSorteado()));
        
        jogadores = todosJogadores;
        
        temEmpate = false;
        verificarEmpates();
        
        if (temEmpate) {
            configurarInterfaceDesempate();
        } else {
            botaoDesempate.setVisible(false);
            configurarInterfaceSemEmpate();
        }
    }
    
    private void mostrarOrdemFinal() {
        JFrame janelaOrdem = new JFrame();
        janelaOrdem.setTitle("Ordem de Jogada");
        janelaOrdem.setSize(800, 700);
        janelaOrdem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaOrdem.setLocationRelativeTo(janela);
        janelaOrdem.setLayout(new BorderLayout());
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 60, 30), 0, getHeight(), new Color(50, 100, 50));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        JLabel titulo = new JLabel("ORDEM DE ESCOLHA DOS PERSONAGENS", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(255, 215, 0));
        titulo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        JPanel painelOrdem = new JPanel(new GridLayout(4, 1, 20, 20));
        painelOrdem.setOpaque(false);
        painelOrdem.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        
        if (jogadores == null || jogadores.isEmpty()) {
            JLabel labelErro = new JLabel("Nenhum jogador encontrado", SwingConstants.CENTER);
            labelErro.setFont(new Font("Segoe UI", Font.BOLD, 20));
            labelErro.setForeground(Color.RED);
            painelOrdem.add(labelErro);
        } else {
            for (int i = 0; i < jogadores.size(); i++) {
                Jogador jogador = jogadores.get(i);
                JPanel cardJogador = new JPanel(new BorderLayout());
                cardJogador.setBackground(i == 0 ? new Color(255, 215, 0, 100) : new Color(255, 255, 255, 50));
                cardJogador.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(i == 0 ? new Color(255, 215, 0) : new Color(200, 200, 200), 3),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));

                JLabel labelPosicao = new JLabel(
                    "<html><div style='text-align: center; color: " + (i == 0 ? "#FFD700" : "#FFFFFF") + ";'>" +
                    "<b>" + (i + 1) + "º Lugar:</b> " + jogador.getNome() + 
                    "<br><font size='3'>Numero Sorteado: " + jogador.getNumeroSorteado() + "</font>" +
                    "</div></html>", SwingConstants.CENTER);
                labelPosicao.setFont(new Font("Segoe UI", Font.BOLD, i == 0 ? 18 : 16));

                cardJogador.add(labelPosicao, BorderLayout.CENTER);
                painelOrdem.add(cardJogador);
            }
        }
        
        JButton botaoOK = criarBotaoEstilizado("Confirmar Ordem", new Color(70, 130, 180), new Color(100, 150, 255));
        botaoOK.addActionListener(e -> janelaOrdem.dispose());
        
        painelFundo.add(titulo, BorderLayout.NORTH);
        painelFundo.add(painelOrdem, BorderLayout.CENTER);
        painelFundo.add(botaoOK, BorderLayout.SOUTH);
        
        janelaOrdem.add(painelFundo);
        janelaOrdem.setVisible(true);
    }
    
    private void prosseguirParaPersonagens() {
        janela.dispose();
        Jogo.setJogadores(jogadores);
    }
    
    public void mostrar() {
        janela.setVisible(true);
    }
    
    public List<Jogador> getJogadores() {
        return new ArrayList<>(jogadores);
    }
}