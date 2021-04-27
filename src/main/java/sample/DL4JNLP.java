package sample;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DL4JNLP {
    private TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
    private Word2Vec word2Vec;
    private String dataLocalPath;
    private double accuracy;
    ConfusionMatrix con;

    public void init(ArrayList<Record> records) throws IOException {
        saveDataFiles(records);
        ArrayList<String> sentences = recordToArrayList(records);
        SentenceIterator sentenceIterator = new CollectionSentenceIterator(sentences);
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        word2Vec = new Word2Vec.Builder()
                .minWordFrequency(2)
                .iterations(5)
                .layerSize(100)
                .seed(42)
                .windowSize(20)
                .iterate(sentenceIterator)
                .tokenizerFactory(tokenizerFactory)
                .build();
        word2Vec.fit();
        readModel();
    }

    public void readModel() throws IOException {
        String DATA_PATH = new File(dataLocalPath, "LabelledNews").getAbsolutePath();
        NewsIterator iTrain = new NewsIterator.Builder()
                .dataDirectory(DATA_PATH)
                .wordVectors(word2Vec)
                .batchSize(50)
                .truncateLength(300)
                .tokenizerFactory(tokenizerFactory)
                .train(true)
                .build();
        NewsIterator iTest = new NewsIterator.Builder()
                .dataDirectory(DATA_PATH)
                .wordVectors(word2Vec)
                .batchSize(50)
                .tokenizerFactory(tokenizerFactory)
                .truncateLength(300)
                .train(false)
                .build();
        int inputNeurons = word2Vec.getWordVector(word2Vec.vocab().wordAtIndex(0)).length;
        int outputs = iTrain.getLabels().size();

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new RmsProp(0.0018))
                .l2(1e-5)
                .weightInit(WeightInit.XAVIER)
                .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue).gradientNormalizationThreshold(1.0)
                .list()
                .layer(new LSTM.Builder()
                        .nIn(inputNeurons)
                        .nOut(200)
                        .activation(Activation.TANH).build())
                .layer(new RnnOutputLayer.Builder()
                        .activation(Activation.SOFTMAX)
                        .lossFunction(LossFunctions.LossFunction.MCXENT)
                        .nIn(200)
                        .nOut(outputs)
                        .build())
                .build();
        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();
        net.setListeners(new ScoreIterationListener(1), new EvaluativeListener(iTest, 1, InvocationType.EPOCH_END));
        net.fit(iTrain, 10);
        Evaluation eval = net.evaluate(iTest);

        con = new ConfusionMatrix();
        con.setcmD(eval.trueNegatives().get(0),
                eval.classCount(0) - eval.trueNegatives().get(0),
                eval.classCount(1) - eval.truePositives().get(0),
                eval.truePositives().get(0));
        System.out.println(eval.stats());
        accuracy = eval.accuracy();
        net.save(new File(dataLocalPath, "NewsModel.net"), true);
    }

    private ArrayList<String> recordToArrayList(ArrayList<Record> recordList) {
        ArrayList<String> sentences = new ArrayList<>();
        for (Record r : recordList) {
            sentences.add(r.getText());
        }
        return sentences;
    }

    private void saveDataFiles(ArrayList<Record> records) throws IOException {
        ArrayList<String> positive = new ArrayList<>();
        ArrayList<String> negative = new ArrayList<>();
        ArrayList<String> trainPositive;
        ArrayList<String> trainNegative;
        ArrayList<String> testPositive;
        ArrayList<String> testNegative;
        for (Record record : records) {
            if (record.getTextClass()) {
                positive.add(record.getText());
            } else {
                negative.add(record.getText());
            }
        }
        trainPositive = saveToArray(positive, 0, (int) (positive.size() * 0.7));
        trainNegative = saveToArray(negative, 0, (int) (negative.size() * 0.7));
        testPositive = saveToArray(positive, (int) (positive.size() * 0.7) + 1, positive.size() - 1);
        testNegative = saveToArray(negative, (int) (negative.size() * 0.7) + 1, negative.size() - 1);
        saveFile(trainPositive, true, true);
        saveFile(trainNegative, true, false);
        saveFile(testPositive, false, true);
        saveFile(testNegative, false, false);
    }

    private ArrayList<String> saveToArray(ArrayList<String> list, int fromID, int toID) {
        ArrayList<String> savedArray = new ArrayList<>();
        for (int i = fromID; i <= toID; i++) {
            savedArray.add(list.get(i));
        }
        return savedArray;
    }

    public void saveFile(ArrayList<String> sentences, boolean isTrain, boolean isPositive) throws IOException {
        File file;
        if (isTrain) {
            if (isPositive) {
                file = new File(dataLocalPath, "LabelledNews\\train\\1.txt");
            } else {
                file = new File(dataLocalPath, "LabelledNews\\train\\0.txt");
            }
        } else {
            if (isPositive) {
                file = new File(dataLocalPath, "LabelledNews\\test\\1.txt");
            } else {
                file = new File(dataLocalPath, "LabelledNews\\test\\0.txt");
            }
        }
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(file));
        for (String s : sentences) {
            outputWriter.write(s);
            outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }

    public double getAccuracy() {
        return accuracy;
    }

    public ConfusionMatrix getCon() {
        return con;
    }

    public Word2Vec getWord2Vec() {
        return word2Vec;
    }
}
