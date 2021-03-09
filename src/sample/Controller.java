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

    //method to get number of files that contain unique words
    public static void getFileWordCount(){
        //create an instance of the WordCounter class
        sample.WordCounter wc = new sample.WordCounter();
        //setup directories to read from
        File spamFile = new File("./src/sample/data/train/spam");
        File hamFile = new File("./src/sample/data/train/hams");
        //setup files to output the map to in the format 'word: count'
        File hamOut = new File("hamOut.txt");
        File spamOut = new File("spamOut.txt");

        try {
            wc.parseFile(spamFile);
            wc.outputWordCount(2,spamOut);
            Scanner spamScan = new Scanner(new FileReader(spamOut));
            while (spamScan.hasNextLine()){
                String[] columns = spamScan.nextLine().split(":");
                int spamInt = Integer.parseInt(columns[1]);
                trainSpamFreq.put(columns[0],spamInt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wc.parseFile(hamFile);
            wc.outputWordCount(2, hamOut);
            Scanner hamScan = new Scanner(new FileReader(hamOut));
            while (hamScan.hasNextLine()){
                String[] columns = hamScan.nextLine().split(":");
                int hamInt = Integer.parseInt(columns[1]);
                trainHamFreq.put(columns[0],hamInt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
