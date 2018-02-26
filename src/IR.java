import ir.utilities.Weight;
import ir.vsr.*;
import ir.utilities.StopWords;
import ir.classifiers.Example;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

//import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.w3c.dom.*;
import org.w3c.dom.Document;

import javax.xml.parsers.*;
import java.io.*;

public class IR {
    public static void main(String[] args) {
        String tweetsLoc = "data\\Trec_microblog11.txt";
        String stopWordsLoc = "data\\StopWords.txt";
        String queriesLoc = "data\\topics_MB1-49.txt";

        if (args.length > 2) {
            queriesLoc = args[2];
        }
        if (args.length > 1) {
            stopWordsLoc = args[1];
        }
        if (args.length > 0) {
            tweetsLoc = args[0];
        }
        //Start
        System.out.println("Hi, I'm gonna read some tweets now");
        List<Example> tweets = new LinkedList<>();
        //Load stopwords
        try {
            StopWords.changeStopWordsLocation(stopWordsLoc);
        } catch (Exception e) {
            System.out.println("Error reading stopwords at " + stopWordsLoc);
            System.exit(0);
        }
        try {
            if (tokenizeLines(tweets, tweetsLoc)) {
                System.out.println("Read " + tweets.size() + " tweets");
            } else {
                System.out.println("Failed to read tweets");
                System.exit(0);
            }
        }catch (Exception e) {
            System.out.println("Error reading tweets at " + tweetsLoc);
            System.exit(0);
        }
        //Next step: turn our dictionary of stemmed and stopword-removed tweets into an inverted index.
        InvertedIndex index = new InvertedIndex(tweets);
        System.out.println("Successfully added " + index.size() + " terms to an inverted index.");

        List<Query> queries = new LinkedList<>();
        parseQueries(queries, queriesLoc);
        System.out.println("Parsed " + queries.size() + " queries.");

//        Retrieval[] results;
//        int i;
//        for (Query query: queries) {
//            results = index.retrieve(query.query);
//            i = 0;
//            for (Retrieval result: results) {
//                i++;
//                System.out.println(query.id + "\tQ0\t" + result.docRef.toString() +
//                "\t" + i + "\t" + result.score  + "\t" + "aRun");
//                if (i > 1000) { break; }
//            }
//        }

        String runName = "aRun";
        if(writeQueryResults(queries, index, runName)) {
            System.out.println("Wrote results to file " + runName + ".txt");
        }
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
        } catch (FileNotFoundException f) {
            System.out.println("Error reading query file " + filePath);
            return false;
        }
        catch (Exception e) {
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
                id = element.getElementsByTagName("num").item(0).getTextContent().split(" ")[2];
                query = element.getElementsByTagName("title").item(0).getTextContent();
                queryList.add(new Query(id, query));
            } catch (Exception e) {
                continue;
            }
        }
        return true;
    }

    static private boolean writeQueryResults(List<Query> queries, InvertedIndex index, String runName) {
        Retrieval[] results;
        int i;

        //check if runName is all whitespace
        if (runName.replaceAll("[\\s]", "").equals("")) {
            System.out.println("Cannot have empty string for run name");
            return false;
        }

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(runName + ".txt", "UTF-8");
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        for (Query query: queries) {
            results = index.retrieve(query.query);
            i = 0;
            for (Retrieval result: results) {
                i++;
                writer.println(query.id + "\tQ0\t" + result.docRef.toString() +
                        "\t" + i + "\t" + result.score  + "\t" + runName);
                if (i >= 1000) { break; }
            }
        }
        writer.close();
        return true;
    }
}
