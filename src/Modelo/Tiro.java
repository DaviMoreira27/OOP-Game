package Modelo;

import Auxiliar.Desenho;
import Auxiliar.Posicao;
import exceptions.EndGameException;

public class Tiro extends Personagem {
    private double posLinha;
    private double posColuna;
    private final double deltaLinha;
    private final double deltaColuna;
    private int distanciaPercorrida = 0;
    private boolean fromHero;
    private static final int DISTANCIA_MAXIMA = 5;

    public Tiro(String sNomeImagePNG, int linhaInicial, int colunaInicial, int linhaMouse, int colunaMouse, int cDano,
            int cVida, boolean imported, boolean fromHero) {
        super(sNomeImagePNG, cDano, cVida, imported);

        this.bTransponivel = true;
        this.showVida = false;
        this.posLinha = linhaInicial;
        this.posColuna = colunaInicial;
        this.fromHero = fromHero;

        double dx = colunaMouse - colunaInicial;
        double dy = linhaMouse - linhaInicial;
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        this.deltaColuna = dx / magnitude;
        this.deltaLinha = dy / magnitude;

        this.setPosicao(linhaInicial, colunaInicial);
    }

    @Override
    public void autoDesenho() {
        try {
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
                if (p != this && p.getPosicao().igual(new Posicao(novaLinha, novaColuna))) {
                    p.receberDano(this.getDano());
                    p.vida -= this.getDano();

                    if (p.vida <= 0) {
                        p.showVida = false;
                    }

                    if (p instanceof Hero && p.vida <= 0) {
                        throw new EndGameException.DeadHero();
                    }

                    if (!(p instanceof Hero)) {
                        Desenho.acessoATelaDoJogo().removePersonagem(p);
                    }

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
        } catch (EndGameException e) {
            System.out.println("Fim do jogo");
        }

    }

    public boolean isFromHero() {
        return this.fromHero;
    }
}
