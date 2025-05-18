package Modelo;

import Auxiliar.Desenho;

public class Fogo extends Personagem{
            
    public Fogo(String sNomeImagePNG) {
        super(sNomeImagePNG);
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
