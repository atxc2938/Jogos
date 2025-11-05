package Model;

public abstract class Personagem {
    private String nome;
    private String habilidade;
    private String image;
    private int usosHabilidade;
    private String descricaoHabilidade;
    
    public Personagem(String nome, String habilidade, String image, int usosHabilidade, String descricaoHabilidade) {
        this.nome = nome;
        this.habilidade = habilidade;
        this.image = image;
        this.usosHabilidade = usosHabilidade;
        this.descricaoHabilidade = descricaoHabilidade;
    }
    
    public String getDescricaoHabilidade() {
        return descricaoHabilidade;
    }

    public void setDescricaoHabilidade(String descricaoHabilidade) {
        this.descricaoHabilidade = descricaoHabilidade;
    }

    public int getUsosHabilidade() {
        return usosHabilidade;
    }

    public void setUsosHabilidade(int usosHabilidade) {
        this.usosHabilidade = usosHabilidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHabilidade() {
        return habilidade;
    }

    public void setHabilidade(String habilidade) {
        this.habilidade = habilidade;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public void habilidadeUsada() {
        if (this.usosHabilidade > 0) {
            this.usosHabilidade = this.usosHabilidade - 1;
        } else {
            System.out.println("Você não tem uso de habilidade disponível :( ");
        }
    }
}