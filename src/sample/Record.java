package sample;

import java.util.List;

public class Record {
    private int id;
    private List<String> text;
    private boolean predictionAFINN;
    private boolean predictionLexicon;
    private boolean predictedClass;

    public Record(int id) {
        this.id = id;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
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

    public boolean getPredictionLexicon() { return predictionLexicon; }

    public void setPredictionLexicon(boolean predictionLexicon) { this.predictionLexicon = predictionLexicon; }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", Text=" + text +
                ", AFINN prediction=" + predictionAFINN +
                ", Lexicon prediction=" + predictionLexicon +
                ", Predicted Class=" + predictedClass +
                '}';
    }
}
