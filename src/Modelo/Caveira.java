package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;

public class Caveira extends Personagem {

    private int iContaIntervalos;

    public Caveira(
        String sNomeImagePNG,
        int cDano,
        int cVida,
        boolean imported
    ) {
        super(sNomeImagePNG, cDano, cVida, imported);
        bMortal = false;
        this.iContaIntervalos = 0;
        this.bTransponivel = false;
    }

    public void autoDesenho() {
        super.autoDesenho();

        this.iContaIntervalos++;
        if (this.iContaIntervalos == Consts.TIMER) {
            this.iContaIntervalos = 0;
            Fogo f = new Fogo("bala.png", 5, 1, false);
            f.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
            Desenho.acessoATelaDoJogo().addPersonagem(f);
        }
    }
}
