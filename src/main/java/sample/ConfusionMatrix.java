package sample;

import java.util.List;

public class ConfusionMatrix {
    private Integer[][] cm = {{0, 0}, {0, 0}};

    public void setcmA(List<Record> listOfRecords) {
        for (Record r : listOfRecords) {
            if (r.getTextClass() == r.getPredictionAFINN() && r.getTextClass()) {
                cm[0][0]++;
            } else if (r.getTextClass() == r.getPredictionAFINN() && !r.getTextClass()) {
                cm[1][1]++;
            } else if (r.getTextClass() != r.getPredictionAFINN() && r.getTextClass()) {
                cm[0][1]++;
            } else if (r.getTextClass() != r.getPredictionAFINN() && !r.getTextClass()) {
                cm[1][0]++;
            }
        }
    }

    public void setcmL(List<Record> listOfRecords) {
        for (Record r : listOfRecords) {
            if (r.getTextClass() == r.getPredictionLexicon() && r.getTextClass()) {
                cm[0][0]++;
            } else if (r.getTextClass() == r.getPredictionLexicon() && !r.getTextClass()) {
                cm[1][1]++;
            } else if (r.getTextClass() != r.getPredictionLexicon() && r.getTextClass()) {
                cm[0][1]++;
            } else if (r.getTextClass() != r.getPredictionLexicon() && !r.getTextClass()) {
                cm[1][0]++;
            }
        }
    }

    public void setcmS(List<Record> listOfRecords) {
        for (Record r : listOfRecords) {
            if (r.getTextClass() == r.getPredictionNLP() && r.getTextClass()) {
                cm[0][0]++;
            } else if (r.getTextClass() == r.getPredictionNLP() && !r.getTextClass()) {
                cm[1][1]++;
            } else if (r.getTextClass() != r.getPredictionNLP() && r.getTextClass()) {
                cm[0][1]++;
            } else if (r.getTextClass() != r.getPredictionNLP() && !r.getTextClass()) {
                cm[1][0]++;
            }
        }
    }

    public void setcmD(int first, int second, int third, int fourth) {
        cm[0][0] = first;
        cm[0][1] = second;
        cm[1][0] = third;
        cm[1][1] = fourth;
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

    @Override
    public String toString() {
        return "ConfusionMatrix{" +
                "cm=" + cm[0][0] + ", " + cm[0][1] + ", " + cm[1][0] + ", " + cm[1][1] +
                '}';
    }
}
