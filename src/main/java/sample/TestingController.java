package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;

public class TestingController {
    @FXML
    private Label lbl_result_AFINN;
    @FXML
    private Label lbl_result_Lexicon;
    @FXML
    private Label lbl_result_SNLP;
    @FXML
    private TextArea txtA_ToAnalyse;
    Dictionary dictionary;
    Record r;

    public void handleSubmit() throws FileNotFoundException {
        lbl_result_AFINN.setText("---");
        lbl_result_Lexicon.setText("---");
        lbl_result_SNLP.setText("---");
        String textToTest = txtA_ToAnalyse.textProperty().getValue();
        System.out.println(textToTest);
        r = new Record(0);
        r.setText(textToTest);
        r.setPredictionAFINN(dictionary.calculateValue(r.getWordList(), true));
        r.setPredictionLexicon(dictionary.calculateValue(r.getWordList(), false));
        CoreNLP.init();
        r.setPredictionNLP(CoreNLP.findSentiment(r.getText()) >= 2);
        printResults();


    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    private void printResults() {
        lbl_result_AFINN.setText(r.getPredictionAFINN() ? "Positive" : "Negative");
        lbl_result_Lexicon.setText(r.getPredictionLexicon() ? "Positive" : "Negative");
        lbl_result_SNLP.setText(r.getPredictionNLP() ? "Positive" : "Negative");
    }
}
