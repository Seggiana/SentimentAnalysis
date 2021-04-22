package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new StackPane(), 800, 400);
        URL url = new File("src/main/java/sample/fxml/fxml_root.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        scene.setRoot(loader.load());
        RootController controller = loader.getController();
        controller.init();

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Sentiment Analysis");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
