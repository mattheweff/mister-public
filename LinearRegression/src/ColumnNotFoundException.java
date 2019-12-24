public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException() {
        super("Columns Not Found.");
    }

    public ColumnNotFoundException(String message) {
        super(message);
    }
}
