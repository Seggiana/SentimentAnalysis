package sample;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;

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
    private WelcomeController fxml_welcomeController;
    @FXML
    private LearningController fxml_learningController;
    @FXML
    private TestingController fxml_testingController;
    @FXML
    private MLMController fxml_mlmController;
    private Dictionary dictionary = new Dictionary();
    //private WordVectors word2Vector;

    public void init() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable,
                                                                        Tab oldValue, Tab newValue) -> {
            if (newValue == tab_welcome) {
                fxml_welcomeController.init();
                System.out.println("Controller = " + fxml_welcomeController);
            } else if (newValue == tab_testing) {
                //word2Vector = fxml_mlmController.getDl4JNLP().getWord2Vec();
                fxml_testingController.setDictionary(dictionary);
                fxml_testingController.setWord2Vectors(fxml_mlmController.getDl4JNLP().getWord2Vec());
                System.out.println("Controller = " + fxml_testingController);
            } else if (newValue == tab_learning) {
                fxml_learningController.setDictionary(dictionary);
                System.out.println("Controller = " + fxml_learningController);
            } else if (newValue == tab_mlm) {
                System.out.println("Controller = " + fxml_mlmController);
            } else {
                System.out.println("- another Tab -");
            }
        });
    }
}
