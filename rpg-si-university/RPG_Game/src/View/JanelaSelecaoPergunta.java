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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Controller.JogoController;
import DAO.PerguntaDAO;
import Model.Jogador;

public class JanelaSelecaoPergunta extends JFrame {
    private String nomeMundo;
    private Jogador jogador;
    private JogoController controller;
    private JanelaJogoPrincipal janelaPrincipal;
    
    private List<PerguntaBanco> perguntasBanco;
    private JPanel painelBotoes;
    
    private static Map<String, Map<Integer, EstadoPergunta>> estadoPerguntasPorMundo = new HashMap<>();
    
    // LISTA CORRETA DOS MUNDOS QUE VOCÊ REALMENTE TEM BASEADO NO LOG!
    private static final String[] TODOS_MUNDOS_REAIS = {
        "Rede de Computadores", "Banco de Dados", "HTML e JavaScript", "Java", "Linguagem C"
    };
    
    public JanelaSelecaoPergunta(String nomeMundo, Jogador jogador, JogoController controller, JanelaJogoPrincipal janelaPrincipal) {
        this.nomeMundo = nomeMundo;
        this.jogador = jogador;
        this.controller = controller;
        this.janelaPrincipal = janelaPrincipal;
        
        inicializarEstadoMundo();
        carregarPerguntasDoBanco();
        
        // VERIFICAÇÃO IMEDIATA - se não tem perguntas, vai direto pro ranking
        if (!existemPerguntasDisponiveisNoMundo()) {
            System.out.println("Mundo " + nomeMundo + " concluído! Verificando todos os mundos...");
            // CORREÇÃO: Mostrar a mensagem de mundo concluído ANTES de verificar
            mostrarMensagemMundoConcluido();
            return;
        }
        
        inicializarInterface();
    }
    
    private void inicializarEstadoMundo() {
        if (!estadoPerguntasPorMundo.containsKey(nomeMundo)) {
            estadoPerguntasPorMundo.put(nomeMundo, new HashMap<>());
            // MUDANÇA: AGORA SÃO 6 PERGUNTAS POR MUNDO
            for (int i = 1; i <= 6; i++) {
                estadoPerguntasPorMundo.get(nomeMundo).put(i, new EstadoPergunta());
            }
        }
    }
    
    private void carregarPerguntasDoBanco() {
        perguntasBanco = new ArrayList<>();
        List<DAO.PerguntaDAO.Pergunta> perguntas = PerguntaDAO.getPerguntasPorMundo(nomeMundo);
        
        if (perguntas.isEmpty()) {
            System.out.println("Nenhuma pergunta encontrada para o mundo: " + nomeMundo);
            return;
        }
        
        int numero = 1;
        for (DAO.PerguntaDAO.Pergunta pergunta : perguntas) {
            // MUDANÇA: AGORA SÃO 6 PERGUNTAS POR MUNDO
            if (numero > 6) break;
            
            PerguntaBanco perguntaBanco = new PerguntaBanco(
                numero, 
                nomeMundo, 
                pergunta.getTexto(), 
                pergunta.getOpcoes(), 
                pergunta.getRespostaCorreta()
            );
            perguntasBanco.add(perguntaBanco);
            numero++;
        }
    }
    
    private void inicializarInterface() {
        setTitle("Selecao de Pergunta - " + nomeMundo);
        setSize(900, 700);
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
        painelFundo.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        JPanel painelCabecalho = new JPanel(new BorderLayout());
        painelCabecalho.setOpaque(false);
        painelCabecalho.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        JLabel lblTitulo = new JLabel("ESCOLHA SUA PERGUNTA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(255, 215, 0));
        
        JLabel lblMundo = new JLabel("Mundo: " + nomeMundo.toUpperCase() + " | Jogador: " + jogador.getNome(), SwingConstants.CENTER);
        lblMundo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMundo.setForeground(new Color(100, 200, 255));
        
        painelCabecalho.add(lblTitulo, BorderLayout.NORTH);
        painelCabecalho.add(lblMundo, BorderLayout.CENTER);
        
        // MUDANÇA: GridLayout para 6 perguntas (2 linhas, 3 colunas)
        painelBotoes = new JPanel(new GridLayout(2, 3, 15, 15));
        painelBotoes.setOpaque(false);
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        criarBotoesPerguntas();
        
        // Se não tem perguntas disponíveis, mostra botão de mundo concluído
        if (!existemPerguntasDisponiveisNoMundo()) {
            adicionarBotaoMundoConcluido();
        }
        
        JPanel painelInfo = new JPanel();
        painelInfo.setOpaque(false);
        painelInfo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JLabel lblInfo = new JLabel(
            "<html><div style='text-align: center; color: #4ECDC4;'>" +
            "Disponivel | Ja acertada | Protegida" +
            "</div></html>", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        painelInfo.add(lblInfo);
        
        painelFundo.add(painelCabecalho, BorderLayout.NORTH);
        painelFundo.add(painelBotoes, BorderLayout.CENTER);
        painelFundo.add(painelInfo, BorderLayout.SOUTH);
        
        add(painelFundo);
    }
    
    // CORREÇÃO: Método simplificado para mostrar mensagem de mundo concluído
    private void mostrarMensagemMundoConcluido() {
        // Primeiro mostra a mensagem
        JFrame janelaMensagem = new JFrame();
        janelaMensagem.setTitle("Mundo Concluido");
        janelaMensagem.setSize(500, 400);
        janelaMensagem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaMensagem.setLocationRelativeTo(null);
        janelaMensagem.setLayout(new BorderLayout());
        janelaMensagem.setUndecorated(true);
        
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
        
        JLabel lblMensagem = new JLabel("<html><div style='text-align: center;'>Todas as 6 perguntas do mundo<br><b>" + nomeMundo.toUpperCase() + "</b><br>ja foram respondidas!</div></html>", SwingConstants.CENTER);
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
                janelaMensagem.dispose();
                dispose();
                verificarTodosMundosImediatamente();
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
        
        janelaMensagem.add(painelFundo);
        janelaMensagem.setVisible(true);
    }
    
    private void adicionarBotaoMundoConcluido() {
        JButton btnMundoConcluido = criarBotaoMundoConcluido();
        btnMundoConcluido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // VERIFICAÇÃO FORÇADA ao clicar no botão
                verificarTodosMundosImediatamente();
            }
        });
        
        painelBotoes.removeAll();
        painelBotoes.setLayout(new BorderLayout());
        painelBotoes.add(btnMundoConcluido, BorderLayout.CENTER);
    }
    
    private JButton criarBotaoMundoConcluido() {
        JButton botao = new JButton("<html><center><b>MUNDO CONCLUÍDO!</b><br>Todas as 6 perguntas foram respondidas!<br>Clique para CONTINUAR</center></html>") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color corBase = new Color(46, 204, 113);
                Color corHover = new Color(39, 174, 96);
                Color corAtual = getModel().isRollover() ? corHover : corBase;
                
                g2d.setColor(corAtual);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                
                String[] lines = getText().replace("<html>", "").replace("</html>", "").split("<br>");
                int lineHeight = g2d.getFontMetrics().getHeight();
                int totalHeight = lines.length * lineHeight;
                int y = (getHeight() - totalHeight) / 2 + g2d.getFontMetrics().getAscent();
                
                for (String line : lines) {
                    String cleanLine = line.replaceAll("<[^>]*>", "").trim();
                    int x = (getWidth() - g2d.getFontMetrics().stringWidth(cleanLine)) / 2;
                    g2d.drawString(cleanLine, x, y);
                    y += lineHeight;
                }
            }
            
            @Override
            public void setContentAreaFilled(boolean b) {
                super.setContentAreaFilled(false);
            }
        };
        
        botao.setFont(new Font("Segoe UI", Font.BOLD, 18));
        botao.setForeground(Color.WHITE);
        botao.setPreferredSize(new Dimension(400, 150));
        botao.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        return botao;
    }
    
    // NOVA ABORDAGEM - Verificação IMEDIATA e FORÇADA
    private void verificarTodosMundosImediatamente() {
        System.out.println("=== VERIFICAÇÃO FORÇADA DE TODOS OS MUNDOS ===");
        
        boolean todosRespondidos = true;
        
        for (String mundo : TODOS_MUNDOS_REAIS) {
            boolean mundoConcluido = !existemPerguntasDisponiveisNoMundo(mundo);
            System.out.println("Mundo " + mundo + ": " + (mundoConcluido ? "CONCLUÍDO" : "PENDENTE"));
            
            if (!mundoConcluido) {
                todosRespondidos = false;
            }
        }
        
        System.out.println("Todos os mundos concluídos? " + todosRespondidos);
        
        if (todosRespondidos) {
            System.out.println("=== ABRINDO TELA DE TODOS MUNDOS ACABARAM ===");
            abrirTelaFinal();
        } else {
            System.out.println("=== AINDA HÁ MUNDOS PENDENTES ===");
            // Se não terminou todos, volta para a janela principal
            if (janelaPrincipal != null) {
                janelaPrincipal.setVisible(true);
            }
            dispose();
        }
    }
    
    private void abrirTelaFinal() {
        // Fecha TODAS as janelas
        java.awt.Window[] windows = java.awt.Window.getWindows();
        for (java.awt.Window window : windows) {
            window.dispose();
        }
        
        // Abre a tela final
        if (controller != null) {
            List<Jogador> jogadores = controller.getJogadores();
            new JanelaFimDeJogo(jogadores).mostrar();
        } else {
            // CORREÇÃO: Abrir a tela corretamente centralizada
            JFrame frame = new TodosMundosAcabaram();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
    
    private void abrirJanelaPergunta(PerguntaBanco pergunta, int numeroPergunta) {
        dispose();
        
        JanelaPergunta.Pergunta perguntaObj = new JanelaPergunta.Pergunta(
            pergunta.getTexto(), pergunta.getOpcoes(), pergunta.getRespostaCorreta()
        );
        
        JanelaPergunta janelaPergunta = new JanelaPergunta(
            nomeMundo, jogador, controller, janelaPrincipal, perguntaObj, numeroPergunta
        );
        janelaPergunta.mostrar();
    }
    
    private boolean existemPerguntasDisponiveisNoMundo() {
        for (PerguntaBanco pergunta : perguntasBanco) {
            if (isPerguntaDisponivel(pergunta.getNumero())) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean existemPerguntasDisponiveisNoMundo(String nomeMundo) {
        if (!estadoPerguntasPorMundo.containsKey(nomeMundo)) {
            return true; // Se o mundo nem foi inicializado, tem perguntas disponíveis
        }
        
        Map<Integer, EstadoPergunta> perguntasMundo = estadoPerguntasPorMundo.get(nomeMundo);
        for (EstadoPergunta estado : perguntasMundo.values()) {
            if (!estado.isAcertada()) {
                return true; // Ainda tem pergunta não acertada
            }
        }
        return false; // Todas as perguntas foram acertadas
    }
    
    private void criarBotoesPerguntas() {
        for (PerguntaBanco pergunta : perguntasBanco) {
            final int numeroPergunta = pergunta.getNumero();
            final boolean disponivel = isPerguntaDisponivel(numeroPergunta);
            final EstadoPergunta estado = estadoPerguntasPorMundo.get(nomeMundo).get(numeroPergunta);
            
            JButton btnPergunta = criarBotaoPergunta(numeroPergunta, disponivel, estado);
            
            btnPergunta.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (disponivel) {
                        abrirJanelaPergunta(pergunta, numeroPergunta);
                    }
                }
            });
            
            painelBotoes.add(btnPergunta);
        }
    }
    
    private JButton criarBotaoPergunta(int numero, boolean disponivel, EstadoPergunta estado) {
        String texto = "<html><center><b>PERGUNTA " + numero + "</b></center></html>";
        Color corBase, corTexto;
        
        if (!disponivel) {
            if (estado != null && estado.isAcertada()) {
                texto = "<html><center><b>PERGUNTA " + numero + "</b><br>CONCLUIDA</center></html>";
                corBase = new Color(100, 100, 100);
                corTexto = new Color(200, 200, 200);
            } else if (estado != null && estado.isProtegida()) {
                texto = "<html><center><b>PERGUNTA " + numero + "</b><br>PROTEGIDA</center></html>";
                corBase = new Color(255, 165, 0);
                corTexto = Color.WHITE;
            } else {
                corBase = new Color(100, 100, 100);
                corTexto = new Color(200, 200, 200);
            }
        } else {
            corBase = new Color(70, 130, 180);
            corTexto = Color.WHITE;
        }
        
        final Color finalCorBase = corBase;
        final Color finalCorTexto = corTexto;
        
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color corAtual = getModel().isRollover() && disponivel ? 
                    finalCorBase.brighter() : finalCorBase;
                g2d.setColor(corAtual);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2d.setColor(finalCorTexto);
                g2d.setFont(getFont());
                
                String[] lines = getText().replace("<html>", "").replace("</html>", "").split("<br>");
                int lineHeight = g2d.getFontMetrics().getHeight();
                int y = (getHeight() - (lines.length * lineHeight)) / 2 + g2d.getFontMetrics().getAscent();
                
                for (String line : lines) {
                    String cleanLine = line.replaceAll("<[^>]*>", "").trim();
                    int x = (getWidth() - g2d.getFontMetrics().stringWidth(cleanLine)) / 2;
                    g2d.drawString(cleanLine, x, y);
                    y += lineHeight;
                }
            }
            
            @Override
            public void setContentAreaFilled(boolean b) {
                super.setContentAreaFilled(false);
            }
        };
        
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setForeground(finalCorTexto);
        botao.setPreferredSize(new Dimension(120, 80));
        botao.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        botao.setEnabled(disponivel);
        
        return botao;
    }
    
    private boolean isPerguntaDisponivel(int numeroPergunta) {
        EstadoPergunta estado = estadoPerguntasPorMundo.get(nomeMundo).get(numeroPergunta);
        
        if (estado == null) {
            return true;
        }
        
        if (estado.isProtegida() && !estado.getProtegidaPor().equals(jogador.getNome())) {
            return false;
        }
        
        return !estado.isAcertada();
    }
    
    public static void atualizarEstadoPergunta(String mundo, int numeroPergunta, boolean acertou, String jogadorNome) {
        if (!estadoPerguntasPorMundo.containsKey(mundo)) {
            estadoPerguntasPorMundo.put(mundo, new HashMap<>());
        }
        
        if (!estadoPerguntasPorMundo.get(mundo).containsKey(numeroPergunta)) {
            estadoPerguntasPorMundo.get(mundo).put(numeroPergunta, new EstadoPergunta());
        }
        
        EstadoPergunta estado = estadoPerguntasPorMundo.get(mundo).get(numeroPergunta);
        
        if (acertou) {
            estado.setAcertada(true);
            estado.setProtegida(false);
            estado.setProtegidaPor(null);
            System.out.println("Pergunta " + numeroPergunta + " do mundo " + mundo + " marcada como CONCLUÍDA");
        }
    }
    
    public static void protegerPergunta(String mundo, int numeroPergunta, String jogador) {
        if (estadoPerguntasPorMundo.containsKey(mundo)) {
            EstadoPergunta estado = estadoPerguntasPorMundo.get(mundo).get(numeroPergunta);
            if (estado != null) {
                estado.setProtegida(true);
                estado.setProtegidaPor(jogador);
            }
        }
    }
    
    public void mostrar() {
        setVisible(true);
    }
    
    public static void resetarJogo() {
        estadoPerguntasPorMundo.clear();
    }
    
    public static class EstadoPergunta {
        private boolean acertada = false;
        private boolean protegida = false;
        private String protegidaPor = null;
        
        public boolean isAcertada() { return acertada; }
        public void setAcertada(boolean acertada) { this.acertada = acertada; }
        
        public boolean isProtegida() { return protegida; }
        public void setProtegida(boolean protegida) { this.protegida = protegida; }
        
        public String getProtegidaPor() { return protegidaPor; }
        public void setProtegidaPor(String protegidaPor) { this.protegidaPor = protegidaPor; }
    }
    
    public class PerguntaBanco {
        private int numero;
        private String mundo;
        private String texto;
        private String[] opcoes;
        private int respostaCorreta;
        
        public PerguntaBanco(int numero, String mundo, String texto, String[] opcoes, int respostaCorreta) {
            this.numero = numero;
            this.mundo = mundo;
            this.texto = texto;
            this.opcoes = opcoes;
            this.respostaCorreta = respostaCorreta;
        }
        
        public int getNumero() { return numero; }
        public String getMundo() { return mundo; }
        public String getTexto() { return texto; }
        public String[] getOpcoes() { return opcoes; }
        public int getRespostaCorreta() { return respostaCorreta; }
    }
}