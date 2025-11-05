package Model;

public class Jogador {
    private String nome;
    private Personagem personagem;
    private int numeroSorteado;
    private int pontos;
    
    public Jogador(String nome) {
        this.nome = nome;
        this.numeroSorteado = 0;
        this.pontos = 0;
        this.personagem = null;
    }
    
    public Jogador(String nome, int numeroSorteado) {
        this.nome = nome;
        this.numeroSorteado = numeroSorteado;
        this.pontos = 0;
        this.personagem = null;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Personagem getPersonagem() {
        return personagem;
    }
    
    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }
    
    public int getNumeroSorteado() {
        return numeroSorteado;
    }
    
    public void setNumeroSorteado(int numeroSorteado) {
        this.numeroSorteado = numeroSorteado;
    }
    
    public int getPontos() {
        return pontos;
    }
    
    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
    
    public void adicionarPontos(int pontos) {
        this.pontos += pontos;
    }
}