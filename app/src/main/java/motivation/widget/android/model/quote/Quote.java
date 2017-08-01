package motivation.widget.android.model.quote;

public class Quote {
    private final int index;
    private final String text;
    private final String author;

    Quote(int index, String text, String author) {

        this.index = index;
        this.text = text;
        this.author = author;
    }

    public int getIndex() {
        return index;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }
}
