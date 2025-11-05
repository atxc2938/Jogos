package Controller;

import java.util.List;

import Model.Jogador;
import View.JanelaJogoPrincipal;

public class JogoController {
    private List<Jogador> jogadores;
    private int jogadorAtualIndex;
    private JanelaJogoPrincipal view;
    private static JogoController instance;
    
    public JogoController(List<Jogador> jogadores) {
        this.jogadores = jogadores;
        this.jogadorAtualIndex = 0;
        instance = this;
    }
    
    public static JogoController getInstance() {
        return instance;
    }
    
    public Jogador getJogadorAtual() {
        if (jogadores.isEmpty()) return null;
        return jogadores.get(jogadorAtualIndex);
    }
    
    public void setView(JanelaJogoPrincipal view) {
        this.view = view;
    }
    
    public void avancarJogador() {
        jogadorAtualIndex = (jogadorAtualIndex + 1) % jogadores.size();
        System.out.println("Proximo jogador: " + getJogadorAtual().getNome());
        
        if (view != null) {
            view.atualizarJogadorAtual(getJogadorAtual());
        }
    }
    
    public String getOrdemJogadores() {
        StringBuilder ordem = new StringBuilder();
        for (int i = 0; i < jogadores.size(); i++) {
            Jogador jogador = jogadores.get(i);
            ordem.append(i + 1).append(". ").append(jogador.getNome());
            if (i == jogadorAtualIndex) {
                ordem.append(" ATUAL");
            }
            ordem.append("\n");
        }
        return ordem.toString();
    }
    
    public List<Jogador> getJogadores() {
        return jogadores;
    }
}