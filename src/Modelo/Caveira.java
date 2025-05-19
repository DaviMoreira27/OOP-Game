package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;

public class Caveira extends Personagem{
    private int iContaIntervalos;
    
    public Caveira(String sNomeImagePNG, int cDano, int cVida) {
        super(sNomeImagePNG, cDano, cVida);
        bMortal = false;
        this.iContaIntervalos = 0;
    }

    public void autoDesenho() {
        super.autoDesenho();

        this.iContaIntervalos++;
        if(this.iContaIntervalos == Consts.TIMER){
            this.iContaIntervalos = 0;
            Fogo f = new Fogo("fire.png", 5, 1);
            f.setPosicao(pPosicao.getLinha(),pPosicao.getColuna()+1);
            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }    
}
