package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import com.sun.source.tree.Tree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {
    //initialize textfields
    public TextField precision;
    public TextField accuracy;

    //initialize frequency maps for ham and spam
    private Map<String, Integer> trainHamFreq = new TreeMap<>();
    private Map<String, Integer> trainSpamFreq = new TreeMap<>();

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

    //trainHamFreq<Map of words, number of files containing that word in ham folder>
    //trainSpamFreq<Map of words, number of files containing that word in spam folder>

    public static void getFileWordCount(){
        sample.WordCounter wc = new sample.WordCounter();
        File spamFile = new File("C:\\Users\\tsoum\\csci2020u-a1\\src\\sample\\data\\train\\spam");
        File hamFile = new File("C:\\Users\\tsoum\\csci2020u-a1\\src\\sample\\data\\train\\hams");
        File hamOut = new File("hamOut.txt");
        File spamOut = new File("spamOut.txt");
        try {
            wc.parseFile(spamFile);
            wc.outputWordCount(2,spamOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wc.parseFile(hamFile);
            wc.outputWordCount(2, hamOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
