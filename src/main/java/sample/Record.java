package sample;

import java.util.List;

public class Record {
    private int id;
    private List<String> wordList;
    private boolean predictionAFINN;
    private boolean predictionLexicon;
    private boolean predictionNLP;
    private boolean predictedClass;
    private String text;

    public Record(int id) {
        this.id = id;
    }

    public List<String> getWordList() {
        return wordList;
    }

    public void setWordList(List<String> wordList) {
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
}
