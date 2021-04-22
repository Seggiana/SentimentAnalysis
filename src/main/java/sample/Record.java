package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Record {
    private int id;
    private ArrayList<String> wordList;
    private boolean predictionAFINN;
    private boolean predictionLexicon;
    private boolean predictionNLP;
    private boolean predictedClass;
    private String text;

    public Record(int id) {
        this.id = id;
    }

    public ArrayList<String> getWordList() {
        return wordList;
    }

    public void setWordList(ArrayList<String> wordList) {
        this.wordList = wordList;
    }

    public boolean getPredictionAFINN() {
        return predictionAFINN;
    }

    public void setPredictionAFINN(boolean predictionAFINN) {
        this.predictionAFINN = predictionAFINN;
    }

    public boolean getPredictedClass() {
        return predictedClass;
    }

    public void setPredictedClass(boolean predictedClass) {
        this.predictedClass = predictedClass;
    }

    public boolean getPredictionLexicon() {
        return predictionLexicon;
    }

    public void setPredictionLexicon(boolean predictionLexicon) {
        this.predictionLexicon = predictionLexicon;
    }

    public boolean getPredictionNLP() {
        return predictionNLP;
    }

    public void setPredictionNLP(boolean predictionNLP) {
        this.predictionNLP = predictionNLP;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        cleanText();
        wordList = textToList(text);
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", Text=" + wordList +
                ", AFINN prediction=" + predictionAFINN +
                ", Lexicon prediction=" + predictionLexicon +
                ", Predicted Class=" + predictedClass +
                '}';
    }

    public void cleanText() {
        text.trim().replaceAll("http.*?[\\S]+", "")
                .replaceAll("@[\\S]+", "")
                .replaceAll("#", "")
                .replaceAll("[\\s]+", " ");
    }
    private ArrayList<String> textToList(String text) {
        String[] s = text.split("[^\\w']+");
        return new ArrayList<>(Arrays.asList(s));
    }
}
