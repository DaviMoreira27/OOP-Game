package Modelo;

public class BlocoMovel extends Personagem {
    public BlocoMovel(String sNomeImagePNG, int cDano, int cVida, boolean imported) {
        super(sNomeImagePNG, cDano, cVida, imported);
        this.bTransponivel = false; 
    }
}