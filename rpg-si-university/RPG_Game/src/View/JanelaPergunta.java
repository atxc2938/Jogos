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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import Controller.JogoController;
import Model.CyberSecurity;
import Model.DataScientist;
import Model.DevOps;
import Model.FullStackDeveloper;
import Model.Jogador;

public class JanelaPergunta extends JFrame {
    private String nomeMundo;
    private Jogador jogador;
    private JogoController controller;
    private JanelaJogoPrincipal janelaPrincipal;
    
    private JLabel lblPergunta;
    private JRadioButton[] opcoes;
    private ButtonGroup grupoOpcoes;
    private JButton btnResponder;
    private JButton btnUsarHabilidade;
    private JButton btnSair;
    
    private Pergunta perguntaAtual;
    private boolean segundaChance = false;
    private boolean respostaUsada = false;
    private int numeroPergunta;
    
    private static Map<String, Map<String, Boolean>> perguntasRespondidas = new HashMap<>();
    private static Map<String, String> perguntasProtegidas = new HashMap<>();
    private static Map<String, Integer> acertosConsecutivos = new HashMap<>();
    
    public JanelaPergunta(String nomeMundo, Jogador jogador, JogoController controller, JanelaJogoPrincipal janelaPrincipal, Pergunta perguntaEspecifica, int numeroPergunta) {
        this.nomeMundo = nomeMundo;
        this.jogador = jogador;
        this.controller = controller;
        this.janelaPrincipal = janelaPrincipal;
        this.perguntaAtual = perguntaEspecifica;
        this.numeroPergunta = numeroPergunta;
        
        setTitle("Desafio " + numeroPergunta + " - " + nomeMundo);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        carregarInterface();
    }
    
    private void carregarInterface() {
        getContentPane().removeAll();
        
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
        painelFundo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel painelCabecalho = new JPanel(new BorderLayout());
        painelCabecalho.setOpaque(false);
        painelCabecalho.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        String tituloTexto = "DESAFIO " + numeroPergunta + ": " + nomeMundo.toUpperCase();
            
        JLabel lblTitulo = new JLabel(tituloTexto, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        int acertosSeq = getAcertosConsecutivos(jogador.getNome());
        String bonusTexto = (acertosSeq > 0) ? " | Bonus: +" + (acertosSeq * 5) + " pontos" : "";
        
        JLabel lblJogador = new JLabel("Jogador: " + jogador.getNome() + " | " + 
            (jogador.getPersonagem() != null ? jogador.getPersonagem().getNome() : "Sem especializacao") + bonusTexto, 
            SwingConstants.CENTER);
        lblJogador.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJogador.setForeground(new Color(100, 200, 255));
        
        painelCabecalho.add(lblTitulo, BorderLayout.NORTH);
        painelCabecalho.add(lblJogador, BorderLayout.CENTER);
        
        JPanel painelPergunta = new JPanel(new BorderLayout());
        painelPergunta.setBackground(new Color(40, 45, 80));
        painelPergunta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 2),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        lblPergunta = new JLabel("<html><div style='text-align: center; width: 650px; color: #FFFFFF;'>" +
                                perguntaAtual.getTexto() + "</div></html>", SwingConstants.CENTER);
        lblPergunta.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        painelPergunta.add(lblPergunta, BorderLayout.NORTH);
        
        JPanel painelOpcoes = new JPanel(new GridLayout(4, 1, 15, 15));
        painelOpcoes.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        painelOpcoes.setBackground(new Color(40, 45, 80));
        
        opcoes = new JRadioButton[4];
        grupoOpcoes = new ButtonGroup();
        
        char[] letras = {'A', 'B', 'C', 'D'};
        for (int i = 0; i < 4; i++) {
            opcoes[i] = new JRadioButton("<html><font color='#FFFFFF'>" + letras[i] + ") " + perguntaAtual.getOpcoes()[i] + "</font></html>");
            opcoes[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            opcoes[i].setBackground(new Color(40, 45, 80));
            opcoes[i].setForeground(Color.WHITE);
            opcoes[i].setOpaque(true);
            grupoOpcoes.add(opcoes[i]);
            painelOpcoes.add(opcoes[i]);
        }
        
        painelPergunta.add(painelOpcoes, BorderLayout.CENTER);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        btnResponder = criarBotaoEstilizado("Responder Pergunta", new Color(70, 130, 180), new Color(100, 150, 255));
        btnUsarHabilidade = criarBotaoEstilizado("Usar Habilidade", new Color(255, 140, 0), new Color(255, 165, 0));
        btnSair = criarBotaoEstilizado("Sair", new Color(220, 20, 60), new Color(255, 50, 50));
        
        painelBotoes.add(btnResponder);
        painelBotoes.add(btnUsarHabilidade);
        painelBotoes.add(btnSair);
        
        painelFundo.add(painelCabecalho, BorderLayout.NORTH);
        painelFundo.add(painelPergunta, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        add(painelFundo);
        
        configurarAcoes();
        atualizarInterfaceHabilidade();
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
        
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(180, 45));
        botao.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        
        return botao;
    }
    
    private void configurarAcoes() {
        btnResponder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                responderPergunta();
            }
        });
        
        btnUsarHabilidade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usarHabilidade();
            }
        });
        
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sairSemResponder();
            }
        });
    }
    
    private int getAcertosConsecutivos(String nomeJogador) {
        return acertosConsecutivos.getOrDefault(nomeJogador, 0);
    }
    
    private int calcularPontuacao(boolean acertou) {
        String nomeJogador = jogador.getNome();
        int acertosSeq = getAcertosConsecutivos(nomeJogador);
        
        if (acertou) {
            int base = 10;
            int bonus = acertosSeq * 5;
            int total = base + bonus;
            
            if (respostaUsada && jogador.getPersonagem() instanceof DevOps) {
                int bonusDevOps = (int)(total * 0.5);
                total += bonusDevOps;
                return total;
            }
            
            acertosConsecutivos.put(nomeJogador, acertosSeq + 1);
            return total;
        } else {
            acertosConsecutivos.put(nomeJogador, 0);
            return 0;
        }
    }
    
    private int calcularPontuacaoBase(boolean acertou) {
        String nomeJogador = jogador.getNome();
        int acertosSeq = getAcertosConsecutivos(nomeJogador);
        
        if (acertou) {
            int base = 10;
            int bonus = acertosSeq * 5;
            return base + bonus;
        } else {
            return 0;
        }
    }
    
    private boolean isPerguntaProtegida() {
        String perguntaId = gerarPerguntaId();
        return perguntasProtegidas.containsKey(perguntaId) && 
               !perguntasProtegidas.get(perguntaId).equals(jogador.getNome());
    }
    
    private String gerarPerguntaId() {
        return nomeMundo + "_" + perguntaAtual.getTexto().hashCode();
    }
    
    private void registrarRespostaPergunta(boolean acertou) {
        String perguntaId = gerarPerguntaId();
        
        if (!perguntasRespondidas.containsKey(nomeMundo)) {
            perguntasRespondidas.put(nomeMundo, new HashMap<>());
        }
        
        perguntasRespondidas.get(nomeMundo).put(perguntaId, acertou);
        
        if (perguntasProtegidas.containsKey(perguntaId)) {
            perguntasProtegidas.remove(perguntaId);
        }
        
        if (numeroPergunta > 0) {
            JanelaSelecaoPergunta.atualizarEstadoPergunta(nomeMundo, numeroPergunta, acertou, jogador.getNome());
        }
    }
    
    private boolean isPerguntaJaAcertada() {
        String perguntaId = gerarPerguntaId();
        return perguntasRespondidas.containsKey(nomeMundo) && 
               perguntasRespondidas.get(nomeMundo).getOrDefault(perguntaId, false);
    }
    
    private void responderPergunta() {
        if (isPerguntaProtegida()) {
            JOptionPane.showMessageDialog(this, 
                "Esta pergunta esta protegida por Firewall!Apenas o jogador que ativou a protecao pode responde-la.",
                "Pergunta Protegida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (isPerguntaJaAcertada()) {
            JOptionPane.showMessageDialog(this, 
                "Voce ja acertou esta pergunta!Escolha outra pergunta para responder.",
                "Pergunta Ja Concluida",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int opcaoSelecionada = -1;
        for (int i = 0; i < opcoes.length; i++) {
            if (opcoes[i].isSelected()) {
                opcaoSelecionada = i;
                break;
            }
        }
        
        if (opcaoSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma opcao antes de responder!");
            return;
        }
        
        boolean acertou = (opcaoSelecionada == perguntaAtual.getRespostaCorreta());
        
        int pontosBase = calcularPontuacaoBase(acertou);
        int pontosTotal = calcularPontuacao(acertou);
        int novaSequencia = getAcertosConsecutivos(jogador.getNome());
        
        if (janelaPrincipal != null) {
            janelaPrincipal.atualizarEstatisticas(acertou, novaSequencia);
        }
        
        if (acertou) {
            mostrarJanelaAcerto(pontosBase, pontosTotal, novaSequencia);
            jogador.adicionarPontos(pontosTotal);
            registrarRespostaPergunta(true);
            
        } else {
            if (segundaChance && jogador.getPersonagem() instanceof DataScientist) {
                mostrarJanelaSegundaChance();
                segundaChance = false;
                grupoOpcoes.clearSelection();
            } else {
                mostrarJanelaErro();
                registrarRespostaPergunta(false);
            }
        }
    }
    
    private void mostrarJanelaAcerto(int pontosBase, int pontosTotal, int sequencia) {
        JFrame janelaAcerto = new JFrame();
        janelaAcerto.setTitle("Resposta Correta!");
        janelaAcerto.setSize(500, 450);
        janelaAcerto.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaAcerto.setLocationRelativeTo(this);
        janelaAcerto.setLayout(new BorderLayout());
        janelaAcerto.setUndecorated(true);
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(40, 100, 40), 0, getHeight(), new Color(20, 60, 20));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(100, 255, 100));
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("RESPOSTA CORRETA!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(100, 255, 100));
        
        JLabel lblPontos = new JLabel("+" + pontosTotal + " PONTOS", SwingConstants.CENTER);
        lblPontos.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblPontos.setForeground(new Color(255, 255, 100));
        lblPontos.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        JLabel lblDetalhes = new JLabel("", SwingConstants.CENTER);
        lblDetalhes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDetalhes.setForeground(new Color(200, 255, 200));
        
        if (respostaUsada && jogador.getPersonagem() instanceof DevOps) {
            int bonusDevOps = pontosTotal - pontosBase;
            lblDetalhes.setText("<html><div style='text-align: center;'>" +
                               "Pontuacao Base: " + pontosBase + "<br>" +
                               "Bonus DevOps: +" + bonusDevOps + "<br>" +
                               "Total: " + pontosTotal + "</div></html>");
        } else {
            lblDetalhes.setText("<html><div style='text-align: center;'>" +
                               "Pontuacao Base: " + pontosBase + "<br>" +
                               "Bonus Sequencia: +" + (pontosBase - 10) + "</div></html>");
        }
        
        JLabel lblSequencia = new JLabel("Sequencia: " + sequencia + " acertos consecutivos", SwingConstants.CENTER);
        lblSequencia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSequencia.setForeground(new Color(200, 255, 200));
        
        JButton btnContinuar = criarBotaoEstilizado("CONTINUAR", new Color(70, 180, 70), new Color(100, 220, 100));
        btnContinuar.addActionListener(e -> {
            janelaAcerto.dispose();
            finalizarPergunta(true);
        });
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(lblTitulo, BorderLayout.NORTH);
        painelCentral.add(lblPontos, BorderLayout.CENTER);
        painelCentral.add(lblDetalhes, BorderLayout.CENTER);
        painelCentral.add(lblSequencia, BorderLayout.SOUTH);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        painelBotoes.add(btnContinuar);
        
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janelaAcerto.add(painelFundo);
        janelaAcerto.setVisible(true);
    }
    
    private void mostrarJanelaSegundaChance() {
        JFrame janelaSegundaChance = new JFrame();
        janelaSegundaChance.setTitle("Segunda Chance!");
        janelaSegundaChance.setSize(450, 350);
        janelaSegundaChance.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaSegundaChance.setLocationRelativeTo(this);
        janelaSegundaChance.setLayout(new BorderLayout());
        janelaSegundaChance.setUndecorated(true);
        
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
        painelFundo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("SEGUNDA CHANCE!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(200, 150, 255));
        
        JLabel lblMensagem = new JLabel("<html><div style='text-align: center;'>Habilidade Data Scientist ativada!Voce tem uma segunda chance!</div></html>", SwingConstants.CENTER);
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblMensagem.setForeground(new Color(220, 200, 255));
        lblMensagem.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnContinuar = criarBotaoEstilizado("CONTINUAR", new Color(120, 100, 200), new Color(150, 130, 220));
        btnContinuar.addActionListener(e -> janelaSegundaChance.dispose());
        
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
        
        janelaSegundaChance.add(painelFundo);
        janelaSegundaChance.setVisible(true);
    }
    
    private void mostrarJanelaErro() {
        JFrame janelaErro = new JFrame();
        janelaErro.setTitle("Resposta Incorreta");
        janelaErro.setSize(400, 300);
        janelaErro.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaErro.setLocationRelativeTo(this);
        janelaErro.setLayout(new BorderLayout());
        janelaErro.setUndecorated(true);
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(100, 40, 40), 0, getHeight(), new Color(60, 20, 20));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(255, 100, 100));
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("RESPOSTA INCORRETA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 150, 150));
        
        JLabel lblMensagem = new JLabel("<html><div style='text-align: center;'>Sequencia de acertos zerada.Tente novamente na proxima vez!</div></html>", SwingConstants.CENTER);
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensagem.setForeground(new Color(255, 200, 200));
        lblMensagem.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnContinuar = criarBotaoEstilizado("CONTINUAR", new Color(180, 70, 70), new Color(220, 100, 100));
        btnContinuar.addActionListener(e -> {
            janelaErro.dispose();
            finalizarPergunta(false);
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
        
        janelaErro.add(painelFundo);
        janelaErro.setVisible(true);
    }
    
    private void usarHabilidade() {
        if (jogador.getPersonagem() == null) {
            JOptionPane.showMessageDialog(this,
                "Voce nao tem uma especializacao selecionada!",
                "Habilidade Indisponivel",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (jogador.getPersonagem().getUsosHabilidade() <= 0) {
            JOptionPane.showMessageDialog(this,
                "Voce nao tem mais usos de habilidade disponiveis!",
                "Habilidade Indisponivel",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        jogador.getPersonagem().habilidadeUsada();
        
        if (jogador.getPersonagem() instanceof FullStackDeveloper) {
            eliminarAlternativaErrada();
            
        } else if (jogador.getPersonagem() instanceof DataScientist) {
            segundaChance = true;
            mostrarJanelaHabilidadeAtivada("Previsao de Dados", "Voce tera uma segunda chance caso erre esta pergunta!");
            
        } else if (jogador.getPersonagem() instanceof CyberSecurity) {
            String perguntaId = gerarPerguntaId();
            perguntasProtegidas.put(perguntaId, jogador.getNome());
            
            if (numeroPergunta > 0) {
                JanelaSelecaoPergunta.protegerPergunta(nomeMundo, numeroPergunta, jogador.getNome());
            }
            
            mostrarJanelaHabilidadeAtivada("Firewall Defensivo", "Esta pergunta estara protegida para TODOS os outros jogadores!");
            
        } else if (jogador.getPersonagem() instanceof DevOps) {
            respostaUsada = true;
            mostrarJanelaHabilidadeAtivada("Auto Scaling", "Sua proxima resposta correta valera 50% a mais de pontos!");
        }
        
        atualizarInterfaceHabilidade();
    }
    
    private void mostrarJanelaHabilidadeAtivada(String nomeHabilidade, String descricao) {
        JFrame janelaHabilidade = new JFrame();
        janelaHabilidade.setTitle("Habilidade Ativada");
        janelaHabilidade.setSize(500, 400);
        janelaHabilidade.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaHabilidade.setLocationRelativeTo(this);
        janelaHabilidade.setLayout(new BorderLayout());
        janelaHabilidade.setUndecorated(true);
        
        JPanel painelFundo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(80, 60, 120), 0, getHeight(), new Color(50, 30, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(new Color(200, 150, 255));
                g2d.setStroke(new java.awt.BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            }
        };
        painelFundo.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
        
        JLabel lblTitulo = new JLabel("HABILIDADE USADA!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        JLabel lblHabilidade = new JLabel(nomeHabilidade, SwingConstants.CENTER);
        lblHabilidade.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblHabilidade.setForeground(new Color(180, 200, 255));
        lblHabilidade.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        JLabel lblDescricao = new JLabel("<html><div style='text-align: center; font-size: 16px;'>" + descricao + "</div></html>", SwingConstants.CENTER);
        lblDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblDescricao.setForeground(new Color(220, 220, 255));
        
        JLabel lblUsos = new JLabel("Usos restantes: " + jogador.getPersonagem().getUsosHabilidade(), SwingConstants.CENTER);
        lblUsos.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsos.setForeground(new Color(200, 255, 200));
        lblUsos.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        JButton btnContinuar = criarBotaoEstilizado("CONTINUAR", new Color(120, 80, 180), new Color(150, 110, 200));
        btnContinuar.addActionListener(e -> janelaHabilidade.dispose());
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(lblTitulo, BorderLayout.NORTH);
        painelCentral.add(lblHabilidade, BorderLayout.CENTER);
        painelCentral.add(lblDescricao, BorderLayout.SOUTH);
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        painelBotoes.add(btnContinuar);
        
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(lblUsos, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janelaHabilidade.add(painelFundo);
        janelaHabilidade.setVisible(true);
    }
    
    private void eliminarAlternativaErrada() {
        List<Integer> opcoesErradas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != perguntaAtual.getRespostaCorreta() && opcoes[i].isEnabled()) {
                opcoesErradas.add(i);
            }
        }
        
        if (!opcoesErradas.isEmpty()) {
            Random random = new Random();
            int opcaoEliminada = opcoesErradas.get(random.nextInt(opcoesErradas.size()));
            opcoes[opcaoEliminada].setEnabled(false);
            opcoes[opcaoEliminada].setText("<html><font color='#FF6B6B'>" + opcoes[opcaoEliminada].getText().replace("<html><font color='#FFFFFF'>", "").replace("</font></html>", "") + " [ELIMINADA]</font></html>");
            
            mostrarJanelaHabilidadeAtivada("Debug Tatico", "Uma alternativa incorreta foi eliminada!");
        }
    }
    
    private void sairSemResponder() {
        JFrame janelaConfirmacao = new JFrame();
        janelaConfirmacao.setTitle("Confirmar Saida");
        janelaConfirmacao.setSize(400, 300);
        janelaConfirmacao.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaConfirmacao.setLocationRelativeTo(this);
        janelaConfirmacao.setLayout(new BorderLayout());
        janelaConfirmacao.setUndecorated(true);
        
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
        painelFundo.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("CONFIRMAR SAIDA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        JLabel lblMensagem = new JLabel("<html><div style='text-align: center;'>Deseja sair sem responder?O proximo jogador sera chamado automaticamente.</div></html>", SwingConstants.CENTER);
        lblMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensagem.setForeground(Color.WHITE);
        lblMensagem.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton btnSim = criarBotaoEstilizado("SIM", new Color(220, 80, 80), new Color(255, 100, 100));
        btnSim.addActionListener(e -> {
            janelaConfirmacao.dispose();
            finalizarPergunta(false);
        });
        
        JButton btnNao = criarBotaoEstilizado("NAO", new Color(80, 80, 220), new Color(100, 100, 255));
        btnNao.addActionListener(e -> janelaConfirmacao.dispose());
        
        painelBotoes.add(btnSim);
        painelBotoes.add(btnNao);
        
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setOpaque(false);
        painelCentral.add(lblTitulo, BorderLayout.NORTH);
        painelCentral.add(lblMensagem, BorderLayout.CENTER);
        
        painelFundo.add(painelCentral, BorderLayout.CENTER);
        painelFundo.add(painelBotoes, BorderLayout.SOUTH);
        
        janelaConfirmacao.add(painelFundo);
        janelaConfirmacao.setVisible(true);
    }
    
    private void atualizarInterfaceHabilidade() {
        if (jogador.getPersonagem() != null) {
            btnUsarHabilidade.setText("Usar Habilidade (" + 
                jogador.getPersonagem().getUsosHabilidade() + " restantes)");
            
            if (jogador.getPersonagem().getUsosHabilidade() <= 0) {
                btnUsarHabilidade.setEnabled(false);
            }
        }
    }
    
    private void finalizarPergunta(boolean acertou) {
        dispose();
        
        if (janelaPrincipal != null) {
            janelaPrincipal.avancarParaProximoJogador();
        }
    }
    
    public void mostrar() {
        setVisible(true);
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