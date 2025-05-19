package Controler;

import Modelo.Personagem;
import Modelo.Tiro;
import Modelo.Caveira;
import Modelo.Hero;
import Modelo.Chaser;
import Modelo.BichinhoVaiVemHorizontal;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.BichinhoVaiVemVertical;
import Modelo.ZigueZague;
import Auxiliar.Posicao;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private boolean jogoPausado = false;
    private JPanel menuPausa;

    public Tela() {
        Desenho.setCenario(this);
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(null);
        this.addMouseListener(this);
        this.addKeyListener(this);

        faseAtual = new ArrayList<>();

        hero = new Hero("Robbo.png", 100, 10);
        hero.setPosicao(0, 7);
        this.addPersonagem(hero);

        ZigueZague zz = new ZigueZague("robo.png", 60, 15);
        zz.setPosicao(5, 5);
        this.addPersonagem(zz);

        BichinhoVaiVemHorizontal bBichinhoH = new BichinhoVaiVemHorizontal("roboPink.png", 40, 8);
        bBichinhoH.setPosicao(3, 3);
        this.addPersonagem(bBichinhoH);

        BichinhoVaiVemHorizontal bBichinhoH2 = new BichinhoVaiVemHorizontal("roboPink.png", 40, 8);
        bBichinhoH2.setPosicao(6, 6);
        this.addPersonagem(bBichinhoH2);

        BichinhoVaiVemVertical bVv = new BichinhoVaiVemVertical("caveira.png", 50, 12);
        bVv.setPosicao(10, 10);
        this.addPersonagem(bVv);

        Caveira bV = new Caveira("caveira.png", 70, 20);
        bV.setPosicao(9, 1);
        this.addPersonagem(bV);

        Chaser chase = new Chaser("Chaser.png", 80, 25);
        chase.setPosicao(12, 12);
        this.addPersonagem(chase);

        initMenuPausa();
        atualizaCamera();
    }

    private void initMenuPausa() {
        menuPausa = new JPanel();
        atualizarMenuPausa();
        menuPausa.setLayout(new GridLayout(4, 1));
        menuPausa.setBackground(new Color(0, 0, 0, 200));
        menuPausa.setVisible(false);

        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setFont(new Font(getName(), Font.BOLD, getHeight() / 20));
        btnContinuar.addActionListener(e -> {
            jogoPausado = false;
            menuPausa.setVisible(false);
            this.requestFocusInWindow();
            this.paint(g2);
            this.toFront();
        });

        JButton btnSalvar = new JButton("Salvar jogo");
        btnSalvar.addActionListener(e -> SaveHandler.salvarJogo(this.faseAtual));
        btnSalvar.setFont(new Font(getName(), Font.BOLD, getHeight() / 20));

        JButton btnCarregar = new JButton("Carregar jogo");
        btnCarregar.addActionListener(e -> System.out.println("Jogo carregado"));
        btnCarregar.setFont(new Font(getName(), Font.BOLD, getHeight() / 20));

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> System.exit(0));
        btnSair.setFont(new Font(getName(), Font.BOLD, getHeight() / 20));

        menuPausa.add(btnContinuar);
        menuPausa.add(btnSalvar);
        menuPausa.add(btnCarregar);
        menuPausa.add(btnSair);

        this.add(menuPausa);
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public boolean ehPosicaoValida(Posicao p) {
        return cj.ehPosicaoValida(this.faseAtual, p);
    }

    public void addPersonagem(Personagem umPersonagem) {
        faseAtual.add(umPersonagem);
    }

    public void removePersonagem(Personagem umPersonagem) {
        faseAtual.remove(umPersonagem);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    public void paint(Graphics gOld) {
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        int linhasVisiveis = getHeight() / Consts.CELL_SIDE;
        int colunasVisiveis = getWidth() / Consts.CELL_SIDE;

        for (int i = 0; i < linhasVisiveis; i++) {
            for (int j = 0; j < colunasVisiveis; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < Consts.MUNDO_ALTURA && mapaColuna < Consts.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "bricks.png");
                        g2.drawImage(newImage, j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual);
        }

        if (jogoPausado) {
            atualizarMenuPausa();
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    private void atualizarMenuPausa() {
        int largura = getWidth() / 3;
        int altura = getHeight() / 3;
        int posX = (getWidth() - largura) / 2;
        int posY = (getHeight() - altura) / 2;

        menuPausa.setBounds(posX, posY, largura, altura);
        menuPausa.setVisible(true);
        menuPausa.revalidate();
        menuPausa.repaint();
    }

    private void atualizaCamera() {
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();
        int linhasVisiveis = getHeight() / Consts.CELL_SIDE;
        int colunasVisiveis = getWidth() / Consts.CELL_SIDE;

        cameraLinha = Math.max(0, Math.min(linha - linhasVisiveis / 2, Consts.MUNDO_ALTURA - linhasVisiveis));
        cameraColuna = Math.max(0, Math.min(coluna - colunasVisiveis / 2, Consts.MUNDO_LARGURA - colunasVisiveis));
    }

    public void go() {
        TimerTask task = new TimerTask() {
            public void run() {
                if (!jogoPausado) {
                    repaint();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.PERIOD);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            jogoPausado = !jogoPausado;
            menuPausa.setVisible(jogoPausado);
            return;
        }

        if (!jogoPausado) {
            if (e.getKeyCode() == KeyEvent.VK_C) {
                this.faseAtual.clear();
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                hero.moveUp();
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                hero.moveDown();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                hero.moveLeft();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                hero.moveRight();
            }

            this.atualizaCamera();
            this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                    + (hero.getPosicao().getLinha()));
        }
    }

    public void mousePressed(MouseEvent e) {
        int linhaHeroi = hero.getPosicao().getLinha();
        int colunaHeroi = hero.getPosicao().getColuna();

        int colunaMouse = e.getX() / Consts.CELL_SIDE;
        int linhaMouse = e.getY() / Consts.CELL_SIDE;

        Tiro tiro = new Tiro("fire.png", linhaHeroi, colunaHeroi, linhaMouse, colunaMouse, 10, 1);
        Desenho.acessoATelaDoJogo().addPersonagem(tiro);

        repaint();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 561, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 500, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents
     // Variables declaration - do not modify//GEN-BEGIN:variables
     // End of variables declaration//GEN-END:variables

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
