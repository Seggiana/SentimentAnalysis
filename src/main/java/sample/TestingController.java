package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestingController {
    @FXML
    private Label lbl_result_AFINN;
    @FXML
    private Label lbl_result_Lexicon;
    @FXML
    private Label lbl_result_SNLP;
    @FXML
    private Label lbl_result_DL4J;
    @FXML
    private TextArea txtA_ToAnalyse;
    private Dictionary dictionary;
    private Record r;
    private WordVectors word2Vectors;
    private TokenizerFactory tokenizerFactory;
    private MultiLayerNetwork net;

    public void handleSubmit() throws IOException {
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
        testDL4J();
        printResults();
    }

    private void printResults() {
        lbl_result_AFINN.setText(r.getPredictionAFINN() ? "Positive" : "Negative");
        lbl_result_Lexicon.setText(r.getPredictionLexicon() ? "Positive" : "Negative");
        lbl_result_SNLP.setText(r.getPredictionNLP() ? "Positive" : "Negative");
        lbl_result_DL4J.setText(r.getPredictionDL4J() ? "Positive" : "Negative");
    }

    private void testDL4J() throws IOException {
        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
        net = MultiLayerNetwork.load(new File("C:\\Users\\Toshiba\\Desktop\\SEMESTR 10\\MGR\\SentimentAnalysis\\NewsModel.net"), true);
        DataSet testNews = prepareTestData(txtA_ToAnalyse.textProperty().getValue());
        INDArray fet = testNews.getFeatures();
        INDArray predicted = net.output(fet, false);
        long[] arrsiz = predicted.shape();
        File categories = new File("C:\\Users\\Toshiba\\Desktop\\SEMESTR 10\\MGR\\SentimentAnalysis\\LabelledNews\\categories.txt");
        double max = 0;
        int pos = 0;
        for (int i = 0; i < arrsiz[1]; i++) {
            if (max < (double) predicted.slice(0).getRow(i).sumNumber()) {
                max = (double) predicted.slice(0).getRow(i).sumNumber();
                pos = i;
            }
        }
        try (BufferedReader brCategories = new BufferedReader(new FileReader(categories))) {
            String temp;
            List<String> labels = new ArrayList<>();
            while ((temp = brCategories.readLine()) != null) {
                labels.add(temp);
            }
            brCategories.close();
            r.setPredictionDL4J(!labels.get(pos).split(",")[1].equals("positive"));
        } catch (Exception e) {
            System.out.println("File Exception : " + e.getMessage());
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private DataSet prepareTestData(String text) {

        List<String> news = new ArrayList<>(1);
        int[] category = new int[1];
        news.add(text);

        List<List<String>> allTokens = new ArrayList<>(news.size());
        int maxLength = 0;
        for (String s : news) {
            List<String> tokens = tokenizerFactory.create(s).getTokens();
            List<String> tokensFiltered = new ArrayList<>();
            for (String t : tokens) {
                if (word2Vectors.hasWord(t)) tokensFiltered.add(t);
            }
            allTokens.add(tokensFiltered);
            maxLength = Math.max(maxLength, tokensFiltered.size());
        }

        INDArray features = Nd4j.create(news.size(), word2Vectors.lookupTable().layerSize(), maxLength);
        INDArray labels = Nd4j.create(news.size(), 2, maxLength);    //labels: Crime, Politics, Bollywood, Business&Development
        INDArray featuresMask = Nd4j.zeros(news.size(), maxLength);
        INDArray labelsMask = Nd4j.zeros(news.size(), maxLength);

        int[] temp = new int[2];
        for (int i = 0; i < news.size(); i++) {
            List<String> tokens = allTokens.get(i);
            temp[0] = i;
            for (int j = 0; j < tokens.size() && j < maxLength; j++) {
                String token = tokens.get(j);
                INDArray vector = word2Vectors.getWordVectorMatrix(token);
                features.put(new INDArrayIndex[]{NDArrayIndex.point(i),
                                NDArrayIndex.all(),
                                NDArrayIndex.point(j)},
                        vector);

                temp[1] = j;
                featuresMask.putScalar(temp, 1.0);
            }
            int idx = category[i];
            int lastIdx = Math.min(tokens.size(), maxLength);
            labels.putScalar(new int[]{i, idx, lastIdx - 1}, 1.0);
            labelsMask.putScalar(new int[]{i, lastIdx - 1}, 1.0);
        }

        return new DataSet(features, labels, featuresMask, labelsMask);
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void setWord2Vectors(WordVectors word2Vectors) {
        this.word2Vectors = word2Vectors;
    }
}
