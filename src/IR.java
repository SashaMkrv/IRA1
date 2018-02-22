import ir.vsr.*;
import ir.utilities.StopWords;
import ir.classifiers.Example;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IR {
    public static void main(String[] args) {
        System.out.println("Hi, I'm gonna read some tweets now");
        //Reading terms into the
        //Map<String, ir.vsr.TextStringDocument> tweets = new HashMap<String, ir.vsr.TextStringDocument>();
        List<Example> tweets = new LinkedList<Example>();
        StopWords.changeStopWordsLocation("C:\\Users\\samue\\IdeaProjects\\IRA1\\data\\StopWords.txt");
        if (tokenizeLines(tweets, "data\\Trec_microblog11.txt")) {
            System.out.println("Read " + tweets.size() + " tweets");
        } else {
            System.out.println("Failed to read tweets");
        }
        //Next step: turn our dictionary of stemmed and stopword-removed tweets into an inverted index.
        InvertedIndex index = new InvertedIndex(tweets);
        System.out.println("Successfully added " + index.size() + " terms to an inverted index.");
    }

    private static boolean tokenizeLines(List<Example> tweets, String dataPath) {
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
                //Create a document with this tweet, tokenizing its contents by stemming and removing stopwords
                TextStringDocument newDoc = new TextStringDocument(tuple[1], true);
                //Use this document to create an example object with a vector of its tokens, retaining its ID
                Example newExample = new Example(newDoc.hashMapVector(), 0, tuple[0], newDoc);
                //Add this example to this list of tweets
                tweets.add(newExample);
            }
        } catch (IOException e) {
            System.out.println(e.toString() + ": Error while reading file on line " + lineCount);
            return false;
        }
        return true;
    }
}
