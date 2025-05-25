package Modelo;

import Auxiliar.Posicao;

public class PersonagemDTO {
    public int linha;
    public int coluna;
    public boolean transponivel;
    public boolean mortal;
    public int vida;
    public int dano;
    public String classe;
    public String image;
    public boolean imported;


    public PersonagemDTO (PersonagemIncompleto p, Posicao pos, String img) {
        this.linha = pos.getLinha();
        this.coluna = pos.getColuna();
        this.transponivel = p.transponivel;
        this.mortal = p.mortal;
        this.vida = p.vida;
        this.dano = p.dano;
        this.classe = p.classe;
        this.image = img;
        this.imported = true;
    }

    public PersonagemDTO(Personagem p) {
        this.linha = p.getPosicao().getLinha();
        this.coluna = p.getPosicao().getColuna();
        this.transponivel = p.isbTransponivel();
        this.mortal = p.isbMortal();
        this.vida = p.getVida();
        this.dano = p.getDano();
        this.classe = p.getClass().getSimpleName();
        this.image = p.imagePath;
        this.imported = p.imported;
    }
}

