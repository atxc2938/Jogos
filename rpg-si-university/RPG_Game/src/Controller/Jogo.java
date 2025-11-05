package Controller;

import java.util.List;

import Model.Jogador;
import View.EscolhaPersonagem;
import View.JanelaJogoPrincipal;
import View.Sorteio;

public class Jogo {
    private static List<Jogador> jogadores;
    private static JogoController controller;
    
    public static void main(String[] args) {
        System.out.println("Iniciando RPG Universidade SI...");
        iniciarSorteio();
    }
    
    public static void iniciarSorteio() {
        Sorteio sorteio = new Sorteio();
        sorteio.mostrar();
    }
    
    public static void setJogadores(List<Jogador> jogadoresSorteados) {
        jogadores = jogadoresSorteados;
        controller = new JogoController(jogadores);
        iniciarEscolhaPersonagem();
    }
    
    public static void iniciarEscolhaPersonagem() {
        EscolhaPersonagem escolha = new EscolhaPersonagem(jogadores, controller);
        escolha.mostrar();
    }
    
    public static void iniciarJogoPrincipal() {
        new JanelaJogoPrincipal(jogadores, controller);
    }
}