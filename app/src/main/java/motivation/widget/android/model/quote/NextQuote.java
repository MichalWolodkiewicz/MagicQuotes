package motivation.widget.android.model.quote;


public class NextQuote {

    NextQuote(String quote, String author, int index, long createTime) {
        this.quote = quote;
        this.author = author;
        this.index = index;
        this.createTime = createTime;
    }

    private String quote;
    private String author;
    private int index;
    private long createTime;

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }

    public int getIndex() {
        return index;
    }

    public long getCreateTime() {
        return createTime;
    }
}
