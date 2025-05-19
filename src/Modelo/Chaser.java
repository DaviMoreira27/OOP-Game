/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Auxiliar.*;

/**
 *
 * @author 2373891
 */
public class Chaser extends Personagem {

    private boolean iDirectionV;
    private boolean iDirectionH;

    public Chaser(String sNomeImagePNG, int cDano, int cVida) {
        super(sNomeImagePNG, cDano, cVida);
        iDirectionV = true;
        iDirectionH = true;
        this.bTransponivel = true;
    }

    public void computeDirection(Posicao heroPos) {
        int chaserX = this.getPosicao().getColuna() * Consts.CELL_SIDE;
        int chaserY = this.getPosicao().getLinha() * Consts.CELL_SIDE;
        int heroX = heroPos.getColuna() * Consts.CELL_SIDE;
        int heroY = heroPos.getLinha() * Consts.CELL_SIDE;

        int distX = Math.abs(heroX - chaserX);
        int distY = Math.abs(heroY - chaserY);

        if (distX > 50) {
            iDirectionH = heroX < chaserX;
        } else {
            iDirectionH = false;
        }

        if (distY > 50) {
            iDirectionV = heroY < chaserY;
        } else {
            iDirectionV = false;
        }
    }

    public void autoDesenho() {
        super.autoDesenho();

        if (iDirectionH) {
            this.moveLeft();
        } else if (!iDirectionH && Math.abs(pPosicao.getColuna() * Consts.CELL_SIDE - Consts.CELL_SIDE) > 50) {
            this.moveRight();
        }

        if (iDirectionV) {
            this.moveUp();
        } else if (!iDirectionV && Math.abs(pPosicao.getLinha() * Consts.CELL_SIDE - Consts.CELL_SIDE) > 50) {
            this.moveDown();
        }
    }
}
