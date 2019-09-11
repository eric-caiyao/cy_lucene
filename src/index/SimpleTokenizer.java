package index;

public class SimpleTokenizer implements Tokenizer{

    private String sourceString;
    private int currentPosition = 0;
    private String stopWord = " ";

    public SimpleTokenizer(){}

    public SimpleTokenizer(String sourceString){
        this.sourceString = sourceString;
    }

    @Override
    public Token nextToken() {
       Token tmpToken = new Token(sourceString.split(stopWord)[currentPosition],currentPosition);
       currentPosition ++;
        return tmpToken;
    }

    @Override
    public boolean hasNext() {
        return sourceString.split(stopWord).length > currentPosition;
    }
}
