package sample;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import sample.Bayes.BayesController;
import sample.CoreNLP.CoreController;
import sample.DL4J.DL4JController;
import sample.Dictionary.Dictionary;
import sample.Dictionary.DictionaryController;
import sample.Testing.TestingController;
import sample.WelcomePage.WelcomeController;

import java.io.IOException;

public class RootController {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tab_welcome;
    @FXML
    private Tab tab_learning;
    @FXML
    private Tab tab_testing;
    @FXML
    private Tab tab_mlm;
    @FXML
    private Tab tab_core;
    @FXML
    private Tab tab_bayes;
    @FXML
    private WelcomeController fxml_welcomeController;
    @FXML
    private DictionaryController fxml_learningController;
    @FXML
    private TestingController fxml_testingController;
    @FXML
    private DL4JController fxml_mlmController;
    @FXML
    private CoreController fxml_coreController;
    @FXML
    private BayesController fxml_bayesController;
    private Dictionary dictionary = new Dictionary();

    public void init() throws IOException {
        initFXMLs();
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable,
                                                                        Tab oldValue, Tab newValue) -> {
            if (newValue == tab_welcome) {
                System.out.println("Controller = " + fxml_welcomeController);
            } else if (newValue == tab_testing) {
                fxml_testingController.setDictionary(dictionary);
                //fxml_testingController.setWord2Vectors(fxml_mlmController.getDl4JNLP().getWord2Vec());
                fxml_testingController.setBayes(fxml_bayesController.getBayes());
                System.out.println("Controller = " + fxml_testingController);
            } else if (newValue == tab_learning) {
                fxml_learningController.setDictionary(dictionary);
                System.out.println("Controller = " + fxml_learningController);
            } else if (newValue == tab_mlm) {
                System.out.println("Controller = " + fxml_mlmController);
            } else if (newValue == tab_bayes) {
                System.out.println("Controller = " + fxml_bayesController);
            } else if (newValue == tab_core) {
                System.out.println("Controller = " + fxml_coreController);
            } else {
                System.out.println("- another Tab -");
            }
        });
    }

    private void initFXMLs() throws IOException {
        fxml_welcomeController.init();
        fxml_learningController.init();
        fxml_mlmController.init();
        fxml_testingController.init();
        fxml_coreController.init();
        fxml_bayesController.init();
    }
}
