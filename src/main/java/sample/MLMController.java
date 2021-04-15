package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.*;
import java.nio.file.Paths;

public class MLMController {
    @FXML
    public Label tab1_label_showText;
    @FXML
    public Button tab1_button_choose;
    @FXML
    private TextArea txtA_ToAnalyse;

    @FXML
    public void handleSubmit() throws Exception {
        tab1_label_showText.setText("Resulted");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Paths.get("..").
                toAbsolutePath().normalize().toString()));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        String desFilePath = "C:\\Users\\Toshiba\\Desktop\\SEMESTR 10\\MGR\\analysis.arff";
        Convert(selectedFile.getAbsolutePath(), desFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(desFilePath));
    }
    public static void Convert(String sourcePath,String destPath) throws Exception
    {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(sourcePath));
        Instances data = loader.getDataSet();
        BufferedWriter writer = new BufferedWriter(new FileWriter(destPath));
        writer.write(data.toString());
        writer.flush();
        writer.close();
    }




}
