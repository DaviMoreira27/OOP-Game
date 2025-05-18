package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;

public class Caveira extends Personagem{
    private int iContaIntervalos;
    
    public Caveira(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bMortal = false;
        this.iContaIntervalos = 0;
        this.vida = 3;
        this.dano = 1;
    }

    public void autoDesenho() {
        super.autoDesenho();

        this.iContaIntervalos++;
        if(this.iContaIntervalos == Consts.TIMER){
            this.iContaIntervalos = 0;
            Fogo f = new Fogo("fire.png");
            f.setPosicao(pPosicao.getLinha(),pPosicao.getColuna()+1);
            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }    
}
