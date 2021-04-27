package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MLMController {
    @FXML
    public Label tab1_label_showText;
    @FXML
    public Button tab1_button_choose;
    @FXML
    public Label label_SNLP_cm;
    @FXML
    public Label label_DL4j_NLP_cm;
    @FXML
    public Label label_DL4j_NLP_accuracy;
    @FXML
    public Label label_SNLP_accuracy;

    ArrayList<String> tweets;
    ArrayList<Record> recordList = new ArrayList<>();
    ConfusionMatrix cmS;
    private DL4JNLP dl4JNLP;

    @FXML
    public void handleSubmit() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Paths.get("..").
                toAbsolutePath().normalize().toString()));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        tab1_label_showText.setText("Processing Stanford Classifier");
        CoreNLP.init();
        initTweets(selectedFile.getAbsolutePath());
        cmS = new ConfusionMatrix();
        cmS.setcmS(recordList);
        printSummary();
        dl4JNLP = new DL4JNLP();
        dl4JNLP.init(recordList);
        ConfusionMatrix conf = dl4JNLP.con;
        label_DL4j_NLP_cm.setText("Macierz pomyłek DL4J: \n\t True \t\t False \n True " + conf.getCm()[0][0] + "\t\t\t"
                + conf.getCm()[0][1] + " \n False " + conf.getCm()[1][0] + "\t\t\t" + conf.getCm()[1][1]);
        label_DL4j_NLP_cm.setVisible(true);
        label_DL4j_NLP_accuracy.setText("DL4J accuracy: " + dl4JNLP.getAccuracy());
        label_DL4j_NLP_accuracy.setVisible(true);
    }

    private void initTweets(String absolutePath) {
        csvToList(new File(absolutePath));
        for (Record r : recordList) {
            r.cleanText();
            r.setPredictionNLP(CoreNLP.findSentiment(r.getText()) >= 2);
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
        r.setTextClass(Boolean.parseBoolean((values[1])));
        return r;
    }

    private void printSummary() {
        label_SNLP_cm.setText("Macierz pomyłek Stanford NLP: \n\t True \t\t False \n True " + cmS.getCm()[0][0] + "\t\t\t"
                + cmS.getCm()[0][1] + " \n False " + cmS.getCm()[1][0] + "\t\t\t" + cmS.getCm()[1][1]);
        label_SNLP_cm.setVisible(true);
        label_SNLP_accuracy.setText("Stanford NLP accuracy: " + cmS.countAccuracy());
        label_SNLP_accuracy.setVisible(true);
    }

    public DL4JNLP getDl4JNLP() {
        return dl4JNLP;
    }

    public void init() {
        Font font = new Font("Times New Roman", 14);
        tab1_label_showText.setFont(font);
        tab1_button_choose.setFont(font);
        label_SNLP_cm.setFont(font);
        label_DL4j_NLP_cm.setFont(font);
        label_DL4j_NLP_accuracy.setFont(font);
        label_SNLP_accuracy.setFont(font);
    }
}
