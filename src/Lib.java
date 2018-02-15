import ir.vsr.*;
import java.math.BigInteger;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Lib {
    public static void main(String[] args) {
        System.out.println("hello");
        //Preprocessing
        //Tokenization + stopword removal
        //Dictionary of tweet ID + TextStringDocument?
        Map<BigInteger, ir.vsr.TextStringDocument> tweets = new HashMap<BigInteger, ir.vsr.TextStringDocument>();
        ir.vsr.TextStringDocument
        test.changeStopWordsLocation("C:\\Users\\samue\\IdeaProjects\\IRA1\\data\\StopWords.txt");
        ir.vsr.TextStringDocument test2 = new ir.vsr.TextStringDocument("This is a tweet", true);
        tweets.put(new BigInteger("190000000"), test2);
        tweets.get(new BigInteger(("190000000"))).printVector();
    }
}
