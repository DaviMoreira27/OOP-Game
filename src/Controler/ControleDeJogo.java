package Controler;

import Auxiliar.Posicao;
import Modelo.Chaser;
import Modelo.Hero;
import Modelo.Personagem;
import Modelo.Tiro;
import java.util.ArrayList;

public class ControleDeJogo {

    public void desenhaTudo(ArrayList<Personagem> e) {
        for (int i = 0; i < e.size(); i++) {
            e.get(i).autoDesenho();
        }
    }

    public void processaTudo(ArrayList<Personagem> umaFase) {
        Hero hero = umaFase.stream()
                .filter(obj -> obj instanceof Hero)
                .map(obj -> (Hero) obj)
                .findFirst()
                .orElse(null);

        ArrayList<Personagem> paraRemover = new ArrayList<>();

        for (int i = 1; i < umaFase.size(); i++) {
            Personagem p = umaFase.get(i);

            // Chaser persegue o herói
            if (p instanceof Chaser) {
                ((Chaser) p).computeDirection(hero.getPosicao());
            }

            // Colisão com o herói
            if (!(p instanceof Hero) && !(p instanceof Tiro)) {
                System.out
                        .println("Posicao Hero " + hero.getPosicao().getColuna() + " " + hero.getPosicao().getLinha());

                System.out.println("Posicao Inimigo " + p.getPosicao().getColuna() + " " + p.getPosicao().getLinha());
                if (hero.getPosicao().igual(p.getPosicao())) {
                    System.out.println("Teste");
                    if (!p.isbTransponivel() && p.getDano() > 0) {
                        hero.tomarDano(p.getDano());
                    }
                }
            }
        }

        // Verificar colisão entre tiros e inimigos
        for (int i = 0; i < umaFase.size(); i++) {
            Personagem p1 = umaFase.get(i);
            if (p1 instanceof Tiro) {
                for (int j = 1; j < umaFase.size(); j++) {
                    Personagem p2 = umaFase.get(j);
                    if (p2 != p1 &&
                            !(p2 instanceof Tiro) &&
                            p1.getPosicao().igual(p2.getPosicao())) {
                        p2.tomarDano(p1.getDano());
                        paraRemover.add(p1); 
                        break;
                    }
                }
            }
        }

        for (Personagem p : umaFase) {
            if (p.getVida() <= 0) {
                paraRemover.add(p);
            }
        }

        umaFase.removeAll(paraRemover);
    }

    /*
     * Retorna true se a posicao p é válida para Hero com relacao a todos os
     * personagens no array
     */
    public boolean ehPosicaoValida(ArrayList<Personagem> umaFase, Posicao p) {
        Personagem pIesimoPersonagem;
        for (int i = 1; i < umaFase.size(); i++) {
            pIesimoPersonagem = umaFase.get(i);
            if (!pIesimoPersonagem.isbTransponivel()) {
                if (pIesimoPersonagem.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }
}
