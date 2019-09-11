package index;

public class Token {
    private String value;
    private int position;

    public Token(){}

    public Token(String value, int position){
        this.value = value;
        this.position = position;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
