package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class LearningController {
    public Button tab1_button_choose;
    @FXML
    private Label tab1_label_showText;
    @FXML
    private Label label_cmA;
    @FXML
    private Label label_cmL;
    @FXML
    private Label label_accuracyA;
    @FXML
    private Label label_accuracyL;

    private Dictionary dictionary;
    private ArrayList<Record> listOfRecords = new ArrayList<>();
    private ConfusionMatrix cmAFINN = new ConfusionMatrix();
    private ConfusionMatrix cmLexicon = new ConfusionMatrix();

    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Paths.get("..").
                toAbsolutePath().normalize().toString()));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        csvToList(selectedFile);
        analyze();
        setLabels();
    }

    private void csvToList(File file) {
        int i = 0;
        listOfRecords.clear();
        cmAFINN.clear();
        cmLexicon.clear();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(file.getAbsolutePath()),
                StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                listOfRecords.add(fillTheRecord(values, i));
                i++;
            }
        } catch (IOException ignored) {
        }
    }

    private Record fillTheRecord(String[] values, int i) {
        Record r = new Record(i);
        r.setText(values[0]);
        r.cleanText();
        r.setWordList(textToList(r.getText()));
        r.setTextClass(Boolean.parseBoolean((values[1])));
        return r;
    }

    private ArrayList<String> textToList(String text) {
        String[] s = text.split("[^\\w']+");
        return new ArrayList<>(Arrays.asList(s));
    }

    private void analyze() {
        for (Record r : listOfRecords) {
            r.setPredictionAFINN(dictionary.calculateValue(r.getWordList(), true));
            r.setPredictionLexicon(dictionary.calculateValue(r.getWordList(), false));
        }
        cmAFINN.setcmA(listOfRecords);
        cmLexicon.setcmL(listOfRecords);
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    private void setLabels() {
        label_cmA.setText("AFINN confusion matrix: \n\t True \t\t False \n True " + cmAFINN.getCm()[0][0] + "\t\t"
                + cmAFINN.getCm()[0][1] + " \n False " + cmAFINN.getCm()[1][0] + "\t\t\t" + cmAFINN.getCm()[1][1]);
        label_cmA.visibleProperty().set(true);
        label_accuracyA.setText("Accuracy: " + cmAFINN.countAccuracy());
        label_accuracyA.visibleProperty().set(true);
        label_cmL.setText("Lexicon confusion matrix: \n\t True \t\t False \n True " + cmLexicon.getCm()[0][0] + "\t\t"
                + cmLexicon.getCm()[0][1] + " \n False " + cmLexicon.getCm()[1][0] + "\t\t\t" + cmLexicon.getCm()[1][1]);
        label_cmL.visibleProperty().set(true);
        label_accuracyL.setText("Accuracy: " + cmLexicon.countAccuracy());
        label_accuracyL.visibleProperty().set(true);
    }
}