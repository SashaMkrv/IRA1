import ir.vsr.*;
import ir.utilities.StopWords;

import java.io.*;
import java.math.BigInteger;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class IR {
    public static void main(String[] args) {
        System.out.println("Hi, I'm gonna read some tweets now");
        //Reading terms into the
        Map<String, ir.vsr.TextStringDocument> tweets = new HashMap<String, ir.vsr.TextStringDocument>();
        StopWords.changeStopWordsLocation("data\\StopWords.txt");
        if (tokenizeLines(tweets, "data\\Trec_microblog11.txt")) {
            System.out.println("Read " + tweets.size() + " tweets");
        } else {
            System.out.println("Failed to read tweets");
        }
        //Next step: turn our dictionary of stemmed and stopword-removed tweets into an inverted index.
    }

    private static boolean tokenizeLines(Map<String, ir.vsr.TextStringDocument> map, String dataPath) {
        BufferedReader dataReader = null;
        String[] tuple;
        int lineCount = 0;
        try {
            dataReader = new BufferedReader(new FileReader(dataPath));
        } catch (FileNotFoundException e) {
            System.out.println(e.toString() + ": Could not read data file " + dataPath);
            return false;
        }
        try {
            for (String nextLine = dataReader.readLine(); nextLine != null; nextLine = dataReader.readLine()) {
                lineCount++;
                tuple = nextLine.split("\t");
                map.put(tuple[0], new TextStringDocument(tuple[1], true));
            }
        } catch (IOException e) {
            System.out.println(e.toString() + ": Error while reading file on line " + lineCount);
            return false;
        }
        return true;
    }
}
