package Modelo;

public class BichinhoVaiVemHorizontal extends Personagem {

    private boolean bRight;
    int iContador;

    public BichinhoVaiVemHorizontal(String sNomeImagePNG, int cDano, int cVida) {
        super(sNomeImagePNG, cDano, cVida);
        bRight = true;
        iContador = 0;
    }

    public void autoDesenho() {
        if (iContador == 5) {
            iContador = 0;
            if (bRight) {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
            } else {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
            }

            bRight = !bRight;
        }
        super.autoDesenho();
        iContador++;
    }
}
