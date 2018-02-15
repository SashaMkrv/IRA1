import ir.vsr.*;
import ir.utilities.StopWords;
import java.math.BigInteger;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class Lib {
    public static void main(String[] args) {
        System.out.println("hello");
        //Preprocessing
        //Tokenization + stopword removal
        //Dictionary of tweet ID + TextStringDocument?]
        Map<BigInteger, ir.vsr.TextStringDocument> tweets = new HashMap<BigInteger, ir.vsr.TextStringDocument>();
        StopWords.changeStopWordsLocation("data\\StopWords.txt");
        ir.vsr.TextStringDocument test = new ir.vsr.TextStringDocument("This is a tweet", true);
        tweets.put(new BigInteger("190000000"), test);
        tweets.get(new BigInteger(("190000000"))).printVector();
    }
}
