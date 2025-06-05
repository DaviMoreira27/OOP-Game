package exceptions;

public class EndGameException extends GameException {
    public EndGameException(String message) {
        super(message);
    }

    public static class DeadHero extends EndGameException {
        public DeadHero() {
            super("Voce Perdeu!");
        }
    }
}
