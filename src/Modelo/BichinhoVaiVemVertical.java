package Modelo;

public class BichinhoVaiVemVertical extends Personagem {

    boolean bUp;

    public BichinhoVaiVemVertical(String sNomeImagePNG, int cDano, int cVida, boolean imported) {
        super(sNomeImagePNG, cDano, cVida, imported);
        bUp = true;
        this.bTransponivel = false;
    }

    public void autoDesenho() {
        if (bUp) this.setPosicao(pPosicao.getLinha() - 1, pPosicao.getColuna());
        else this.setPosicao(pPosicao.getLinha() + 1, pPosicao.getColuna());

        super.autoDesenho();
        bUp = !bUp;
    }
}
