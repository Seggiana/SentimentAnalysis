package sample.Bayes;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Dictionary.ConfusionMatrix;
import sample.Dictionary.Record;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BayesController {
    private final Classifier<String, String> bayes = new BayesClassifier<String, String>();
    @FXML
    public Label tab1_label_showText;
    @FXML
    public Button tab1_button_choose;
    @FXML
    public Label label_bayes_cm;
    @FXML
    public Label label_bayes_accuracy;
    ArrayList<String> tweets;
    ArrayList<Record> recordList = new ArrayList<>();
    ArrayList<Record> trainPositive;
    ArrayList<Record> trainNegative;
    ArrayList<Record> testData = new ArrayList<>();

    @FXML
    public void handleSubmit() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Paths.get("..").
                toAbsolutePath().normalize().toString()));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        initTweets(selectedFile.getAbsolutePath());
        bayes.setMemoryCapacity(7000);
        for (Record r : trainPositive) {
            bayes.learn("true", r.getWordList());
        }
        for (Record r : trainNegative) {
            bayes.learn("false", r.getWordList());
        }
        for (Record r : testData) {
            r.setPredictionBayes(Boolean.parseBoolean(bayes.classify(r.getWordList()).getCategory()));
        }
        ConfusionMatrix cmB = new ConfusionMatrix();
        cmB.setcmB(testData);
        label_bayes_cm.setText("Macierz pomyłek Bayesa: \n\t True \t\t False \n True " + cmB.getCm()[0][0] + "\t\t"
                + cmB.getCm()[0][1] + " \n False " + cmB.getCm()[1][0] + "\t\t" + cmB.getCm()[1][1]);
        label_bayes_cm.setVisible(true);
        label_bayes_accuracy.setText("Bayes accuracy: " + cmB.countAccuracy());
        label_bayes_accuracy.setVisible(true);
    }

    private void initTweets(String absolutePath) {
        csvToList(new File(absolutePath));
        ArrayList<Record> positive = new ArrayList<>();
        ArrayList<Record> negative = new ArrayList<>();
        for (Record r : recordList) {
            r.cleanText();
        }
        for (Record record : recordList) {
            if (record.getTextClass()) {
                positive.add(record);
            } else {
                negative.add(record);
            }
        }
        trainPositive = saveToArray(positive, 0, (int) (positive.size() * 0.7) - 1);
        trainNegative = saveToArray(negative, 0, (int) (negative.size() * 0.7) - 1);
        saveTestData(positive, (int) (positive.size() * 0.7), positive.size() - 1);
        saveTestData(negative, (int) (negative.size() * 0.7), negative.size() - 1);
    }

    private ArrayList<Record> saveToArray(ArrayList<Record> list, int fromID, int toID) {
        ArrayList<Record> savedArray = new ArrayList<>();
        for (int i = fromID; i <= toID; i++) {
            savedArray.add(list.get(i));
        }
        return savedArray;
    }

    private void saveTestData(ArrayList<Record> list, int fromID, int toID) {
        for (int i = fromID; i <= toID; i++) {
            testData.add(list.get(i));
        }
    }

    private void csvToList(File file) {
        int i = 0;
        tweets = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(file.getAbsolutePath()),
                StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                tweets.add(values[0]);
                recordList.add(fillTheRecord(values, i));
                i++;
            }
        } catch (IOException ignored) {
        }
    }

    private Record fillTheRecord(String[] values, int i) {
        Record r = new Record(i);
        r.setText(values[0]);
        r.setTextClass(Boolean.parseBoolean((values[1])));
        return r;
    }

    public void init() {
        Font font = new Font("Times New Roman", 14);
        tab1_label_showText.setFont(font);
        tab1_button_choose.setFont(font);
        label_bayes_cm.setFont(font);
        label_bayes_accuracy.setFont(font);
    }

    public Classifier<String, String> getBayes() {
        return bayes;
    }
}
