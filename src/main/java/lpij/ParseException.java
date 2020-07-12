package lpij;

public class ParseException extends Error {
    private final int index;
    private final String strippedSource;

    public ParseException(int index, String strippedSource, String message) {
        super(message);
        this.index = index;
        this.strippedSource = strippedSource;
    }

    public int getIndex() {
        return index;
    }

    public String getStrippedSource() {
        return strippedSource;
    }
}
