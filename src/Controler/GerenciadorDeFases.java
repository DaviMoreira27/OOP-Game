package Controler;

import Modelo.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class GerenciadorDeFases {
    private int[][] mapa;
    private ArrayList<Personagem> faseAtual = new ArrayList<>();
    private int faseAtualNumero;

    public GerenciadorDeFases(int faseInicial) {
        try {
            this.faseAtualNumero = faseInicial;
            carregarFaseAtual();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void carregarProximaFase(Hero hero) {
        faseAtualNumero++;
        if (faseAtualNumero == 6) {
            System.out.println("Parabéns! Você concluiu o jogo!");
            return;
        }
        carregarFaseAtual();
        hero.setPosicao(0, 7);
        faseAtual.add(hero);
    }

    private void carregarFaseAtual() {
        faseAtual.clear();
        try {
            carregarMapa("mapas/fase" + faseAtualNumero + ".txt");
            for (int linha = 0; linha < mapa.length; linha++) {
                for (int coluna = 0; coluna < mapa[linha].length; coluna++) {
                    switch (mapa[linha][coluna]) {
                        case 2:
                            // ZigueZague inimigo = new ZigueZague("robo.png", 60, 15, false);
                            // inimigo.setPosicao(linha, coluna);
                            // faseAtual.add(inimigo);
                            System.out.println("Teste");
                            break;
                        // outros personagens
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar fase: " + e.getMessage());
        }
    }

    private void carregarMapa(String caminho) throws IOException {
        try {
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
            this.mapa = linhas.toArray(new int[0][]);
            System.out.println(mapa);
        } catch (Exception e) {
            System.out.println("Nao foi possivel carregar corretamente o mapa: " + e.getMessage());
        }
        
    }

    public ArrayList<Personagem> getFaseAtual() {
        return faseAtual;
    }

    public int[][] getMapa() {
        return mapa;
    }
}
