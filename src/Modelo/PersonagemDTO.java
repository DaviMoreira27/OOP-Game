package Modelo;

public class PersonagemDTO {
    public int linha;
    public int coluna;
    public boolean transponivel;
    public boolean mortal;
    public int vida;
    public int dano;
    public String classe;
    public String image;

    public PersonagemDTO(Personagem p) {
        this.linha = p.getPosicao().getLinha();
        this.coluna = p.getPosicao().getColuna();
        this.transponivel = p.isbTransponivel();
        this.mortal = p.isbMortal();
        this.vida = p.getVida();
        this.dano = p.getDano();
        this.classe = p.getClass().getSimpleName();
        this.image = p.imagePath;
    }
}
