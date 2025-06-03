package exceptions;

public class LoadGameException extends GameException {
    public LoadGameException(String message) {
        super(message);
    }

    public static class NoSaveFileFound extends LoadGameException {
        public NoSaveFileFound() {
            super("Nenhum arquivo de salvamento encontrado.");
        }
    }
}
