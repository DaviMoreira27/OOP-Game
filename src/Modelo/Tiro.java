package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Posicao;

public class Tiro extends Personagem {
    private double posLinha;
    private double posColuna;
    private final double deltaLinha;
    private final double deltaColuna;
    private int distanciaPercorrida = 0;
    private static final int DISTANCIA_MAXIMA = 5;

    public Tiro(String sNomeImagePNG, int linhaInicial, int colunaInicial, int linhaMouse, int colunaMouse, int cDano,
            int cVida, boolean imported) {
        super(sNomeImagePNG, cDano, cVida, imported);

        this.bTransponivel = true;
        this.showVida = false;
        this.posLinha = linhaInicial;
        this.posColuna = colunaInicial;

        double dx = colunaMouse - colunaInicial;
        double dy = linhaMouse - linhaInicial;
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        this.deltaColuna = dx / magnitude;
        this.deltaLinha = dy / magnitude;

        this.setPosicao(linhaInicial, colunaInicial);
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();

        if (distanciaPercorrida >= DISTANCIA_MAXIMA) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            return;
        }

        posLinha += deltaLinha;
        posColuna += deltaColuna;

        int novaLinha = (int) Math.round(posLinha);
        int novaColuna = (int) Math.round(posColuna);

        if (Desenho.acessoATelaDoJogo().ehParede(novaLinha, novaColuna)) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            return;
        }

        for (Personagem p : Desenho.acessoATelaDoJogo().getFaseAtual()) {
            if (p != this && !(p instanceof Hero) && p.getPosicao().igual(new Posicao(novaLinha, novaColuna))) {
                p.receberDano(this.getDano());
                Desenho.acessoATelaDoJogo().removePersonagem(this);
                return;
            }
        }

        boolean podeMover = this.setPosicao(novaLinha, novaColuna);
        if (!podeMover) {
            Desenho.acessoATelaDoJogo().removePersonagem(this);
            return;
        }

        distanciaPercorrida++;
    }
}
