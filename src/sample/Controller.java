package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Controller {
    //initialize text fields
    public TextField precision;
    public TextField accuracy;

    //initialize frequency maps for ham and spam
    public static Map<String, Integer> trainHamFreq = new TreeMap<>();
    public static Map<String, Integer> trainSpamFreq = new TreeMap<>();

    //initialize probability maps
    public static Map<String, Integer> wordInHam = new TreeMap<>();
    public static Map<String, Integer> wordInSpam = new TreeMap<>();
    public static Map<String, Integer> fileIsSpam = new TreeMap<>();

    //initialize values for table and columns
    @FXML
    private TableView<SpamDetector> tabView;
    @FXML
    private TableColumn<Object, Object> file;
    @FXML
    private TableColumn<Object, Object> type;
    @FXML
    private TableColumn<Object, Object> prob;

    public static class SpamDetector {
        //initialize important attributes for each file
        private final String fileName;
        private final String classType;
        private final double spamProb;

        //constructor for SpamDetector
        public SpamDetector(String fileName, String classType, double spamProb) {
            this.fileName = fileName;
            this.classType = classType;
            this.spamProb = spamProb;
        }

        //accessors for SpamDetector
        public String getFileName() { return fileName; }
        public String getClassType() { return classType; }
        public double getSpamProb() { return spamProb; }
    }

    //create an observable list that is put into the table
    public static class EmailSource {
        public static ObservableList<SpamDetector> getAllEmails() {
            ObservableList<SpamDetector> emails = FXCollections.observableArrayList();
            return emails;
        }
    }

    //initialize table and columns
    public void initialize(){
        file.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        type.setCellValueFactory(new PropertyValueFactory<>("classType"));
        prob.setCellValueFactory(new PropertyValueFactory<>("spamProb"));
        tabView.setItems(EmailSource.getAllEmails());
    }

    //method to return number of files in a directory
    public static int getTotalFiles(File file){
        int numberFiles = 0;
        File[] listFiles = file.listFiles();
        for (File current : listFiles) {
            numberFiles++;
        }
        return numberFiles;
    }

    //method to get number of files that contain unique words
    public static void getFileWordCount(){
        //create an instance of the WordCounter class
        WordCounter wc = new WordCounter();
        //setup directories to read from
        File spamFile = new File("./src/sample/data/train/spam");
        File hamFile = new File("./src/sample/data/train/hams");
        //setup files to output the map to in the format 'word: count'
        File hamOut = new File("hamOut.txt");
        File spamOut = new File("spamOut.txt");
        //setup file counts for spam/ham/total files
        int spamCount = getTotalFiles(spamFile);
        int hamCount = getTotalFiles(hamFile);
        int totalCount = hamCount + spamCount;

        try {
            //parse spam files and output how many files each word appears in, in a file
            wc.parseFile(spamFile);
            wc.outputWordCount(2,spamOut);
            //scan every spam file
            Scanner spamScan = new Scanner(new FileReader(spamOut));
            while (spamScan.hasNextLine()){
                //obtain word and count for each line
                String[] columns = spamScan.nextLine().split(":");
                //turn count from string to int
                int spamInt = Integer.parseInt(columns[1]);
                //input each word count pair into a map
                trainSpamFreq.put(columns[0],spamInt);
                //input each word probability pair into a map
                wordInSpam.put(columns[0],spamInt/spamCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //parse ham files and output how many files each word appears in, in a file
            wc.parseFile(hamFile);
            wc.outputWordCount(2, hamOut);
            //scan every ham file
            Scanner hamScan = new Scanner(new FileReader(hamOut));
            while (hamScan.hasNextLine()){
                //obtain word and count for each line
                String[] columns = hamScan.nextLine().split(":");
                //turn count from string to int
                int hamInt = Integer.parseInt(columns[1]);
                //input each word count pair into a map
                trainHamFreq.put(columns[0],hamInt);
                //input each word probability pair into a map
                wordInHam.put(columns[0],hamInt/hamCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //merge maps into the form (word, probability file is spam)
        wordInHam.forEach(
                (key,value) -> wordInSpam.merge(key, value, (v1,v2) -> v1.equals(v2) ? v1 : v1/(v1+v2))
        );
    }
}
