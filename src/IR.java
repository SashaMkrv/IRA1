import ir.utilities.Weight;
import ir.vsr.*;
import ir.utilities.StopWords;
import ir.classifiers.Example;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

import org.w3c.dom.*;
import org.w3c.dom.Document;

import javax.xml.parsers.*;
import java.io.*;

public class IR {
    public static void main(String[] args) {
        System.out.println("Hi, I'm gonna read some tweets now");
        //Reading terms into the
        //Map<String, ir.vsr.TextStringDocument> tweets = new HashMap<String, ir.vsr.TextStringDocument>();
        List<Example> tweets = new LinkedList<Example>();
        StopWords.changeStopWordsLocation("data\\StopWords.txt");
        if (tokenizeLines(tweets, "data\\Trec_microblog11.txt")) {
            System.out.println("Read " + tweets.size() + " tweets");
        } else {
            System.out.println("Failed to read tweets");
        }
        //Next step: turn our dictionary of stemmed and stopword-removed tweets into an inverted index.
        InvertedIndex index = new InvertedIndex(tweets);
        System.out.println("Successfully added " + index.size() + " terms to an inverted index.");



        index.processQueries();
//        TextStringDocument query = new TextStringDocument("hello america states", true);
//        HashMapVector queryMap = query.hashMapVector();
//        Map<String, Weight> queryTokens = queryMap.hashMap;
//        for (String str: queryTokens.keySet()) {
//            System.out.println(index.tokenHash.get(str));
//        }
        List<Query> queries = new LinkedList<>();
        parseQueries(queries, "data\\topics_MB1-49.txt");
        System.out.println("Parsed " + queries.size() + " queries.");
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
                //First line sometimes has header chars attached to it. Can upset BigInteger. Replace w empty strings
                Example newExample = new Example(newDoc.hashMapVector(), 0, tuple[0].replaceAll("[^0-9]", ""), newDoc);
                //Add this example to this list of tweets
                tweets.add(newExample);
            }
        } catch (IOException e) {
            System.out.println(e.toString() + ": Error while reading file on line " + lineCount);
            return false;
        }
        return true;
    }

    private static boolean parseQueries(List<Query> queryList, String filePath){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document;
        try {
            //no root element, need to accomodate
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            List<InputStream> stream = Arrays.asList(
                    new ByteArrayInputStream("<root>".getBytes()),
                            fis,
                            new ByteArrayInputStream(("</root>".getBytes()))
            );

            InputStream cntr =  new SequenceInputStream(Collections.enumeration(stream));
            builder = factory.newDocumentBuilder();
            document = builder.parse(cntr);
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }

        //Element root = document.getDocumentElement();

        NodeList nodes = document.getElementsByTagName("top");
        Node node;
        Element element;
        String id;
        String query;
        for (int i = 0; i < nodes.getLength(); i++){
            node = nodes.item(i);
            element = (Element) node;
            try {
                id = element.getElementsByTagName("num").item(0).getTextContent();
                query = element.getElementsByTagName("title").item(0).getTextContent();
                queryList.add(new Query(id, query));
            } catch (Exception e) {
                continue;
            }
        }
        return true;
    }
}
