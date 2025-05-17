package Modelo;

import Auxiliar.Desenho;
import types.EnumDirecao;

public class Tiro extends Personagem {
    private EnumDirecao direcao;
    private int distanciaPercorrida = 0;
    private static int DISTANCIA_MAXIMA = 5;

    public Tiro(String sNomeImagePNG, int linha, int coluna, EnumDirecao direcao) {
        super(sNomeImagePNG);
        this.direcao = direcao;
        this.setPosicao(linha, coluna);
        this.bTransponivel = true;
    }

    public void autoDesenho() {
        super.autoDesenho();

        if (distanciaPercorrida >= DISTANCIA_MAXIMA) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            return;
        }

        boolean vivo = true;
        switch (direcao) {
            case DIREITA: vivo = this.moveRight(); break;
            case ESQUERDA: vivo = this.moveLeft(); break;
            case CIMA: vivo = this.moveUp(); break;
            case BAIXO: vivo = this.moveDown(); break;
        }

        distanciaPercorrida++;
        if (!vivo) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
        }
    }
}
