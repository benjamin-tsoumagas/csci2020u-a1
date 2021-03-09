package sample;

import java.io.*;
import java.util.*;

public class WordCounter{

    //initialize wordCounts as a Map
    private Map<String, Integer> wordCounts;

    //initialize repeatedWords as a HashSet
    HashSet<String> repeatedWords = new HashSet<>();

    //initialize a WordCounter object with a new TreeMap wordCounts
    public WordCounter(){
        wordCounts = new TreeMap<>();
    }

    //method to clear our HashSet of repeated words
    public void resetRepeat(){
        repeatedWords.clear();
    }

    //parses the file/directory
    //obtains number of times each word appears in the target
    public void parseFile(File file) throws IOException{
        System.out.println("Starting parsing the file:" + file.getAbsolutePath());
        resetRepeat();
        if(file.isDirectory()){
            //parse each file inside the directory
            File[] content = file.listFiles();
            for(File current: content){
                parseFile(current);
            }
        }else {
            Scanner scanner = new Scanner(file);
            // scanning token by token
            while (scanner.hasNext()) {
                String token = scanner.next();
                //if word is valid and has been used in another file
                if (isValidWord(token) && !repeatedWords.contains(token)) {
                    //increment count for files the word has appeared in
                    countWord(token);
                }
                //add the word to the HashSet which sorts and ensures no duplication
                repeatedWords.add(token);
            }
        }
    }

    private boolean isValidWord(String word){
        String allLetters = "^[a-zA-Z]+$";
        // returns true if the word is composed by only letters otherwise returns false;
        return word.matches(allLetters);
    }

    private void countWord(String word) {
        if (wordCounts.containsKey(word)) {
            int previous = wordCounts.get(word);
            wordCounts.put(word, previous + 1);
        } else {
            wordCounts.put(word, 1);
        }
    }

    public void outputWordCount(int minCount, File output) throws IOException{
        System.out.println("Saving word counts to file:" + output.getAbsolutePath());
        System.out.println("Total words:" + wordCounts.keySet().size());

        if (!output.exists()){
            output.createNewFile();
            if (output.canWrite()){
                PrintWriter fileOutput = new PrintWriter(output);

                Set<String> keys = wordCounts.keySet();
                Iterator<String> keyIterator = keys.iterator();

                while(keyIterator.hasNext()){
                    String key = keyIterator.next();
                    int count = wordCounts.get(key);

                    // testing minimum number of occurrences
                    if(count>=minCount){
                        fileOutput.println(key + ":" + count);
                    }
                }

                fileOutput.close();
            }
        }else{
            System.out.println("Error: the output file already exists: " + output.getAbsolutePath());
        }

    }

    //main method
    public static void main(String[] args) {

        if(args.length < 2){
            System.err.println("Usage: java WordCounter <inputDir> <outfile>");
            System.exit(0);
        }

        File dataDir = new File(args[0]);
        File outFile = new File(args[1]);

        WordCounter wordCounter = new WordCounter();
        System.out.println("Hello");
        try{
            wordCounter.parseFile(dataDir);
            wordCounter.outputWordCount(2, outFile);
        }catch(FileNotFoundException e){
            System.err.println("Invalid input dir: " + dataDir.getAbsolutePath());
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
