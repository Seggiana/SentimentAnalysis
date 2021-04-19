package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MLMController {
    @FXML
    public Label tab1_label_showText;
    @FXML
    public Button tab1_button_choose;
    @FXML
    public Label label_cm;
    @FXML
    public Label label_accuracy;
    StringToWordVector stringToWordVector;
    Instances data;
    String desFilePath = "C:\\Users\\Toshiba\\Desktop\\SEMESTR 10\\MGR\\SentimentAnalysis\\analysis.arff";
    Evaluation eval;
    FilteredClassifier filteredClassifier;
    ArrayList<Attribute> attributes;
    Instances train;
    Instances test;

    @FXML
    public void handleSubmit() throws Exception {
        tab1_label_showText.setText("Resulted");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Paths.get("..").
                toAbsolutePath().normalize().toString()));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        Convert(selectedFile.getAbsolutePath(), desFilePath);
        setData();
        initializeFilter();
        stringToWordVector.setInputFormat(data);
        train = Filter.useFilter(train, stringToWordVector);
        test = Filter.useFilter(test, stringToWordVector);
        filteredClassifier.buildClassifier(train);
        eval = new Evaluation(test);
        eval.evaluateModel(filteredClassifier, test);
        printSummary();
    }

    private void setData() throws IOException {
        ArffLoader loader = new ArffLoader();
        loader.setFile(new File(desFilePath));
        data = loader.getDataSet();
        data.setClassIndex(1);
        attributes = new ArrayList<Attribute>();
        ArrayList<Boolean> classVal = new ArrayList<Boolean>();
        attributes.add(new Attribute("text", (ArrayList<String>) null));
        attributes.add(new Attribute("sentiment", String.valueOf(classVal)));
        train = data.trainCV(3, 0);
        test = data.testCV(3, 0);
        train.setClassIndex(1);
        test.setClassIndex(1);
    }

    public static void Convert(String sourcePath, String destPath) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(sourcePath));
        Instances data = loader.getDataSet();
        BufferedWriter writer = new BufferedWriter(new FileWriter(destPath));
        writer.write(data.toString());
        writer.flush();
        writer.close();
    }

    private void initializeFilter() {
        filteredClassifier = new FilteredClassifier();
        filteredClassifier.setClassifier(new NaiveBayesMultinomialText());
        stringToWordVector = new StringToWordVector();
        stringToWordVector.setLowerCaseTokens(true);
        stringToWordVector.setMinTermFreq(1);
        stringToWordVector.setTFTransform(false);
        stringToWordVector.setIDFTransform(false);
        stringToWordVector.setWordsToKeep(1000);
        NGramTokenizer tokenizer = new NGramTokenizer();
        tokenizer.setNGramMinSize(2);
        tokenizer.setNGramMaxSize(5);
        stringToWordVector.setTokenizer(tokenizer);
        stringToWordVector.setAttributeIndices("first");
        filteredClassifier.setFilter(stringToWordVector);
    }

    private void printSummary() throws Exception {
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toMatrixString());
        label_cm.setText(eval.toMatrixString());
        label_accuracy.setText("Accuracy: " + eval.correct() / (eval.incorrect() + eval.correct()));
        label_accuracy.setVisible(true);
        label_cm.setVisible(true);
    }

}
