import java.util.Scanner;

import Controler.Tela;

public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Tela tTela = new Tela(mostrarMenuFases());
                tTela.setVisible(true);
                tTela.setResizable(true);
                tTela.setAlwaysOnTop(true);
                tTela.createBufferStrategy(2);
                tTela.go();
            }
        });
    }
    private static int mostrarMenuFases() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha a fase para começar (1 a 5): ");
        int fase = 1;
        try {
            fase = Integer.parseInt(scanner.nextLine());
            if (fase < 1 || fase > 5) fase = 1;
        } catch (Exception e) {
            fase = 1;
        }
        scanner.close();
        return fase;
    }
}

