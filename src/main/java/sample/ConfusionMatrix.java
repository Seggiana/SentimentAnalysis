package sample;

import java.util.List;

public class ConfusionMatrix {
    private Integer[][] cm = {{0, 0}, {0, 0}};

    public void setcmA(List<Record> listOfRecords) {
        for (Record r : listOfRecords) {
            if (r.getPredictedClass() == r.getPredictionAFINN() && r.getPredictedClass()) {
                cm[0][0]++;
            } else if (r.getPredictedClass() == r.getPredictionAFINN() && !r.getPredictedClass()) {
                cm[1][1]++;
            } else if (r.getPredictedClass() != r.getPredictionAFINN() && r.getPredictedClass()) {
                cm[0][1]++;
            } else if (r.getPredictedClass() != r.getPredictionAFINN() && !r.getPredictedClass()) {
                cm[1][0]++;
            }
        }
    }

    public void setcmL(List<Record> listOfRecords) {
        for (Record r : listOfRecords) {
            if (r.getPredictedClass() == r.getPredictionLexicon() && r.getPredictedClass()) {
                cm[0][0]++;
            } else if (r.getPredictedClass() == r.getPredictionLexicon() && !r.getPredictedClass()) {
                cm[1][1]++;
            } else if (r.getPredictedClass() != r.getPredictionLexicon() && r.getPredictedClass()) {
                cm[0][1]++;
            } else if (r.getPredictedClass() != r.getPredictionLexicon() && !r.getPredictedClass()) {
                cm[1][0]++;
            }
        }
    }
    public void setcmS(List<Record> listOfRecords) {
        for (Record r : listOfRecords) {
            if (r.getPredictedClass() == r.getPredictionNLP() && r.getPredictedClass()) {
                cm[0][0]++;
            } else if (r.getPredictedClass() == r.getPredictionNLP() && !r.getPredictedClass()) {
                cm[1][1]++;
            } else if (r.getPredictedClass() != r.getPredictionNLP() && r.getPredictedClass()) {
                cm[0][1]++;
            } else if (r.getPredictedClass() != r.getPredictionNLP() && !r.getPredictedClass()) {
                cm[1][0]++;
            }
        }
    }


    public float countAccuracy() {
        return (float) (cm[0][0] + cm[1][1]) / (float) (cm[0][0] + cm[0][1] + cm[1][1] + cm[1][0]);
    }

    public Integer[][] getCm() {
        return cm;
    }

    public void clear() {
        cm[0][0] = 0;
        cm[0][1] = 0;
        cm[1][0] = 0;
        cm[1][1] = 0;
    }
}
