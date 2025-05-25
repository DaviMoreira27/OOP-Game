package Modelo;

public class PersonagemIncompleto {
    public boolean transponivel;
    public boolean mortal;
    public int vida;
    public int dano;
    public String classe;
    public boolean imported = true;

    public PersonagemIncompleto (int cDano, int cVida, boolean transpon, boolean mort, String classe) {
        this.transponivel = transpon;
        this.mortal = mort;
        this.vida = cVida;
        this.dano = cDano;
        this.classe = classe;
    }
}
