
package Modelo;

public class BichinhoVaiVemVertical extends Personagem{
    boolean bUp;
    public BichinhoVaiVemVertical(String sNomeImagePNG, int cDano, int cVida) {
        super(sNomeImagePNG, cDano, cVida);
        bUp = true;
    }

    public void autoDesenho(){
        if(bUp)
            this.setPosicao(pPosicao.getLinha()-1, pPosicao.getColuna());
        else
            this.setPosicao(pPosicao.getLinha()+1, pPosicao.getColuna());           

        super.autoDesenho();
        bUp = !bUp;
    }  
}
