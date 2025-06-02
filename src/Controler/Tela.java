package Controler;

import Modelo.Personagem;
import Modelo.PersonagemDTO;
import Modelo.PersonagemIncompleto;
import Modelo.Tiro;
import Modelo.Hero;
import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.ZigueZague;
import Auxiliar.Posicao;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    private ArrayList<Personagem> faseAtual;
    private ControleDeJogo cj = new ControleDeJogo();
    private Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    private boolean jogoPausado = false;
    private JPanel menuPausa;

private int[][] mapa;

private int faseAtualNumero = 1;

private void carregarMapa(String caminho) throws IOException {
    List<int[]> linhas = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
        String linha;
        while ((linha = br.readLine()) != null) {
            String[] partes = linha.trim().split("");
            int[] valores = new int[partes.length];
            for (int i = 0; i < partes.length; i++) {
                valores[i] = Integer.parseInt(partes[i]);
            }
            linhas.add(valores);
        }
    }
    mapa = linhas.toArray(new int[0][]);
}

public void carregarProximaFase() {
    faseAtualNumero++;
    if(faseAtualNumero == 6){
        System.out.println("Parabens! Voce concluiu nosso jogo!");
        return;
    }
    String nomeMapa = "mapas/fase" + faseAtualNumero + ".txt";
    try {
        carregarMapa(nomeMapa);
        faseAtual.clear();

        // Adiciona personagens da nova fase
        for (int linha = 0; linha < mapa.length; linha++) {
            for (int coluna = 0; coluna < mapa[linha].length; coluna++) {
                switch (mapa[linha][coluna]) {
                    case 2:
                        ZigueZague inimigo = new ZigueZague("robo.png", 60, 15, false);
                        inimigo.setPosicao(linha, coluna);
                        this.addPersonagem(inimigo);
                        break;
                    // Adicione outros tipos de inimigos se quiser
                }
            }
        }
        // Reposiciona o herói no início da nova fase
        hero.setPosicao(0, 7);
        this.addPersonagem(hero);

        atualizaCamera();
        repaint();
    }catch(IOException e){
        System.out.println("");
    }
}

private void checarFimDaFase() {
    long inimigosRestantes = faseAtual.stream()
        .filter(p -> !(p instanceof Hero))
        .count();
    if (inimigosRestantes == 0) {
        carregarProximaFase();
    }
}

    public Tela(int faseInicial) {
        Desenho.setCenario(this);
        faseAtualNumero = faseInicial;
        try {
            carregarMapa("mapas/fase" + faseInicial + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        faseAtual = new ArrayList<>();

        for (int linha = 0; linha < mapa.length; linha++) {
            for (int coluna = 0; coluna < mapa[linha].length; coluna++) {
                switch (mapa[linha][coluna]) {
                    case 2:
                        ZigueZague inimigo = new ZigueZague("robo.png", 60, 15, false);
                        inimigo.setPosicao(linha, coluna);
                        this.addPersonagem(inimigo);
                        break;
                    case 3:
                        // Aqui você pode marcar a sala final ou criar um objeto especial
                        break;
                    // Adicione outros tipos conforme necessário
                }
            }
        }
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(null);
        this.addMouseListener(this);
        this.addKeyListener(this);

        new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent fileDrop) {
                try {
                    Point mousePosition = fileDrop.getLocation();
                    System.out.println("Arquivo solto na posição: " + mousePosition);

                    fileDrop.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = fileDrop.getTransferable();

                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        @SuppressWarnings("unchecked")
                        List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        for (File file : files) {
                            System.out.println("Arquivo: " + file.getAbsolutePath());

                            int colunaMouse = (int) mousePosition.getX() / Consts.CELL_SIDE;
                            int linhaMouse = (int) mousePosition.getY() / Consts.CELL_SIDE;
                            Posicao p = new Posicao(linhaMouse, colunaMouse);

                            Tela.this.loadCharacter(file, p);
                        }
                    }

                    fileDrop.dropComplete(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    fileDrop.dropComplete(false);
                }
            }
        });

        hero = new Hero("Robbo.png", 100, 10, false);
        hero.setPosicao(0, 7);
        this.addPersonagem(hero);

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
        btnCarregar.addActionListener(e -> this.loadGame());
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

    private void loadGame() {
        this.faseAtual = SaveHandler.carregarJogo();

        for (Personagem p : faseAtual) {
            System.out.println(p.getClass());
            System.out.println(p.getPosicao().getColuna());
            System.out.println(p.getPosicao().getLinha());
            System.out.print("\n");
            if (p.getClass() == Hero.class) {
                this.hero = (Hero) p;
            }
        }

        this.cj.desenhaTudo(faseAtual);
        this.cj.processaTudo(faseAtual);
        this.atualizaCamera();
        repaint();
    }

    // TODO: Refactor this method, its horrible
    private void loadCharacter(File file, Posicao position) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipInputStream zis = new ZipInputStream(fis, StandardCharsets.UTF_8);
        ZipEntry entry;
        StringBuilder jsonBuilder = new StringBuilder();
        String imagePath = null;

        while ((entry = zis.getNextEntry()) != null) {
            String entryName = entry.getName();
            System.out.println(entryName);

            if (entryName.endsWith("personagem.json")) {
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }
                String jsonContent = baos.toString(StandardCharsets.UTF_8);
                jsonBuilder.append(jsonContent);
                baos.close();
            } else if (entryName.toLowerCase().matches(".*\\.(png|jpg|jpeg)")) {
                File tempDir = new File(System.getProperty("java.io.tmpdir"));
                File extractedImage = new File(tempDir, new File(entryName).getName());
                try (FileOutputStream fos = new FileOutputStream(extractedImage)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                imagePath = extractedImage.getAbsolutePath();
            }
            zis.closeEntry();
        }
        zis.close();

        System.out.println("JSON lido: " + jsonBuilder.toString());
        String json = jsonBuilder.toString().trim();

        if (json.isEmpty()) {
            throw new IOException("Arquivo personagem.json não encontrado ou está vazio.");
        }

        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        String classe = obj.get("classe").getAsString();
        int vida = obj.get("vida").getAsInt();
        int dano = obj.get("dano").getAsInt();
        boolean mortal = obj.get("mortal").getAsBoolean();
        boolean transponivel = obj.get("transponivel").getAsBoolean();

        PersonagemIncompleto personagem = new PersonagemIncompleto(dano, vida, transponivel, mortal, classe);
        PersonagemDTO dto = new PersonagemDTO(personagem, position, imagePath);
        Personagem p = SaveHandler.criarPersonagemFromDTO(dto);
        this.faseAtual.add(p);
        this.cj.desenhaTudo(faseAtual);
        this.cj.processaTudo(faseAtual);
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

    public List<Personagem> getFaseAtual() {
        return this.faseAtual;
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
                if (mapaLinha < mapa.length && mapaColuna < mapa[0].length) {
                    try {
                        if (mapa[mapaLinha][mapaColuna] == 1) {
                            // desenhe parede
                            Image parede = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "bricks.png");
                            g2.drawImage(parede, j * Consts.CELL_SIDE, i * Consts.CELL_SIDE, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                        } else {
                            // desenhe chão
                            Image chao = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "blackTile.png");
                            g2.drawImage(chao, j * Consts.CELL_SIDE, i * Consts.CELL_SIDE, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (!this.faseAtual.isEmpty()) {
            this.cj.desenhaTudo(faseAtual);
            this.cj.processaTudo(faseAtual);
        }

        checarFimDaFase();

        if (jogoPausado) {
            atualizarMenuPausa();
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    public boolean ehParede(int linha, int coluna) {
        return mapa[linha][coluna] == 1;
    }

    public Hero getHero() {
        return hero;
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
            int novaLinha = hero.getPosicao().getLinha();
            int novaColuna = hero.getPosicao().getColuna();

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                novaLinha--;
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                novaLinha++;
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                novaColuna--;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                novaColuna++;
            }

            if (novaLinha >= 0 && novaLinha < mapa.length &&
                novaColuna >= 0 && novaColuna < mapa[0].length &&
                !ehParede(novaLinha, novaColuna)) {
                hero.setPosicao(novaLinha, novaColuna);
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

        Tiro tiro = new Tiro("fire.png", linhaHeroi, colunaHeroi, linhaMouse, colunaMouse, 10, 1, false);
        Desenho.acessoATelaDoJogo().addPersonagem(tiro);

        repaint();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAutoRequestFocus(false);

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
