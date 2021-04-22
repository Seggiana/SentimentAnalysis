package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MLMController {
    @FXML
    public Label tab1_label_showText;
    @FXML
    public Button tab1_button_choose;
    @FXML
    public Label label_SNLP_cm;
    @FXML
    public Label label_SNLP_accuracy;
    ArrayList<String> tweets;
    List<Record> recordList = new ArrayList<>();
    ConfusionMatrix cmS;

    @FXML
    public void handleSubmit() throws Exception {
        tab1_label_showText.setText("Resulted");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Paths.get("..").
                toAbsolutePath().normalize().toString()));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        tab1_label_showText.setText("Processing Stanford Classifier");
        NLP.init();
        initTweets(selectedFile.getAbsolutePath());
        tab1_label_showText.setText("Select file to process");
        cmS = new ConfusionMatrix();
        cmS.setcmS(recordList);
        printSummary();
    }


    private void initTweets(String absolutePath) {
        csvToList(new File(absolutePath));
        for (Record r : recordList) {
            r.cleanText();
            r.setPredictionNLP(NLP.findSentiment(r.getText()) >= 2);
        }
    }

    private void csvToList(File file) {
        int i = 0;
        tweets = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(file.getAbsolutePath()),
                StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
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
        r.setPredictedClass(Boolean.parseBoolean((values[1])));
        return r;
    }

    private void printSummary() {
        label_SNLP_cm.setText("NLP confusion matrix: \n\t True \t\t False \n True " + cmS.getCm()[0][0] + "\t\t"
                + cmS.getCm()[0][1] + " \n False " + cmS.getCm()[1][0] + "\t\t\t" + cmS.getCm()[1][1]);
        label_SNLP_cm.setVisible(true);
        label_SNLP_accuracy.setText("Stanford NLP classifier accuracy: " + cmS.countAccuracy());
        label_SNLP_accuracy.setVisible(true);
    }

}
