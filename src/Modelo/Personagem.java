package Modelo;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Posicao;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public abstract class Personagem implements Serializable {

    protected ImageIcon iImage;
    protected Posicao pPosicao;
    protected boolean bTransponivel; /*Pode passar por cima?*/
    protected boolean bMortal;       /*Se encostar, morre?*/
    protected int vida;
    protected int dano;
    protected boolean showVida = true;
    protected String imagePath;
    protected boolean imported;

    protected Personagem(String sNomeImagePNG, int cDano, int cVida, boolean imported) {
        this.pPosicao = new Posicao(1, 1);
        /*
         * Acontece um erro se a imagem do personagem e a de um inimigo ficam uma sobre
         * a outra
         * 
         * Exception in thread "AWT-EventQueue-0" java.lang.ClassCastException: class
         * Modelo.ZigueZague cannot be cast to class Modelo.Hero (Modelo.ZigueZague and
         * Modelo.Hero are in unnamed module of loader 'app')
         * FIX ME
         */
        this.bTransponivel = true;
        this.bMortal = false;
        this.vida = cVida;
        this.dano = cDano;
        this.imagePath = sNomeImagePNG;
        this.imported = imported;

        try {
            if (!imported) {
                iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + sNomeImagePNG);
                Image img = iImage.getImage();
                BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.createGraphics();
                g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                iImage = new ImageIcon(bi);
                return;
            }

            boolean getFile = new File(sNomeImagePNG).exists();
            System.out.println(getFile);

            if (!getFile) {
                iImage = this.getAssociatedImage(this.getClass().getSimpleName());
                Image img = iImage.getImage();
                BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.createGraphics();
                g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                iImage = new ImageIcon(bi);
                System.out.println(iImage.getImageLoadStatus());
                return;
            }

            iImage = new ImageIcon(sNomeImagePNG);
            Image img = iImage.getImage();
            BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
            iImage = new ImageIcon(bi);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected ImageIcon getAssociatedImage (String childClass) throws IOException, ClassNotFoundException {
        try {
            switch (childClass) {
                case "ZigueZague":
                    return new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "skoot.png");
                case "Hero":
                    return new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "Robbo.png");
                case "BichinhoVaiVemHorizontal":
                    return new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "roboPink.png");
                case "BichinhoVaiVemVertical":
                    return new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "skoot.png");
                case "Caveira":
                    return new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "caveira.png");
                case "Chaser":
                    return new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + "Chaser.png");
                default:
                    throw new ClassNotFoundException();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
    

    public boolean isbMortal() {
        return bMortal;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getVida() {
        return this.vida;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public int getDano() {
        return this.dano;
    }

    public void tomarDano(int valor) {
        this.vida -= valor;
        if (this.vida <= 0) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            this.showVida = false;
        }
    }

    public Posicao getPosicao() {
        /*TODO: Retirar este método para que objetos externos nao possam operar
         diretamente sobre a posição do Personagem*/
        return pPosicao;
    }

    public boolean isbTransponivel() {
        return bTransponivel;
    }

    public ImageIcon getImage() {
        return this.iImage;
    }

    public void setbTransponivel(boolean bTransponivel) {
        this.bTransponivel = bTransponivel;
    }

    public void autoDesenho(){
        Desenho.desenhar(this.iImage, this.pPosicao.getColuna(), this.pPosicao.getLinha()); 

        if (this.showVida) {
            Graphics g = Desenho.acessoATelaDoJogo().getGraphicsBuffer();
            int cameraX = Desenho.acessoATelaDoJogo().getCameraColuna() * Consts.CELL_SIDE;
            int cameraY = Desenho.acessoATelaDoJogo().getCameraLinha() * Consts.CELL_SIDE;
            
            int x = this.pPosicao.getColuna() * Consts.CELL_SIDE - cameraX;
            int y = this.pPosicao.getLinha() * Consts.CELL_SIDE - cameraY + Consts.CELL_SIDE;

            g.setColor(Color.YELLOW); // fundo da barra
            g.fillRect(x, y, Consts.CELL_SIDE, 4);

            g.setColor(Color.GREEN); // barra proporcional à vida
            int vidaBarra = (int) ((this.vida / 10.0) * Consts.CELL_SIDE);
            g.fillRect(x, y, vidaBarra, 8);
        }
    }

    public boolean setPosicao(int linha, int coluna) {
        return pPosicao.setPosicao(linha, coluna);
    }

    public boolean moveUp() {
        return this.pPosicao.moveUp();
    }

    public boolean moveDown() {
        return this.pPosicao.moveDown();
    }

    public boolean moveRight() {
        return this.pPosicao.moveRight();
    }

    public boolean moveLeft() {
        return this.pPosicao.moveLeft();
    }
}
