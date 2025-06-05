package Modelo;

import Auxiliar.Desenho;

public class ZigueZague extends Personagem {

    private boolean moveHorizontal = true;
    private int contadorTiro = 0;

    public ZigueZague(String sNomeImagePNG, int cDano, int cVida, boolean imported) {
        super(sNomeImagePNG, cDano, cVida, imported);
        this.bTransponivel = false;
    }

    @Override
    public void autoDesenho() {
        Hero heroi = Desenho.acessoATelaDoJogo().getHero();
        if (heroi == null)
            return;

        int linhaHeroi = heroi.getPosicao().getLinha();
        int colunaHeroi = heroi.getPosicao().getColuna();

        int linhaAtual = pPosicao.getLinha();
        int colunaAtual = pPosicao.getColuna();

        int distanciaLinha = Math.abs(linhaHeroi - linhaAtual);
        int distanciaColuna = Math.abs(colunaHeroi - colunaAtual);

        if (distanciaLinha > 1 || distanciaColuna > 1) {
            if (moveHorizontal) {
                if (colunaHeroi > colunaAtual)
                    this.setPosicao(linhaAtual, colunaAtual + 1);
                else if (colunaHeroi < colunaAtual)
                    this.setPosicao(linhaAtual, colunaAtual - 1);
            } else {
                if (linhaHeroi > linhaAtual)
                    this.setPosicao(linhaAtual + 1, colunaAtual);
                else if (linhaHeroi < linhaAtual)
                    this.setPosicao(linhaAtual - 1, colunaAtual);
            }
        
            moveHorizontal = !moveHorizontal;
        }
        

        moveHorizontal = !moveHorizontal;

        contadorTiro++;
        if (contadorTiro >= 30) {
            contadorTiro = 0;

            Tiro tiro = new Tiro("bala.png", linhaAtual, colunaAtual, linhaHeroi, colunaHeroi, 10, 1, false);
            Desenho.acessoATelaDoJogo().addPersonagem(tiro);
        }

        super.autoDesenho();
    }
}