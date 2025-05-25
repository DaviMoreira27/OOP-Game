package Modelo;

import Auxiliar.Desenho;

public class Fogo extends Personagem{
            
    public Fogo(String sNomeImagePNG, int cDano, int cVida, boolean imported) {
        super(sNomeImagePNG, cDano, cVida, imported);
        this.bMortal = true;
        this.showVida = false;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        if(!this.moveRight())
            Desenho.acessoATelaDoJogo().removePersonagem(this);
    }
    
}
