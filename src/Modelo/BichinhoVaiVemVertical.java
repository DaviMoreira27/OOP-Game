
package Modelo;

public class BichinhoVaiVemVertical extends Personagem{
    boolean bUp;
    public BichinhoVaiVemVertical(String sNomeImagePNG) {
        super(sNomeImagePNG);
        bUp = true;
        this.vida = 3;
        this.dano = 1;
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
